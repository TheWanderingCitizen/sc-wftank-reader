package tools.starcitizen.reader.ship.item;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import tools.starcitizen.entity.ship.item.quantumdrive.JumpParams;
import tools.starcitizen.entity.ship.item.quantumdrive.QuantumDriveEntity;
import tools.starcitizen.entity.ship.item.quantumdrive.QuantumDriveHeatParams;
import tools.starcitizen.entity.ship.item.quantumdrive.SplineJumpParams;
import tools.starcitizen.util.PropertiesReadUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: wftank
 * @Date: 2020/10/4
 * @Description: 量子引擎解析xml
 */
@Slf4j
public class QuantumDriveXmlReader extends ShipItemXmlReader<QuantumDriveEntity> {
    private String path = "/quantumdrive";
    private String filePath;
    public QuantumDriveXmlReader(String baseDir) {
        super(baseDir);
        this.filePath = super.baseDir + this.path;
    }

    public String getFilePath() {
        return filePath;
    }

    @Override
    public Map<Class<QuantumDriveEntity>, Map<String,QuantumDriveEntity>> read() {
        Map<Class<QuantumDriveEntity>, Map<String,QuantumDriveEntity>> result = new HashMap<>();
        Map<String,QuantumDriveEntity> refEntityMap = new HashMap<>();
        result.put(QuantumDriveEntity.class,refEntityMap);
        File dir = new File(filePath);
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isFile()){
                if (!file.getName().toLowerCase().contains("template")){
                    QuantumDriveEntity QuantumDriveEntity = readFile(file);
                    refEntityMap.put(QuantumDriveEntity.getRef(),QuantumDriveEntity);
                }
            }else {
                log.warn("目录里出现文件夹：{}，将不会解析！",file.getName());
            }

        }
        return result;
    }

    private QuantumDriveEntity readFile(File file) {
        QuantumDriveEntity quantumDrive = new QuantumDriveEntity();
        SAXReader reader = new SAXReader();
        try {
            super.readFile(file,quantumDrive);
            Document QuantumDriveDoc = reader.read(file);
            Element rootEle = QuantumDriveDoc.getRootElement();
            //量子引擎属性
            Element generatorEle = (Element)rootEle.selectSingleNode("//SCItemQuantumDriveParams");
            PropertiesReadUtil.readSameNameProperties(generatorEle,quantumDrive);

            //跳跃属性
            JumpParams jumpParams = new JumpParams();
            Element jumpParamsEle = (Element)rootEle.selectSingleNode("//SCItemQuantumDriveParams/params");
            PropertiesReadUtil.readSameNameProperties(jumpParamsEle,jumpParams);
            quantumDrive.setJumpParams(jumpParams);
            //跳跃属性
            SplineJumpParams splineJumpParams = new SplineJumpParams();
            Element splineJumpParamsEle = (Element)rootEle.selectSingleNode("//SCItemQuantumDriveParams/splineJumpParams");
            PropertiesReadUtil.readSameNameProperties(splineJumpParamsEle,splineJumpParams);
            quantumDrive.setSplineJumpParams(splineJumpParams);
            //热属性
            QuantumDriveHeatParams quantumDriveHeatParams = new QuantumDriveHeatParams();
            Element heatParamsEle = (Element)rootEle.selectSingleNode("//SCItemQuantumDriveParams/heatParams");
            PropertiesReadUtil.readSameNameProperties(heatParamsEle, quantumDriveHeatParams);
            quantumDrive.setQuantumDriveHeatParams(quantumDriveHeatParams);
            return quantumDrive;
        } catch (DocumentException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return quantumDrive;
    }
}
