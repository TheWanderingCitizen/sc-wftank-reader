package tools.starcitizen.reader.ship.item;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import tools.starcitizen.entity.ship.item.fueltanks.FueltanksEntity;
import tools.starcitizen.util.PropertiesReadUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: wftank
 * @Date: 2020/10/4
 * @Description: 邮箱解析xml
 */
@Slf4j
public class FuelTanksXmlReader extends ShipItemXmlReader<FueltanksEntity> {
    private String path = "/fueltanks";
    private String filePath;
    public FuelTanksXmlReader(String baseDir) {
        super(baseDir);
        this.filePath = super.baseDir + this.path;
    }

    public String getFilePath() {
        return filePath;
    }

    @Override
    public Map<Class<FueltanksEntity>, Map<String,FueltanksEntity>> read() {
        Map<Class<FueltanksEntity>, Map<String,FueltanksEntity>> result = new HashMap<>();
        Map<String,FueltanksEntity> refEntityMap = new HashMap<>();
        result.put(FueltanksEntity.class,refEntityMap);
        File dir = new File(filePath);
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isFile()){
                if (!file.getName().toLowerCase().contains("template")){
                    FueltanksEntity FueltanksEntity = readFile(file);
                    refEntityMap.put(FueltanksEntity.getRef(),FueltanksEntity);
                }
            }else {
                log.warn("目录里出现文件夹：{}，将不会解析！",file.getName());
            }

        }
        return result;
    }

    private FueltanksEntity readFile(File file) {
        FueltanksEntity cooler = new FueltanksEntity();
        SAXReader reader = new SAXReader();
        try {
            super.readFile(file,cooler);
            Document fuelTanksDoc = reader.read(file);
            Element rootEle = fuelTanksDoc.getRootElement();
            //冷却器属性
            Element fuelTanksEle = (Element)rootEle.selectSingleNode("//SCItemFuelTankParams");
            PropertiesReadUtil.readSameNameProperties(fuelTanksEle,cooler);
            return cooler;
        } catch (DocumentException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return cooler;
    }
}
