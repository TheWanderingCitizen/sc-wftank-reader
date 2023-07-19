package tools.starcitizen.processor.excel.product;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import tools.starcitizen.entity.mapstruct.ProductMapper;
import tools.starcitizen.entity.product.ProductEntity;
import tools.starcitizen.entity.vo.product.JsonProductVO;
import tools.starcitizen.entity.vo.product.ProductRentShopVO;
import tools.starcitizen.entity.vo.product.ProductShopVO;
import tools.starcitizen.entity.vo.product.ProductVO;
import tools.starcitizen.enums.ShowTypeEnum;
import tools.starcitizen.json.Index;
import tools.starcitizen.json.IndexEntity;
import tools.starcitizen.util.JsonUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * @Author: wftank
 * @Date: 2020/10/5
 * @Description: 商品导出Excel
 */
@Slf4j
public class JsonFileProductProcessor extends ProductProcessor {

    public static final String rootDir = "./jsonfile/";
    public static final File indexFile = new File(rootDir+"index.json");
    public static final File extIndexFile = new File(rootDir+"extention_index.json");

    public JsonFileProductProcessor(String defaultSheetName) {
        super(defaultSheetName);
    }

    @Override
    public List<ProductVO> convertData(Map<String, ProductEntity> data) {
        List<ProductVO> productVOList = super.convertData(data);
        Index index = new Index();
        index.setVersion("3.16.1");
        /**
         * 根据名称分组
         * 因为父类中是每个物品的每个位置一个对象,这里需要根据物品分组
         */
        ConcurrentMap<String, List<ProductVO>> productNameGroup =
                productVOList.parallelStream().filter((vo) -> {
                    return StringUtils.isNotBlank(vo.getName())
                            && StringUtils.isNotBlank(vo.getFileName());
                })
                        .collect(Collectors.groupingByConcurrent(ProductVO::getName));
        Map<String, List<ProductVO>> treeGroup = new TreeMap<>(productNameGroup);
        //创建索引文件
        List<IndexEntity> indexList = index.getIndex();
        //数据转对象
        treeGroup.forEach((name,productList) -> {
            ProductVO firstProductVO = productList.get(0);
            JsonProductVO jsonProductVO = convert(productList, firstProductVO);
            indexList.add(new IndexEntity(jsonProductVO.getId()
                    ,name
                    ,jsonProductVO.getNameCn()
                    ,jsonProductVO.getJsonPath()
                    ,jsonProductVO.getShowTypeKey()
                    ,jsonProductVO.getSize()
                    ,jsonProductVO.getGrade()));
        });

        File rootDir = new File(JsonFileProductProcessor.rootDir);
        try {
            if (!rootDir.exists()) {
                Files.createDirectories(rootDir.toPath());
            }
            if (!indexFile.exists()) {
                Files.createFile(indexFile.toPath());
            }
            Files.write(indexFile.toPath(), JsonUtil.toJson(index).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return productVOList;
    }

    private JsonProductVO convert(List<ProductVO> productList, ProductVO firstProductVO) {
        JsonProductVO jsonVO = ProductMapper.INSTANCE.voToJsonVO(firstProductVO);
        jsonVO.setId(firstProductVO.getID());
        ShowTypeEnum showTypeEnum = ShowTypeEnum.get(firstProductVO.getProductType());
        jsonVO.setShowTypeKey(showTypeEnum.getTypeKey());
        jsonVO.setShowType(showTypeEnum.getType());
        jsonVO.setShowTypeCn(showTypeEnum.getTypeCn());
        //过滤商品的尺寸级别,放入类型索引
        if (firstProductVO.getSize() != null){
            ShowTypeEnum.ShowMap.get(showTypeEnum.getTypeKey()).getSizeSet().add(firstProductVO.getSize());
        }
        if (firstProductVO.getGrade() != null){
            ShowTypeEnum.ShowMap.get(showTypeEnum.getTypeKey()).getGradeSet().add(firstProductVO.getGrade());
        }
        boolean isCommodity = 1 == firstProductVO.getCommodity();
        jsonVO.setCommodity(isCommodity);
        productList.stream().forEach(productVO -> {
            boolean canbuy = 1 == productVO.getCanBuy();
            boolean canSell = 1 == productVO.getCanSell();
            boolean canRent = 1 == productVO.getCanRent();
            if (canbuy){
                if(!jsonVO.getCanBuy()) jsonVO.setCanBuy(canbuy);
                ProductShopVO shopVO = ProductMapper.INSTANCE.voToShopVO(productVO);
                jsonVO.getShopBuy().add(shopVO);
            }
            if (canSell){
                if(!jsonVO.getCanSell()) jsonVO.setCanSell(canSell);
                ProductShopVO shopVO = ProductMapper.INSTANCE.voToShopVO(productVO);
                jsonVO.getShopSell().add(shopVO);
            }
            if (canRent){
                if(!jsonVO.getCanRent()) jsonVO.setCanRent(canRent);
                ProductRentShopVO shopVO = ProductMapper.INSTANCE.voToRentShopVO(productVO);
                jsonVO.getShopRent().add(shopVO);
            }
        });
        int i = jsonVO.getFileName().lastIndexOf(".");
        String jsonPath = jsonVO.getFileName().substring(0,i+1)+"json";
        jsonVO.setJsonPath(jsonPath.replaceAll("\\\\","/"));
        File file = new File(jsonPath);
        try {
            if (!file.getParentFile().exists()){
                Files.createDirectories(file.getParentFile().toPath());
            }
            if (!file.exists()){
                Files.createFile(file.toPath());
            }
            Files.write(file.toPath(),JsonUtil.toJson(jsonVO).getBytes(StandardCharsets.UTF_8));
        }catch (IOException e) {
            e.printStackTrace();
        }
        return jsonVO;
    }

}
