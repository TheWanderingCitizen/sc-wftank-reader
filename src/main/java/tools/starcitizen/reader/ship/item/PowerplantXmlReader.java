package tools.starcitizen.reader.ship.item;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.dom4j.io.SAXReader;
import tools.starcitizen.entity.ship.item.powerplant.PowerplantEntity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: wftank
 * @Date: 2020/10/4
 * @Description: 电池xml解析
 */
@Slf4j
public class PowerplantXmlReader extends ShipItemXmlReader<PowerplantEntity> {
    private String path = "/powerplant";
    private String filePath;
    public PowerplantXmlReader(String baseDir) {
        super(baseDir);
        this.filePath = super.baseDir + this.path;
    }

    public String getFilePath() {
        return filePath;
    }

    @Override
    public Map<Class<PowerplantEntity>, Map<String,PowerplantEntity>> read() {
        Map<Class<PowerplantEntity>, Map<String,PowerplantEntity>> result = new HashMap<>();
        Map<String,PowerplantEntity> refEntityMap = new HashMap<>();
        result.put(PowerplantEntity.class,refEntityMap);
        File dir = new File(filePath);
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isFile()){
                if (!file.getName().toLowerCase().contains("template")){
                    PowerplantEntity PowerplantEntity = readFile(file);
                    refEntityMap.put(PowerplantEntity.getRef(),PowerplantEntity);
                }
            }else {
                log.warn("目录里出现文件夹：{}，将不会解析！",file.getName());
            }

        }
        return result;
    }

    private PowerplantEntity readFile(File file) {
        PowerplantEntity powerplant = new PowerplantEntity();
        SAXReader reader = new SAXReader();
        try {
            super.readFile(file,powerplant);
            return powerplant;
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return powerplant;
    }
}
