package tools.starcitizen.reader.ship.item;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import tools.starcitizen.entity.ship.item.cooler.CoolerEntity;
import tools.starcitizen.util.PropertiesReadUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: wftank
 * @Date: 2020/10/4
 * @Description: 冷却器解析xml
 */
@Slf4j
public class CoolerXmlReader extends ShipItemXmlReader<CoolerEntity> {
    private String path = "/cooler";
    private String filePath;
    public CoolerXmlReader(String baseDir) {
        super(baseDir);
        this.filePath = super.baseDir + this.path;
    }

    public String getFilePath() {
        return filePath;
    }

    @Override
    public Map<Class<CoolerEntity>, Map<String,CoolerEntity>> read() {
        Map<Class<CoolerEntity>, Map<String,CoolerEntity>> result = new HashMap<>();
        Map<String,CoolerEntity> refEntityMap = new HashMap<>();
        result.put(CoolerEntity.class,refEntityMap);
        File dir = new File(filePath);
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isFile()){
                if (!file.getName().toLowerCase().contains("template")){
                    CoolerEntity coolerEntity = readFile(file);
                    refEntityMap.put(coolerEntity.getRef(),coolerEntity);
                }
            }else {
                log.warn("目录里出现文件夹：{}，将不会解析！",file.getName());
            }

        }
        return result;
    }

    private CoolerEntity readFile(File file) {
        CoolerEntity cooler = new CoolerEntity();
        SAXReader reader = new SAXReader();
        try {
            super.readFile(file,cooler);
            Document coolerDoc = reader.read(file);
            Element rootEle = coolerDoc.getRootElement();
            //冷却器属性
            Element coolerEle = (Element)rootEle.selectSingleNode("//SCItemCoolerParams");
            PropertiesReadUtil.readSameNameProperties(coolerEle,cooler);
            return cooler;
        } catch (DocumentException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return cooler;
    }
}
