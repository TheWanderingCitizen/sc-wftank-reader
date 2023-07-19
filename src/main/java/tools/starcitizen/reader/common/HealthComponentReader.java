package tools.starcitizen.reader.common;

import org.dom4j.Element;
import org.dom4j.Node;
import tools.starcitizen.entity.common.DamageResistance;
import tools.starcitizen.entity.common.HealthComponentParams;

/**
 * @Author: wftank
 * @Date: 2020/10/4
 * @Description: 耐久属性解析
 */
public class HealthComponentReader {

    public HealthComponentParams read(Node node) {
        HealthComponentParams params = new HealthComponentParams();
        Element ele = (Element)node;
        params.setHealth(Double.valueOf(ele.attributeValue("Health")));
        //设置伤害抗性
        params.setPhysicalResistance(getDamageResistanceByType("Physical",ele));
        params.setEnergyResistance(getDamageResistanceByType("Energy",ele));
        params.setDistortionResistance(getDamageResistanceByType("Distortion",ele));
        params.setThermalResistance(getDamageResistanceByType("Thermal",ele));
        params.setBiochemicalResistance(getDamageResistanceByType("Biochemical",ele));
        params.setStunResistance(getDamageResistanceByType("Stun",ele));
        return params;
    }

    private DamageResistance getDamageResistanceByType(String type,Element ele) {
        Element physicalEle = (Element)ele.selectSingleNode("//DamageResistances/DamageResistance/"+type+"Resistance");
        DamageResistance damageResistance = new DamageResistance();
        damageResistance.setMultiplier(Double.valueOf(physicalEle.attributeValue("Multiplier")));
        damageResistance.setThreshold(Double.valueOf(physicalEle.attributeValue("Threshold")));
        return damageResistance;
    }
}
