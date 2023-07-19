package tools.starcitizen.reader.ship.item;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import tools.starcitizen.entity.ship.item.shield.ShieldAbsorption;
import tools.starcitizen.entity.ship.item.shield.ShieldGeneratorEntity;
import tools.starcitizen.entity.ship.item.shield.ShieldHardening;
import tools.starcitizen.entity.ship.item.shield.ShieldResistance;
import tools.starcitizen.util.PropertiesReadUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: wftank
 * @Date: 2020/10/4
 * @Description: 护盾解析xml
 */
@Slf4j
public class ShieldGeneratorXmlReader extends ShipItemXmlReader<ShieldGeneratorEntity> {
    private String path = "/shieldgenerator";
    private String filePath;
    public ShieldGeneratorXmlReader(String baseDir) {
        super(baseDir);
        this.filePath = super.baseDir + this.path;
    }

    public String getFilePath() {
        return filePath;
    }

    @Override
    public Map<Class<ShieldGeneratorEntity>, Map<String,ShieldGeneratorEntity>> read() {
        Map<Class<ShieldGeneratorEntity>, Map<String,ShieldGeneratorEntity>> result = new HashMap<>();
        Map<String,ShieldGeneratorEntity> refEntityMap = new HashMap<>();
        result.put(ShieldGeneratorEntity.class,refEntityMap);
        File dir = new File(filePath);
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isFile()){
                if (!file.getName().toLowerCase().contains("template")){
                    ShieldGeneratorEntity ShieldGeneratorEntity = readFile(file);
                    refEntityMap.put(ShieldGeneratorEntity.getRef(),ShieldGeneratorEntity);
                }
            }else {
                log.warn("目录里出现文件夹：{}，将不会解析！",file.getName());
            }

        }
        return result;
    }

    private ShieldGeneratorEntity readFile(File file) {
        ShieldGeneratorEntity shieldGenerator = new ShieldGeneratorEntity();
        SAXReader reader = new SAXReader();
        try {
            super.readFile(file,shieldGenerator);
            Document shieldGeneratorDoc = reader.read(file);
            Element rootEle = shieldGeneratorDoc.getRootElement();
            //护盾属性
            Element generatorEle = (Element)rootEle.selectSingleNode("//SCItemShieldGeneratorParams");
            PropertiesReadUtil.readSameNameProperties(generatorEle,shieldGenerator);
            //护盾硬化属性
            ShieldHardening hardening = new ShieldHardening();
            Element hardeningEle = (Element)rootEle.selectSingleNode("//SCItemShieldGeneratorParams/ShieldHardening");
            PropertiesReadUtil.readSameNameProperties(hardeningEle,hardening);
            shieldGenerator.setShieldHardening(hardening);
            //护盾抗性
            List<Node> resistanceNodes = rootEle
                    .selectNodes("//SCItemShieldGeneratorParams/ShieldResistance/SShieldResistance");
            //物理抗性
            shieldGenerator.setPhysicalResistance(new ShieldResistance());
            PropertiesReadUtil.readSameNameProperties((Element) resistanceNodes.get(0),shieldGenerator.getPhysicalResistance());
            //能量抗性
            shieldGenerator.setEnergyResistance(new ShieldResistance());
            PropertiesReadUtil.readSameNameProperties((Element) resistanceNodes.get(1),shieldGenerator.getEnergyResistance());
            //干扰抗性
            shieldGenerator.setDistortionResistance(new ShieldResistance());
            PropertiesReadUtil.readSameNameProperties((Element) resistanceNodes.get(2),shieldGenerator.getDistortionResistance());
            //热抗性
            shieldGenerator.setThermalResistance(new ShieldResistance());
            PropertiesReadUtil.readSameNameProperties((Element) resistanceNodes.get(3),shieldGenerator.getThermalResistance());
            //生化抗性
            shieldGenerator.setBiochemicalResistance(new ShieldResistance());
            PropertiesReadUtil.readSameNameProperties((Element) resistanceNodes.get(4),shieldGenerator.getBiochemicalResistance());
            //冲撞抗性
            shieldGenerator.setStunResistance(new ShieldResistance());
            PropertiesReadUtil.readSameNameProperties((Element) resistanceNodes.get(5),shieldGenerator.getStunResistance());
            //护盾伤害吸收率
            List<Node> absorptionNodes = rootEle
                    .selectNodes("//SCItemShieldGeneratorParams/ShieldAbsorption/SShieldAbsorption");
            //物理伤害吸收率
            shieldGenerator.setPhysicalAbsorption(new ShieldAbsorption());
            PropertiesReadUtil.readSameNameProperties((Element) absorptionNodes.get(0),shieldGenerator.getPhysicalAbsorption());
            //能量伤害吸收率
            shieldGenerator.setEnergyAbsorption(new ShieldAbsorption());
            PropertiesReadUtil.readSameNameProperties((Element) absorptionNodes.get(1),shieldGenerator.getEnergyAbsorption());
            //干扰伤害吸收率
            shieldGenerator.setDistortionAbsorption(new ShieldAbsorption());
            PropertiesReadUtil.readSameNameProperties((Element) absorptionNodes.get(2),shieldGenerator.getDistortionAbsorption());
            //热伤害吸收率
            shieldGenerator.setThermalAbsorption(new ShieldAbsorption());
            PropertiesReadUtil.readSameNameProperties((Element) absorptionNodes.get(3),shieldGenerator.getThermalAbsorption());
            //生化伤害吸收率
            shieldGenerator.setBiochemicalAbsorption(new ShieldAbsorption());
            PropertiesReadUtil.readSameNameProperties((Element) absorptionNodes.get(4),shieldGenerator.getBiochemicalAbsorption());
            //冲撞伤害吸收率
            shieldGenerator.setStunAbsorption(new ShieldAbsorption());
            PropertiesReadUtil.readSameNameProperties((Element) absorptionNodes.get(5),shieldGenerator.getStunAbsorption());
            return shieldGenerator;
        } catch (DocumentException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return shieldGenerator;
    }
}
