package tools.starcitizen.reader.product;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import tools.starcitizen.config.SourceConfig;
import tools.starcitizen.entity.common.AttachableComponentParams;
import tools.starcitizen.entity.common.ItemPurchasableParams;
import tools.starcitizen.entity.mapstruct.ProductMapper;
import tools.starcitizen.entity.product.ProductEntity;
import tools.starcitizen.entity.product.rent.ShopRentalTemplate;
import tools.starcitizen.reader.common.AttachableComponentReader;
import tools.starcitizen.reader.common.ItemPurchasableReader;
import tools.starcitizen.reader.common.StarCitizenReader;
import tools.starcitizen.util.PropertiesReadUtil;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: jiawei
 * @Date: 2020/10/7
 * @Description: 读取贸易文件,与普通的组件解析不通用
 */
@Slf4j
public class ProductXmlReader extends StarCitizenReader<ProductEntity> {

    private static final String BATH_PATH = SourceConfig.BATH_PATH;
    private static final String DIR = "/Libs/Subsumption/Shops";
    private static final String LAYOUTS_NAME = "/ShopLayouts.xml";
    //租用模板目录
    private static final String RENT_TEMPLATE_DIR = "/Templates/ShipRentalPeriods";
    private static final String RETAIL_PRODUCT_PRICES_NAME = "/RetailProductPrices.xml";
    private static final ItemPurchasableReader itemPurchasableReader = new ItemPurchasableReader();
    private static final AttachableComponentReader attachableComponentReader = new AttachableComponentReader();
    private static final Map<String,Map<String,String>> pathIdFileCache = new ConcurrentHashMap<>();
    private ReentrantLock lock = new ReentrantLock();


    @Override
    public Map<Class<ProductEntity>, Map<String, ProductEntity>> read() {
        File retailFile = new File(BATH_PATH+ DIR + RETAIL_PRODUCT_PRICES_NAME);
        File layoutsFile = new File(BATH_PATH + DIR + LAYOUTS_NAME);
        File rentTemplateDir = new File(BATH_PATH + DIR + RENT_TEMPLATE_DIR);
        SAXReader reader = new SAXReader();
        Map<Class<ProductEntity>, Map<String, ProductEntity>> result = new HashMap<>();
        Map<String, ProductEntity> retailProductPrices;

        //因为每个商品都需要读取对应的文件来拿到映射的名字,所以采用多线程,如果提前就能将所有物品解析好
        //就不存在这个问题了
        ForkJoinPool forkjoinPool = new ForkJoinPool();
        try {
            long start = System.currentTimeMillis();
            //解析租赁模板
            Map<String, ShopRentalTemplate> rentalMap = new HashMap<>();
            File[] files = rentTemplateDir.listFiles();
            for (int i = 0; i < files.length; i++) {
                File templateFile = files[i];
                if (templateFile.isFile()){
                    ShopRentalTemplate rental = new ShopRentalTemplate();
                    Element ele = (Element)reader.read(templateFile).selectSingleNode("//ShopRentalTemplate");
                    PropertiesReadUtil.readSameNameProperties(ele,rental);
                    rentalMap.put(rental.getID(),rental);
                }
            }

            //解析RetailProductPrices.xml(存放所有商品的文件)
            Element retailEle = reader.read(retailFile).getRootElement();
            List<Node> nodes = retailEle.selectNodes("//Node");

            ForkJoinTask<Map<String, ProductEntity>> task =
                    forkjoinPool.submit(new RetailProductPricesTask(0, 500, nodes));
            retailProductPrices = task.get();
            long end1 = System.currentTimeMillis();
            log.info("扫描商品耗时:"+(end1-start));
            //解析ShopLayouts.xml
            Element layoutsEle = reader.read(layoutsFile).getRootElement();
            List<Node> inventorysNodes = layoutsEle.selectNodes("//ShopInventoryNode");
            ForkJoinTask<Map<String, ProductEntity>> shopLayoutTask =
                    forkjoinPool.submit(
                            new ShopLayoutTask(0, 1000, inventorysNodes, retailProductPrices, rentalMap));
            result.put(ProductEntity.class,shopLayoutTask.get());
            long end2 = System.currentTimeMillis();
            log.info("扫描商店耗时:"+(end2-end1));
        } catch (Exception e) {
            log.error("扫描商品出现异常,异常详情:{}", ExceptionUtils.getStackTrace(e));
        }
        return result;
    }

    private void readInventoryNode(Element element, ProductEntity productEntity, Map<String, ShopRentalTemplate> rentalMap) {
        PropertiesReadUtil.readSameNameProperties("shop",element,productEntity);
        Element shopLayoutEle = findLayoutNode(element);
        productEntity.setLayoutName(shopLayoutEle.attributeValue("Name"));
        productEntity.setLayoutId(shopLayoutEle.attributeValue("ID"));
        List<Node> transactionTypes = element.selectNodes("./TransactionTypes/TransactionType");
        transactionTypes.forEach( node -> {
            if(node.getText().equalsIgnoreCase("Buy")) productEntity.setCanBuy(1);
            if(node.getText().equalsIgnoreCase("Sell")) productEntity.setCanSell(1);
            if(node.getText().equalsIgnoreCase("Rent")) productEntity.setCanRent(1);
        });
        //读取赁模板
        List<Node> nodes = element.selectNodes("./RentalTemplates/ID");
        if (ObjectUtils.isNotEmpty(nodes)){
            List<ShopRentalTemplate> rentals = new LinkedList<>();
            productEntity.setRentals(rentals);
            nodes.forEach(node -> {
                ShopRentalTemplate rental = rentalMap.get(node.getStringValue());
                if (rental != null){
                    rentals.add(rental);
                }
            });
        }
    }

    private Element findLayoutNode(Element element) {
        Element parent = element.getParent();
        while (!parent.getName().equals("ShopLayoutNode")){
            parent = parent.getParent();
        }
        return parent;
    }

    public class RetailProductPricesTask extends RecursiveTask<Map<String, ProductEntity>>{

        private int start;
        private int limit;
        private List<Node> nodes;

        public RetailProductPricesTask(int start, int limit, List<Node> nodes) {
            this.start = start;
            this.limit = limit;
            this.nodes = nodes;
        }

        @Override
        protected Map<String, ProductEntity> compute() {
            Map<String, ProductEntity> map = new ConcurrentHashMap<>();
            RetailProductPricesTask task = null;
            int end = nodes.size()-1;
            if (start + limit - 1 < nodes.size()){
                task = new RetailProductPricesTask(start + limit, limit , nodes);
                task.fork();
                end = start + limit - 1;
            }
            log.info("正在多线程解析商品文件,此线程正在解析{} ~ {},总共:{}",start+1,end+1,nodes.size());
            for (int i = start; i < end + 1; i++) {
                try {
                    SAXReader reader = new SAXReader();
                    Element element =(Element)nodes.get(i);
                    ProductEntity product = new ProductEntity();
                    PropertiesReadUtil.readSameNameProperties(element,product);
                    //读取商品本身的信息,获取内容
                    String productInfoFilePath = product.getFilename();
                    productInfoFilePath = productInfoFilePath.replaceAll("\\\\","/");
                    int prefixIndex = productInfoFilePath.indexOf("Data/Libs/");
                    if (prefixIndex>-1){
                        productInfoFilePath = productInfoFilePath.substring(prefixIndex);
                    }
                    if (productInfoFilePath.indexOf("Data/Libs/Foundry/Records/SCItem")>-1){
                        productInfoFilePath = productInfoFilePath.replace("Data/Libs/Foundry/Records/SCItem","Data/Libs/Foundry/Records/entities/SCItem");
                    }
                    productInfoFilePath = BATH_PATH.substring(0,BATH_PATH.lastIndexOf("/")+1)+productInfoFilePath;
                    File productFile = new File(productInfoFilePath);
                    if (productFile.exists()){
                        //商品对应的文件解析
                        parseProductFile(reader, product, productFile);
                    }else{
                        //居然有些文件名写的不对,靠了,在最后一层目录遍历所有文件,通过ref来判断是不是对应文件
                        String subDirStr = productInfoFilePath.substring(0, productInfoFilePath.lastIndexOf("/"));
                        //处理错误路径

                        lock.lock();
                        try{
                            Map<String, String> idFileMap = pathIdFileCache.get(subDirStr);
                            if (idFileMap != null
                                    && new File(subDirStr).listFiles() != null
                                    && idFileMap.size() == new File(subDirStr).listFiles().length
                                    && idFileMap.get(product.getID()) == null){
                                log.debug("product:{} not exists", product.getName());
                                continue;
                            }
                            if (idFileMap == null){
                                idFileMap = new ConcurrentHashMap<>();
                                pathIdFileCache.put(subDirStr,idFileMap);
                            }
                            String path = idFileMap.get(product.getID());
                            if (path == null){
                                //居然有些文件名写的不对,靠了,在最后一层目录遍历所有文件,通过ref来判断是不是对应文件
                                File subDir = new File(subDirStr);
                                if (subDir.isDirectory()) {
                                    File[] files = subDir.listFiles();
                                    log.debug("product:{} will find in {} files", product.getName(), files.length);
                                    for (int j = 0; j < files.length; j++) {
                                        if (files[j].isFile()) {
                                            Document productDoc = reader.read(files[j]);
                                            Element rootElement = productDoc.getRootElement();
                                            String ref = rootElement.attributeValue("__ref");
                                            if (ref.equals(product.getID())) {
                                                log.debug("product:{} find it's own file", product.getName());
                                                parseProductFile(reader, product, files[j]);
                                            } else {
                                                idFileMap.put(ref, files[j].getAbsolutePath());
                                            }
                                        }else{
                                            //站位,为了让获取到的文件数量相等
                                            idFileMap.put(""+j,"");
                                        }
                                    }
                                }
                            }else{
                                //缓存里有文件
                                log.debug("product:{} find in cache",product.getName());
                                parseProductFile(reader, product, new File(path));
                            }
                        }catch(Exception e){
                            log.error("error when find product file,source:{},path:{},ex:\r\n{}",productInfoFilePath,subDirStr,ExceptionUtils.getStackTrace(e));
                            continue;
                        }finally {
                            lock.unlock();
                        }
                    }
                    map.put(product.getID(),product);
                } catch (Exception e) {
                    log.error(ExceptionUtils.getStackTrace(e));
                }
            }
            if (task != null){
                Map<String, ProductEntity> childResult = task.join();
                map.putAll(childResult);
            }

            return map;
        }

        private void parseProductFile(SAXReader reader, ProductEntity product, File productFile) throws DocumentException {
            Document productDoc = reader.read(productFile);
            Element rootElement = productDoc.getRootElement();
            //物品交易属性
            Node itemPurchasableNode = rootElement.selectSingleNode("//SCItemPurchasableParams");
            Node attachNode = rootElement.selectSingleNode("//SAttachableComponentParams");
            //交易属性
            if (itemPurchasableNode != null){
                //该组件的引用
                product.setRef(rootElement.attributeValue("__ref"));
                product.setFileName(product.getFilename());
                ItemPurchasableParams purchasable = itemPurchasableReader.read(itemPurchasableNode);
                product.setName(purchasable.getDisplayNameLocalKey());
                product.setType(purchasable.getDisplayTypeLocalKey());
            }
            //size grade description等
            if (attachNode != null){
                AttachableComponentParams attachParams = attachableComponentReader.read(attachNode);
                //由于有个别商品存在名称和类别都是空的情况,名称改为去attach里的名称
                String placeHolder = "@LOC_PLACEHOLDER";
                if (product.getName().equalsIgnoreCase(placeHolder)){
                    product.setName(attachParams.getLocalizationParams().getName());
                }
                product.setSize(attachParams.getSize());
                product.setGrade(attachParams.getGrade());
                product.setProductType(attachParams.getType());
                product.setProductSubType(attachParams.getSubType());
                product.setDescription(attachParams.getLocalizationParams().getDescription());
            }
        }
    }

    public class ShopLayoutTask extends RecursiveTask<Map<String, ProductEntity>>{

        private int start;
        private int limit;
        private List<Node> nodes;
        private Map<String, ProductEntity> retailProductPrices;
        private Map<String, ShopRentalTemplate> rentalMap;

        public ShopLayoutTask(int start, int limit, List<Node> nodes
                , Map<String, ProductEntity> retailProductPrices, Map<String, ShopRentalTemplate> rentalMap) {
            this.start = start;
            this.limit = limit;
            this.nodes = nodes;
            this.retailProductPrices = retailProductPrices;
            this.rentalMap = rentalMap;
        }

        @Override
        protected Map<String, ProductEntity> compute() {
            ShopLayoutTask task = null;
            int end = nodes.size()-1;
            if (start + limit - 1 < nodes.size()){
                task = new ShopLayoutTask(start + limit, limit , nodes, retailProductPrices, rentalMap);
                task.fork();
                end = start + limit - 1;
            }
            Map<String, ProductEntity> layoutsMap = new HashMap<>(nodes.size());
            log.info("正在多线程解析商店文件,此线程正在解析{} ~ {},总共:{}",start+1,end+1,nodes.size());
            for (int i = start; i < end + 1; i++) {
                try {
                    Element element =(Element)nodes.get(i);
                    ListIterator<Node> iterator = nodes.listIterator();
                    //这个id就是RETAIL_PRODUCT_PRICES中的id
                    String inventoryID = element.attributeValue("InventoryID");
                    //每个商品必须新建一个对象,不能重复使用
                    ProductEntity productEntity;
                    ProductEntity sourceProductEntity = retailProductPrices.get(inventoryID);
                    if (sourceProductEntity != null){
                        productEntity = ProductMapper.INSTANCE.entityCopy(sourceProductEntity);
                    }else{
                        productEntity = new ProductEntity();
                        productEntity.setID(inventoryID);
                        log.info(inventoryID);
                    }
                    readInventoryNode(element,productEntity,rentalMap);
                    layoutsMap.put(productEntity.getShopID(),productEntity);
                } catch (Exception e) {
                    log.error(ExceptionUtils.getStackTrace(e));
                }
            }
            if (task != null){
                Map<String, ProductEntity> childResult = task.join();
                layoutsMap.putAll(childResult);
            }

            return layoutsMap;
        }
    }
}
