package tools.starcitizen.converter.product;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import tools.starcitizen.config.SystemLocationConfig;
import tools.starcitizen.context.LocalizationContext;
import tools.starcitizen.context.SystemContext;
import tools.starcitizen.converter.Converter;
import tools.starcitizen.entity.mapstruct.ProductMapper;
import tools.starcitizen.entity.product.ProductEntity;
import tools.starcitizen.entity.product.rent.ShopRentalTemplate;
import tools.starcitizen.entity.system.SystemEntity;
import tools.starcitizen.entity.vo.product.ProductVO;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: wftank
 * @Date: 2020/10/7
 * @Description:
 */
@Slf4j
public class ProductConverter implements Converter<ProductVO> {

    // 英文
    private static Pattern pattren = Pattern.compile(".*[a-zA-Z]+.*");
    private Properties unknownLocationsProp;
    private static final String UNKNOWN_PROPERTIES_PATH = "./unknown_shop.txt";
    private static final String EXCLUDE_SHOPNAME_PATTERNS_PATH = "./exclude_shopname_patterns.txt";
    private static final String EXCLUDE_LOCATION_LEVEL_PATTERNS_PATH = "./exclude_location_level_patterns.txt";
    private static final String SHOPNAME_MAPPING_PATH = "./shopname_mapping.txt";
    private static final String UNKNOWN_PLACEHOLDER = "&UNKNOWN_PLACEHOLDER";
    public static final Set<String> unknownShops = new HashSet<>();

    //未知位置的商店名称映射
    //地名拼接
    Pattern ruleShopNamePattern = Pattern.compile("stanton_\\d{1}[a-zA-Z]{0,1}_[A-Za-z0-9]+_[A-Za-z0-9]+");
    private Pattern stantonPattern = Pattern.compile("_(Stanton\\d{1}[a-zA-Z]{0,1}_){1}");
    private Pattern stantonLnPattern = Pattern.compile("_(Stanton\\d{1}[a-zA-Z]{0,1}_L\\d{1}){1}");
    private Pattern stantonLEOnPattern = Pattern.compile("_(Stanton\\d{1}[a-zA-Z]{0,1}_LEO\\d{1}){1}");

    private List<String> excludeShopNamePattern = new ArrayList<>();
    private List<Pattern> excludeLocaltionLevelPattern = new ArrayList<>();
    private Map<Pattern,String> shopnameMapping = new HashMap<>();
    //将商店名称中的指定字符串映射为global.ini中的字符串
    private Map<String,String> shopNameReplaceMap = new HashMap(){
        {
            put("_hrst_","_HurDynMining_");
            put("_indyFarm_","_IndyFarmer_");
            put("_drugfarm_","_DrugLab_");
            put("stanton3b_arccorp_area045","Stanton3b_ArcCorp_001");
            put("stanton3b_arccorp_area048","Stanton3b_ArcCorp_002");
            put("stanton3b_arccorp_area056","Stanton3b_ArcCorp_003");
            put("stanton3b_arccorp_area061","Stanton3b_ArcCorp_004");
            put("stanton3a_shubin_sal2","Stanton3a_Shubin_001");
            put("stanton3a_shubin_sal5","Stanton3a_Shubin_002");
            put("stanton3a_indy_humboldt","Stanton3a_IndyMine_001");
        }
    };
    //未知商店名映射
    private Map<String,String> unknownShopNameReplaceMap = new HashMap(){
        {
            put("StashHouse_Stanton3a_Orphanage","Stanton3a_Stash_001");
            put("StashHouse_Stanton2a_PrivateProperty","Stanton2a_Stash_001");
            put("StashHouse_Stanton2c_NT999XX","Stanton2c_Stash_001");
            put("StashHouse_Stanton2b_NuenWaste","Stanton2b_Stash_001");
            put("arccorp_cluster_001_frost","Stanton2c_ArcCorp_001");
            put("indy_miner_001_dust","Stanton2a_IndyMine_001");
            put("indy_farmer_001_dust","Stanton2a_IndyFarmer_001");
            put("indy_farmer_001_sand","Stanton2b_IndyFarmer_001");
            put("indy_miner_001_frost","Stanton2c_IndyMine_001");
            put("indy_miner_001_sand","Stanton2b_IndyMine_001");
            put("shubin_cluster_001_sand","Stanton2b_Shubin_001");
            put("arccorp_cluster_001_sand","Stanton2b_ArcCorp_001");
            put("DrugLab_Stanton3a_ParadiseCove","Stanton3a_DrugLab_001");
            put("drug_farm_001_frost","Stanton2c_DrugLab_001");
            put("rayari_cluster_001_dust","Stanton2a_Rayari_001");
            put("rayari_cluster_001_frost","Stanton2c_Rayari_001");
            put("t_mills_cluster_001_dust","Stanton2a_TerraMills_001");
            put("StashHouse_Stanton4","Stanton4_Stash_001");
            put("Klescher_Aberdeen","Stanton1b_Aberdeen_Prison");
        }
    };

    public ProductConverter() {
        init();
    }

    public void init(){
        refreshUnknownLocationsProp(UNKNOWN_PROPERTIES_PATH);
        loadExcludeShopNamepatterns(EXCLUDE_SHOPNAME_PATTERNS_PATH);
        loadExcludeLocationLevelpatterns(EXCLUDE_LOCATION_LEVEL_PATTERNS_PATH);
        loadShopnameMappingConfig(SHOPNAME_MAPPING_PATH);
    }

    private void loadExcludeLocationLevelpatterns(String path) {
        File file = new File(path);
        if (file.exists() && file.isFile()){
            try (
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            ){
                String pattern = null;
                while ((pattern = br.readLine()) != null) {
                    if (StringUtils.isNotBlank(pattern.trim())){
                        excludeLocaltionLevelPattern.add(Pattern.compile(pattern,Pattern.CASE_INSENSITIVE));
                    }
                }
            }catch (Exception e){
                log.error("加载{}出错,错误详情:{}",file.getAbsolutePath(),ExceptionUtils.getStackTrace(e));
            }
        }else{
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadExcludeShopNamepatterns(String path) {
        File file = new File(path);
        if (file.exists() && file.isFile()){
            try (
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
                    ){
                String pattern = null;
                while ((pattern = br.readLine()) != null) {
                    if (StringUtils.isNotBlank(pattern.trim())){
                        excludeShopNamePattern.add(pattern);
                    }
                }
            }catch (Exception e){
                log.error("加载{}文件出错,错误详情:{}",file.getAbsolutePath(),ExceptionUtils.getStackTrace(e));
            }
        }else{
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void loadShopnameMappingConfig(String path) {
        File file = new File(path);
        if (file.exists() && file.isFile()){
            try (
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            ){
                String row = null;
                while ((row = br.readLine()) != null) {
                    if (StringUtils.isNotBlank(row.trim())){
                        int index = row.indexOf("=");
                        String key = row.substring(0,index);
                        String value = row.substring(index+1);
                        shopnameMapping.put(Pattern.compile(key,Pattern.CASE_INSENSITIVE),value);
                    }
                }
            }catch (Exception e){
                log.error("加载{}文件出错,错误详情:{}",file.getAbsolutePath(),ExceptionUtils.getStackTrace(e));
            }
        }else{
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void refreshUnknownLocationsProp(String path){
        //加载未知商店的配置文件
        File file = new File(path);
        unknownLocationsProp = new Properties();
        if (file.exists()){
            try (InputStream is  = new FileInputStream(file)){
                unknownLocationsProp.load(is);
            }catch (Exception e){
                log.error("加载未知商店位置出现异常,异常信息:{}", ExceptionUtils.getStackTrace(e));
            }

        }else{
            //如果文件不存在,创建一个新文件
            //保证要有文件,方便后续写入
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static Set<String> typeSet = new ConcurrentSkipListSet<>();
    @Override
    public ProductVO convert(Object source) {
        ProductEntity entity = (ProductEntity) source;
        ProductVO vo = ProductMapper.INSTANCE.entityToVO(entity);
        String nameKey = vo.getName();
//        String typeKey = vo.getType();
        String typeKey = "@item_Type"+vo.getProductType();
        String descriptionKey = vo.getDescription();
        String fileName = vo.getFileName();
        //名称
        if (StringUtils.isNotBlank(nameKey) && nameKey.startsWith("@")){
            nameKey = nameKey.substring(1);
            vo.setName(LocalizationContext.get(nameKey));
            vo.setNameCn(LocalizationContext.getCn(nameKey));
        }
        //类型

        if (StringUtils.isNotBlank(typeKey) && typeKey.startsWith("@")){
            typeKey = typeKey.substring(1);
            //个别商品存在错误,将类型映射成描述,这种情况使用productType
            vo.setType(LocalizationContext.get(typeKey));
            vo.setTypeCn(LocalizationContext.getCn(typeKey));
            if (!typeSet.contains(typeKey)){
                log.info("productType: "+ vo.getProductType() +" cn: "+ vo.getTypeCn());
            }
            typeSet.add(typeKey);
//            if (typeKey.contains("Desc")){
//                //有个别商品类型里引用用的描述,这里特殊处理下
//                String[] split = vo.getType().split("\\\\n");
//                if (split.length > 1){
//                    String[] typeStr = split[1].split(":");
//                    vo.setType(typeStr[1].trim());
//                    String[] splitCn = vo.getTypeCn().split("\\\\n");
//                    String[] typeStrCn = splitCn[1].split(":");
//                    //兼容中文冒号
//                    if (typeStrCn.length < 2){
//                        typeStrCn = splitCn[1].split("：");
//                    }
//                    vo.setTypeCn(typeStrCn[1].trim());
//                }
//            }
        }
        //处理描述
        formatDescription(vo, descriptionKey);
        //判断是否为贸易品
        deduceCommodities(vo);
        //计算价格
        caculatePrice(vo);
        //计算每分钟刷新量
        caculateRefreshPerMinute(vo);
        //计算飞船租用价格
        caculateRentPrice(entity,vo);
        //格式化库存相关信息
        formatRentPrice(vo);
        if (fileName != null){
            //特殊处理飞船和地面载具
            if (fileName.startsWith("Data\\Libs\\Foundry\\Records\\Entities\\Spaceships")){
                //船的类型做单独处理
                vo.setType("ship");
                vo.setTypeCn("飞船");
            }else if (fileName.startsWith("Data\\Libs\\Foundry\\Records\\Entities\\GroundVehicles")){
                vo.setType("Ground Vehicles");
                vo.setTypeCn("地面载具");
            }
        }
        return vo;
    }
    //设置商品所属商店(位置)
    public void findAndSetLocation(ProductEntity entity, ProductVO vo,SystemEntity systemEntity) {
        StringBuilder layoutValueSb = new StringBuilder();
        StringBuilder layoutCnValueSb = new StringBuilder();
        findLocalLayoutName(entity, layoutValueSb, layoutCnValueSb, systemEntity);

        String localtionName = formatLocationName(layoutValueSb.toString());
        localtionName = localtionName.endsWith("-") ?
                localtionName.substring(0,localtionName.length()-1) : localtionName;
        vo.setLayoutName(localtionName);

        String localtionNameCn = formatLocationName(layoutCnValueSb.toString());
        localtionNameCn = localtionNameCn.endsWith("-") ?
                localtionNameCn.substring(0,localtionNameCn.length()-1) : localtionNameCn;
        vo.setLayoutNameCn(localtionNameCn);
    }

    /**
     * 处理描述
     * @author jw
     * @date 2020/10/24
     * @param vo
     * @param descriptionKey
     * @return void
     */
    private void formatDescription(ProductVO vo, String descriptionKey) {
        if (StringUtils.isNotBlank(descriptionKey) && descriptionKey.startsWith("@")){
            descriptionKey = descriptionKey.substring(1);
            String description = LocalizationContext.get(descriptionKey);
            String descriptionCn = LocalizationContext.getCn(descriptionKey);
            description =  description.replaceAll("(\\\\n){2,}","\\\\n");
            descriptionCn =  descriptionCn.replaceAll("(\\\\n){2,}","\\\\n");

            //把多个连着的/n/n换为一个
            String classKey = "\\nClass";
            String classKeyCn = "\\n类别";
            String classTypeKeyCn = "\\n分类";
            String classTypeKeyCn2 = "\\n等级";
            String classTypeKeyCn3 = "\\n级别";
            //class
            if (description.contains(classKey)){
                int startIndex = description.indexOf(classKey) + classKey.length() +1;
                int endIndex = description.indexOf("\\n", startIndex);
                vo.setClassDes(description.substring(startIndex,endIndex));
            }
            //类别
            if (descriptionCn.contains(classKeyCn)){
                int startIndex = descriptionCn.indexOf(classKeyCn) + classKeyCn.length()  +1;
                int endIndex = descriptionCn.indexOf("\\n", startIndex);
                String classDescValue = descriptionCn.substring(startIndex, endIndex);
                if (!pattren.matcher(classDescValue).matches()){
                    vo.setClassDesCn(classDescValue);
                }
            }
            //分类
            if (descriptionCn.contains(classTypeKeyCn)){
                int startIndex = descriptionCn.indexOf(classTypeKeyCn)+ classTypeKeyCn.length()  +1 ;
                int endIndex = descriptionCn.indexOf("\\n", startIndex);
                String classDescValue = descriptionCn.substring(startIndex, endIndex);
                if (!pattren.matcher(classDescValue).matches()){
                    vo.setClassDesCn(classDescValue);
                }
            }
            //等级
            if (descriptionCn.contains(classTypeKeyCn2)){
                int startIndex = descriptionCn.indexOf(classTypeKeyCn2) + classTypeKeyCn2.length() +1;
                int endIndex = descriptionCn.indexOf("\\n", startIndex);
                String classDescValue = descriptionCn.substring(startIndex, endIndex);
                //如果包含英文,证明不是对的列
                if (!pattren.matcher(classDescValue).matches()){
                    vo.setClassDesCn(classDescValue);
                }
            }
            //级别
            if (descriptionCn.contains(classTypeKeyCn3)){
                int startIndex = descriptionCn.indexOf(classTypeKeyCn3) + classTypeKeyCn3.length() +1;
                int endIndex = descriptionCn.indexOf("\\n", startIndex);
                String classDescValue = descriptionCn.substring(startIndex, endIndex);
                //如果包含英文,证明不是对的列
                if (!pattren.matcher(classDescValue).matches()){
                    vo.setClassDesCn(classDescValue);
                }
            }
            //处理描述
            vo.setDescription(description.replaceAll("\\\\n", System.lineSeparator()));
            vo.setDescriptionCn(descriptionCn.replaceAll("\\\\n", System.lineSeparator()));
        }
    }

    private void formatRentPrice(ProductVO vo) {
        Double shopInventory = vo.getShopInventory();
        Double shopMaxInventory = vo.getShopMaxInventory();
        Double refreshPerMinute = vo.getRefreshPerMinute();
        if (shopInventory != null){
            BigDecimal shopInventorySCU = new BigDecimal(shopInventory.toString()).divide(new BigDecimal(100))
                    .setScale(0, BigDecimal.ROUND_HALF_UP);
            vo.setShopInventorySCU(shopInventorySCU.doubleValue());
        }
        if (shopMaxInventory != null){
            BigDecimal shopMaxInventorySCU = new BigDecimal(shopMaxInventory.toString()).divide(new BigDecimal(100))
                    .setScale(0, BigDecimal.ROUND_HALF_UP);
            vo.setShopMaxInventorySCU(shopMaxInventorySCU.doubleValue());
        }
        if (refreshPerMinute != null){
            BigDecimal refreshPerMinuteSCU = new BigDecimal(refreshPerMinute.toString()).divide(new BigDecimal(100))
                    .setScale(0, BigDecimal.ROUND_HALF_UP);
            vo.setRefreshPerMinuteSCU(refreshPerMinuteSCU.doubleValue());
        }
    }

    private void caculateRentPrice(ProductEntity entity, ProductVO vo) {
        List<ShopRentalTemplate> rentals = entity.getRentals();
        if (ObjectUtils.isNotEmpty(rentals)){
            for (ShopRentalTemplate rental : rentals) {
                BigDecimal multiplier = new BigDecimal(rental.getPercentageOfSalePrice().toString())
                        .divide(new BigDecimal(100));
                BigDecimal rentPrice = new BigDecimal(vo.getCurrentPrice().toString()).multiply(multiplier)
                        .setScale(0, BigDecimal.ROUND_HALF_UP);
                switch (rental.getRentalDuration()){
                    case 1:
                        vo.setRentPrice1(rentPrice.doubleValue());
                        break;
                    case 3:
                        vo.setRentPrice3PerDay(rentPrice.doubleValue());
                        vo.setRentPrice3(rentPrice.multiply(new BigDecimal(3)).doubleValue());
                        break;
                    case 7:
                        vo.setRentPrice7PerDay(rentPrice.doubleValue());
                        vo.setRentPrice7(rentPrice.multiply(new BigDecimal(7)).doubleValue());
                        break;
                    case 30:
                        vo.setRentPrice30PerDay(rentPrice.doubleValue());
                        vo.setRentPrice30(rentPrice.multiply(new BigDecimal(30)).doubleValue());
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void caculateRefreshPerMinute(ProductVO vo) {
        BigDecimal refreshPerMinite = new BigDecimal(vo.getShopMaxInventory().toString())
                .multiply(new BigDecimal(vo.getShopRefreshRatePercentagePerMinute().toString()))
                .divide(new BigDecimal(100))
                .setScale(0, BigDecimal.ROUND_HALF_UP);
        vo.setRefreshPerMinute(refreshPerMinite.doubleValue());
    }

    private void deduceCommodities(ProductVO vo) {
        vo.setCommodity(0);
        if (vo.getFileName() != null && vo.getFileName().startsWith("Data\\Libs\\Foundry\\Records\\Entities\\Commodities")){
            //是贸易品
            vo.setCommodity(1);
        }
    }

    private void caculatePrice(ProductVO vo) {
        //计算实际价格
        Double basePrice = vo.getBasePrice();
        if (basePrice != null){
            Double percentage = vo.getShopBasePriceOffsetPercentage();
            BigDecimal percentageBig = new BigDecimal(percentage.toString()).add(new BigDecimal(100));

            BigDecimal factor = percentageBig.divide(new BigDecimal(100));
            //如果不是贸易品,计算当前价格
            //计算价格
            BigDecimal currentPrice = new BigDecimal(basePrice.toString()).multiply(factor)
                    .setScale(0, BigDecimal.ROUND_HALF_UP);
            //如果是贸易品,根据折扣比和溢价比计算对应的价格
            if (vo.getCommodity().equals(1)){

                //在该路径下证明是交易物品,计算时保留两位小数
                currentPrice = new BigDecimal(basePrice.toString()).multiply(factor)
                        .setScale(2, BigDecimal.ROUND_HALF_UP);
                if (vo.getCanBuy().equals(1)){
                    BigDecimal bFactor = new BigDecimal(100)
                            .subtract(new BigDecimal(vo.getShopMaxDiscountPercentage().toString()));
                    bFactor = bFactor.divide(new BigDecimal(100));
                    BigDecimal minBuyingPrice = bFactor.multiply(currentPrice).setScale(2, BigDecimal.ROUND_HALF_UP);
                    vo.setMinBuyingPrice(minBuyingPrice.doubleValue());
                }
                if (vo.getCanSell().equals(1)){

                    BigDecimal sFactor = new BigDecimal(100)
                            .add(new BigDecimal(vo.getShopMaxPremiumPercentage().toString()));
                    sFactor = sFactor.divide(new BigDecimal(100));
                    BigDecimal maxSellingPrice = sFactor.multiply(currentPrice).setScale(2, BigDecimal.ROUND_HALF_UP);
                    vo.setMaxSellingPrice(maxSellingPrice.doubleValue());
                }
            }
            vo.setCurrentPrice(currentPrice.doubleValue());
        }

    }

    /**
     * 根据商店的id找到layer中对应的系统id进而找出mapping位置
     * @author jw
     * @date 2020/10/24
     * @param entity
     * @param sb
     * @param cnSb
     * @return void
     */
    private Pattern stantonMoonPattern = Pattern.compile("^Stanton\\d{1}[a-zA-Z]{1}$");
    private synchronized void findLocalLayoutName(ProductEntity entity, StringBuilder sb, StringBuilder cnSb,SystemEntity systemEntity) {
        //如果layout中存在该商店位置,则直接使用
        if (systemEntity != null){
            String[] ids = systemEntity.getID().split(",");
            //从第一层遍历到第四层作为路径开头
            int length = ids.length > 3 ? 4 : ids.length;
            for (int i = 0; i < length; i++) {
                String levelId = "";
                for (int j = 0; j <= i ; j++) {
                    levelId += ","+ids[j];
                }
                levelId = levelId.substring(1);
                SystemEntity levelEntity = SystemContext.SYSTEM_MAP.get(levelId);
                String levelKey = SystemContext.appendKey(levelEntity).substring(1);

                //英语
                String slEnValue = SystemLocationConfig.EN.getProperty(levelKey);
                String value = StringUtils.isNotBlank(slEnValue) ?
                        getLocalValue(slEnValue) : levelEntity.getName();
                //中文
                String slCnValue = SystemLocationConfig.CN.getProperty(levelKey);
                String cnValue = StringUtils.isNotBlank(slCnValue) ?
                        getLocalCnValue(slCnValue) : levelEntity.getName();
                //根据名称过滤
                if (!fiterLevel(value,cnValue)){
                    sb.append(value).append("-");
                    cnSb.append(cnValue).append("-");
                }
                //这里做特殊处理,如果到了第三层,不是moon的话,则不再进行第四次梳理(主星第四层已经不知道是什么了)

                //德拉玛特殊处理一下,因为你前面多了一个Aaron Halo
                if (i == 2 && slEnValue != null){
                    if (slEnValue.startsWith("@")){
                        if (!stantonMoonPattern.matcher(slEnValue.substring(1)).matches()
                                && !slEnValue.contains("Delamar")){
                            break;
                        }
                    }
                }
            }
            String formatShopName = formatShopName(entity.getLayoutName());
            //将类似stanton_4a_shubin_004 拼接成 stanton4a_shubin_004
            Matcher ruleName = ruleShopNamePattern.matcher(formatShopName);
            String cnFormatShopName = formatShopName;
            if (ruleName.find()){
                /**
                 * 如果字符串中包含stanton_xx_xxx_xxx
                 * 则拼接成stantonxx_xxx_xxx(把第一个下划线去掉)
                 */
                String key = ruleName.group(0).replaceFirst("_", "");
                if (StringUtils.isBlank(LocalizationContext.get(key))){
                    Iterator<Map.Entry<String, String>> iterator =
                            shopNameReplaceMap.entrySet().iterator();
                    while (iterator.hasNext()){
                        Map.Entry<String, String> next = iterator.next();
                        key = key.replaceAll(next.getKey(),next.getValue());
                    }
                }
                formatShopName = StringUtils.isNotBlank(LocalizationContext.get(key)) ?
                        LocalizationContext.get(key) : formatShopName;
                cnFormatShopName = StringUtils.isNotBlank(LocalizationContext.getCn(key)) ?
                        LocalizationContext.getCn(key) : formatShopName;
                if (StringUtils.isBlank(LocalizationContext.get(key))){
                    log.info("属于规则商店名stanton_xx_xx_xx但没有找到映射,key:"+key);
                }
            }else if (StringUtils.isNotBlank(LocalizationContext.get("item_NameShop_"+formatShopName))){
                //看看是否为游戏内的类似cassaba的商店名
                String key = formatShopName;
                String nameShopKey = "item_NameShop_"+ key;
                formatShopName = StringUtils.isNotBlank(LocalizationContext.get(nameShopKey)) ?
                        LocalizationContext.get(nameShopKey) : formatShopName;
                cnFormatShopName = StringUtils.isNotBlank(LocalizationContext.getCn(nameShopKey)) ?
                        LocalizationContext.getCn(nameShopKey) : formatShopName;
            }else{
                //如果还找不到,从未知商店map中加载
                String key = unknownShopNameReplaceMap.get(formatShopName);
                if (key == null){
                    unknownShops.add(formatShopName);
                }else{
                    formatShopName = StringUtils.isNotBlank(LocalizationContext.get(key)) ?
                            LocalizationContext.get(key) : formatShopName;
                    cnFormatShopName = StringUtils.isNotBlank(LocalizationContext.getCn(key)) ?
                            LocalizationContext.getCn(key) : formatShopName;
                }
            }
            //最后读取配置文件,让配置文件优先级最高
            if (!shopnameMapping.isEmpty()){
                Iterator<Map.Entry<Pattern, String>> shopmameMappingIterator
                        = shopnameMapping.entrySet().iterator();
                while (shopmameMappingIterator.hasNext()){
                    Map.Entry<Pattern, String> entry = shopmameMappingIterator.next();
                    Matcher matcher = entry.getKey().matcher(entity.getLayoutName());
                    if  (matcher.matches()){
                        String mappingStr = entry.getValue();
                        formatShopName = "";
                        cnFormatShopName = "";
                        String[] split = mappingStr.split("-");
                        for (int i = 0; i < split.length; i++) {
                            if (split[i].startsWith("@")){
                                formatShopName+=LocalizationContext.get(split[i])+"-";
                                cnFormatShopName+=LocalizationContext.getCn(split[i])+"-";
                            }else{
                                String[] split1 = split[i].split("%%");
                                cnFormatShopName+=split1[0]+"-";
                                formatShopName+=split1[1]+"-";
                            }
                        }
                        formatShopName = formatShopName.substring(0,formatShopName.length()-1);
                        cnFormatShopName = cnFormatShopName.substring(0,cnFormatShopName.length()-1);
                    }

                }
            }
            //将商店名拼接到位置的末尾
            sb.append(formatShopName);
            cnSb.append(cnFormatShopName);
        }else{
            /**
             * 如果layer中不存在,则从配置文件中加载
             * 如果还没有,就写入配置文件
             */
           String mappingValue = unknownLocationsProp.getProperty(entity.getLayoutName());
           if (StringUtils.isNotBlank(mappingValue) && !UNKNOWN_PLACEHOLDER.equals(mappingValue)){
               //如果配置文件中有,则加载配置文件中的信息
               String[] splitMapping = mappingValue.split("-");
               for (int i = 0; i < splitMapping.length; i++) {
                   if (splitMapping[i].startsWith("@")){
                       sb.append(LocalizationContext.get(splitMapping[i].substring(1)));
                       cnSb.append(LocalizationContext.getCn(splitMapping[i].substring(1)));
                   }else{
                       sb.append(splitMapping[i]);
                       cnSb.append(splitMapping[i]);
                   }
               }
               sb.append("-");
               cnSb.append("-");
           }else{
               //特殊处理一些规律性的字符
               //比如XXXX_standonX_LX
               if (!formatRuleShopLayoutName(entity.getLayoutName(),sb, cnSb)){
                   //如果配置文件中没有,写入到配置文件中
                   //并且给往属性里放一个特殊标记字段,页面重复写入
                   File file = new File(UNKNOWN_PROPERTIES_PATH);
                   try(Writer writer =
                               new OutputStreamWriter(new FileOutputStream(file,true), StandardCharsets.UTF_8)){
                       writer.write("#new"+System.lineSeparator());
                       writer.write(entity.getLayoutName()+"="+System.lineSeparator());
                       unknownLocationsProp.setProperty(entity.getLayoutName(),UNKNOWN_PLACEHOLDER);
                   }catch (Exception e){
                       log.error("加载商店配置文件出错,错误详情:{}",ExceptionUtils.getStackTrace(e));
                   }
               }
           }
        }


    }

    private boolean fiterLevel(String value, String cnValue) {
        for (Pattern pattern : excludeLocaltionLevelPattern) {
            Matcher matcher = pattern.matcher(value);
            Matcher cnMatcher = pattern.matcher(cnValue);
            if (matcher.find() ||cnMatcher.find()){
                return true;
            }
        }
        return false;
    }
    private String formatShopName(String layoutName) {
        for (String pattern : excludeShopNamePattern) {
            Matcher matcher = Pattern.compile(pattern).matcher(layoutName);
            if (matcher.find()){
                return layoutName.replaceAll(matcher.group(),"");
            }
        }
        return layoutName;
    }

    private boolean formatRuleShopLayoutName(String layoutName, StringBuilder sb, StringBuilder cnSb) {
        Matcher stantonMacher = stantonPattern.matcher(layoutName);
        Matcher stantonLEOnMatcher = stantonLEOnPattern.matcher(layoutName);
        if (stantonMacher.find()){
            sb.append(LocalizationContext.get("StantonStar")).append("-");
            cnSb.append(LocalizationContext.getCn("StantonStar")).append("-");
            Matcher stantonLMatcher = stantonLnPattern.matcher(layoutName);
            if (stantonLMatcher.find()){
                //类似Cassaba_stanton_L3
                String starKey = stantonLMatcher.group(0).substring(1);
                sb.append(LocalizationContext.get(starKey)).append("-");
                cnSb.append(LocalizationContext.getCn(starKey)).append("-");
                int endIndex = stantonLMatcher.start(0);
                //根据商店名称查找有没有对应的nameshop,有就映射,没有就显示名字
                String shopName = layoutName.substring(0,endIndex).replaceAll("Generic_","");
                String nameShopKey = "item_NameShop_"+ shopName;
                shopName = StringUtils.isNotBlank(LocalizationContext.get(nameShopKey)) ?
                        LocalizationContext.get(nameShopKey) : shopName;
                String cnShopName = StringUtils.isNotBlank(LocalizationContext.getCn(nameShopKey)) ?
                        LocalizationContext.getCn(nameShopKey) : shopName;
                sb.append(shopName);
                cnSb.append(cnShopName);
                return true;
            }else if (stantonLEOnMatcher.find()){
                //类似Cassaba_stanton1_LEO1
                //如果是空间站
                String starKey = stantonLEOnMatcher.group(0).substring(1);
                starKey = starKey.split("_")[0]+"_Transfer";
                sb.append(LocalizationContext.get(starKey)).append("-");
                cnSb.append(LocalizationContext.getCn(starKey)).append("-");
                int endIndex = stantonLEOnMatcher.start(0);
                //根据商店名称查找有没有对应的nameshop,有就映射,没有就显示名字
                String shopName = layoutName.substring(0,endIndex).replaceAll("Generic_","");
                String nameShopKey = "item_NameShop_"+ shopName;
                shopName = StringUtils.isNotBlank(LocalizationContext.get(nameShopKey)) ?
                        LocalizationContext.get(nameShopKey) : shopName;
                String cnShopName = StringUtils.isNotBlank(LocalizationContext.getCn(nameShopKey)) ?
                        LocalizationContext.getCn(nameShopKey) : shopName;
                sb.append(shopName);
                cnSb.append(cnShopName);
                return true;
            }

        }
        return false;
    }

    private String formatLocationName(String name) {
        return name.replaceAll("OC_","")
                .replaceAll("_OC","")
                .replaceAll("Strut_","")
                .replaceAll("ObjectContainer-[0-9]{3}","")
                .replaceAll("ObjectContainer-","")
                .replaceAll("ObjectContainer_","")
                .replaceAll("_objectContainer","")
                .replaceAll("Generic_","")
                .replaceAll("_int","")
                .replaceAll("_Int","")
                .replaceAll("--","-");
    }

    private String getLocalCnValue(String slEnValue) {
        if (slEnValue.startsWith("@")){
            return LocalizationContext.getCn(slEnValue.substring(1));
        }
        return slEnValue;
    }

    private String getLocalValue(String slEnValue) {
        if (slEnValue.startsWith("@")){
            return LocalizationContext.get(slEnValue.substring(1));
        }
        return slEnValue;
    }
}
