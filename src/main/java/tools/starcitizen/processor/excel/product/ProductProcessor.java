package tools.starcitizen.processor.excel.product;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import tools.starcitizen.config.SystemLocationConfig;
import tools.starcitizen.context.SystemContext;
import tools.starcitizen.converter.product.ProductConverter;
import tools.starcitizen.entity.mapstruct.ProductMapper;
import tools.starcitizen.entity.product.ProductEntity;
import tools.starcitizen.entity.system.SystemEntity;
import tools.starcitizen.entity.vo.product.ProductVO;
import tools.starcitizen.processor.excel.AbstractExcelProcessor;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: wftank
 * @Date: 2020/10/5
 * @Description: 商品导出Excel
 */
@Slf4j
public class ProductProcessor extends AbstractExcelProcessor<ProductEntity> {

    private String defaultSheetName;
    private ProductConverter converter = new ProductConverter();
    protected static final Map<String, SystemEntity> shopMap = new ConcurrentHashMap();
    public ProductProcessor(String defaultSheetName) {
        this.defaultSheetName = defaultSheetName;
    }

    private static final Map<String,String> commonSystemMap = new HashMap<>();
    //通用的结构直接写上值
    static {
        commonSystemMap.put("ObjectContainer_Cargo","@ui_dest_cargo");
        commonSystemMap.put("ObjectContainer_Commercial","@ui_dest_commercial");
        commonSystemMap.put("ObjectContainer_Refinery","@ui_dest_refinery");
        commonSystemMap.put("ObjectContainer-Refinery","@ui_dest_refinery");
    }


    @Override
    public List<ProductVO> convertData(Map<String, ProductEntity> data) {
        //初始化商店位置的配置文件
        SystemLocationConfig.init();
        data.forEach( (ref,entity) -> {
            if (entity.getLayoutName()!=null){
                String layoutId = entity.getLayoutId();
                List<SystemEntity> systems = SystemContext.getSystemsByLayer(layoutId);
                if (systems != null){
                    systems.stream().forEach( systemEntity -> {
                        shopMap.put(systemEntity.getID(),systemEntity);
                    });
                }
            }
        });
        writeDefaultLocationConfigFile(shopMap);
        List<ProductVO> convertData = new ArrayList<>(data.size());
        Iterator<Map.Entry<String, ProductEntity>> iterator
                = data.entrySet().iterator();
        //迭代器遍历方便删除

        while (iterator.hasNext()){
            Map.Entry<String, ProductEntity> entry = iterator.next();
            ProductEntity entity = entry.getValue();
            //过滤各种不需要的实体
            if (!filterEntity(entity)){
                iterator.remove();
                continue;
            }
            ProductVO vo = converter.convert(entity);
            //由于商品文件中的一个商品不是只对应一个位置,
            //比如新出的货物甲板中,不管是弧光星还是奥丽莎的,租船终端都是同一个,非常坑爹
            String layoutId = entity.getLayoutId();
            List<SystemEntity> systems = SystemContext.getSystemsByLayer(layoutId);
            if (systems != null){
                for (SystemEntity systemEntity : systems) {
                    ProductVO setLocationVO = ProductMapper.INSTANCE.voCopy(vo);
                    converter.findAndSetLocation(entity,setLocationVO,systemEntity);
                    if (StringUtils.isNotBlank(setLocationVO.getLayoutName())){
                        convertData.add(setLocationVO);
                    }
                }
            }else{
                converter.findAndSetLocation(entity,vo,null);
                if (StringUtils.isNotBlank(vo.getLayoutName())){
                    convertData.add(vo);
                }
            }

        }
        log.info("未知商店名称:"+ProductConverter.unknownShops.toString());
        return convertData;
    }

    /**
     * 过滤无用商品
     * @author jw
     * @date 2020/10/20
     * @param entity
     * @return boolean
     */
    private boolean filterEntity(ProductEntity entity) {
        if (entity.getLayoutName().equals("AC_Inventory")) return false;
        if (entity.getLayoutName().equals("ANVL")) return false;
        if (entity.getLayoutName().equals("DRAK")) return false;
        if (entity.getLayoutName().equals("ORIG")) return false;
        if (entity.getLayoutName().equals("MISC")) return false;
        if (entity.getLayoutName().equals("AEGS")) return false;
        if (entity.getLayoutName().equals("RSI")) return false;
        if (entity.getLayoutName().equals("BestInShow")) return false;
        if (entity.getLayoutName().equals("CNOU_ARGO_GRIN")) return false;
        if (entity.getLayoutName().equals("CRUS_TMBL_KRIG")) return false;
        if (entity.getLayoutName().equals("APOA_BANU_ESPR")) return false;
        if (entity.getLayoutName().equals("CNOU_ARGO_GRIN")) return false;
        if (entity.getLayoutName().equals("Temp_Outpost_ShopInventory")) return false;
        //平台补充弹药
        if (entity.getLayoutName().equals("CryAstro_Crusader_01")) return false;
        if (entity.getLayoutName().contains("_old")) return false;
        //各种过滤逻辑
        return true;
    }

    private void writeDefaultLocationConfigFile(Map<String, SystemEntity> shopMap) {
        //如果配置文件不存在,生成一个默认的
        Set<String> set = new LinkedHashSet<>();
        SystemEntity root = SystemContext.findRoot();
        //按原文件顺序平铺ID
        resursive(root,set);
        Iterator<String> iterator = set.iterator();
        Set<String> keySet = new LinkedHashSet<>();
        while (iterator.hasNext()){
            String id = iterator.next();
            for (Map.Entry<String, SystemEntity> entry: shopMap.entrySet()) {
                SystemEntity systemEntity = entry.getValue();
                if (systemEntity.getID().contains(id)){
                    SystemEntity useEntity = SystemContext.SYSTEM_MAP.get(id);
                    keySet.add(SystemContext.appendKey(useEntity).substring(1));
                    break;
                }
            }
        }
        if (!SystemLocationConfig.enFlag) {
            File file = new File(SystemLocationConfig.EN_CONFIG_PATH);
            write2File(keySet,file);
        }
        if (!SystemLocationConfig.cnFlag) {
            File file = new File(SystemLocationConfig.CN_CONFIG_PATH);
            write2File(keySet,file);
        }
        boolean enFlag = false;
        boolean cnFlag = false;
        //todo 如果配置文件已存在,那么遍历key,从配置文件缓存中获取,如果出现不存在的情况,追加到文件中
        try(
            OutputStreamWriter enWriter =
                    new OutputStreamWriter(new FileOutputStream(
                            new File(SystemLocationConfig.EN_CONFIG_PATH),true),StandardCharsets.UTF_8);
            OutputStreamWriter cnWriter =
                    new OutputStreamWriter(new FileOutputStream(
                            new File(SystemLocationConfig.CN_CONFIG_PATH),true),StandardCharsets.UTF_8);
        ){
            Iterator<String> configKeyIterator = keySet.iterator();
            while (configKeyIterator.hasNext()){
                String key = configKeyIterator.next();
                if (SystemLocationConfig.enFlag
                        && SystemLocationConfig.EN.getProperty(key) == null){
                    if (!enFlag){
                        enWriter.write(System.lineSeparator()+"#new"+ LocalDateTime.now());
                        enFlag =true;
                    }
                    String commonKey = commonSystemNameMapping(key);
                    enWriter.write(System.lineSeparator()+key+"="+commonKey);
                }
                if (SystemLocationConfig.cnFlag
                        && SystemLocationConfig.CN.getProperty(key) == null){
                    if (!cnFlag){
                        cnWriter.write(System.lineSeparator()+"#new"+ LocalDateTime.now());
                        cnFlag = true;
                    }
                    String commonKey = commonSystemNameMapping(key);
                    cnWriter.write(System.lineSeparator()+key+"="+commonKey);
                }
            }

        }catch (Exception e){
            log.error(ExceptionUtils.getStackTrace(e));
        }

    }

    private String commonSystemNameMapping(String key) {
        String objectName = key.substring(key.lastIndexOf("]") + 1);
        return commonSystemMap.getOrDefault(objectName,"");
    }


    private void write2File(Set<String> set, File file) {

        try (Writer writer
                     = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)){
            if (ObjectUtils.isNotEmpty(set)){
                file.createNewFile();

                Iterator<String> iterator = set.iterator();
                while (iterator.hasNext()){
                    String key = iterator.next();
                    writer.write(key+"="+System.lineSeparator());
                }
            }
        } catch (IOException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }

    public void resursive(SystemEntity root,Set set) {
        //过滤掉第四层以下的,没必要用
        if (root.getLevel() > 3){
            return;
        }
        set.add(root.getID());
        List<SystemEntity> childs = root.getChilds();
        if (ObjectUtils.isNotEmpty(childs)){
            childs.forEach((entity)->{
                resursive(entity,set);
            });
        }

    }

    /**
     * 从数据中随便拿一个实体获取类型名
     * @author jw
     * @date 2020/10/5
     * @param data
     * @return java.lang.String
     */
    @Override
    public String getTypeName(Map<String, ProductEntity> data) {
        return this.defaultSheetName;
    }

}
