package tools.starcitizen.processor.excel;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import tools.starcitizen.config.ExcelConfig;
import tools.starcitizen.entity.StarCitizenEntity;
import tools.starcitizen.entity.product.ProductEntity;
import tools.starcitizen.entity.ship.item.cooler.CoolerEntity;
import tools.starcitizen.entity.ship.item.fueltanks.FueltanksEntity;
import tools.starcitizen.entity.ship.item.powerplant.PowerplantEntity;
import tools.starcitizen.entity.ship.item.quantumdrive.QuantumDriveEntity;
import tools.starcitizen.entity.ship.item.shield.ShieldGeneratorEntity;
import tools.starcitizen.processor.Processor;
import tools.starcitizen.processor.excel.product.JsonFileProductProcessor;
import tools.starcitizen.processor.excel.ship.item.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author: wftank
 * @Date: 2020/10/5
 * @Description: 将解析结果生成为Excel
 */
@Slf4j
public class DefaultExcelProcessor implements Processor {

    private Map<Class,ExcelProcessor> processorMap = new LinkedHashMap<>();

    private String filePath = "./starcitizen.xlsx";

    {
        init();
    }

    public void init(){
        ExcelConfig.init();
    }



    public DefaultExcelProcessor() {
        processorMap.put(CoolerEntity.class,new CoolerExcelProcessor("散热器"));
        processorMap.put(ShieldGeneratorEntity.class,new ShieldGeneratorExcelProcessor("护盾生成器"));
        processorMap.put(PowerplantEntity.class,new PowerplantProcessor("电源"));
        processorMap.put(FueltanksEntity.class,new FueltanksProcessor("油箱"));
        processorMap.put(QuantumDriveEntity.class,new QuantumDriveExcelProcessor("量子引擎"));
        processorMap.put(ProductEntity.class,new JsonFileProductProcessor("商品数据原表"));
    }

    @Override
    public void process(Map<Class,Map<String, StarCitizenEntity>> data) {
        File file = new File(filePath);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            Workbook workbook = new SXSSFWorkbook(100);

            //通过子类型解析器解析所有文件
            Iterator<Class> iterator = data.keySet().iterator();
            while (iterator.hasNext()){
                Class clazz = iterator.next();
                ExcelProcessor excelProcessor = processorMap.get(clazz);
                if (excelProcessor != null){
                    excelProcessor.process(data.get(clazz),workbook);
                }else{
                    log.warn("{} 没有相应的处理器支持",clazz.getSimpleName());
                }
            }
            workbook.write(fos);
        }catch (Exception e){
            log.error("生成Excel异常：异常详情：\n{}", ExceptionUtils.getStackTrace(e));
        }finally {
            try {
                fos.close();
            } catch (IOException e) {
                log.error(ExceptionUtils.getStackTrace(e));
            }
        }
    }

}
