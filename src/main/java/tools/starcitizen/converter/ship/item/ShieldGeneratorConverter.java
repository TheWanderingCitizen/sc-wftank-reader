package tools.starcitizen.converter.ship.item;

import tools.starcitizen.converter.ComponentConvertor;
import tools.starcitizen.converter.Converter;
import tools.starcitizen.entity.mapstruct.ShipItemMapper;
import tools.starcitizen.entity.ship.item.shield.ShieldGeneratorEntity;
import tools.starcitizen.entity.vo.ship.item.ShieldGeneratorVO;

/**
 * @Author: wftank
 * @Date: 2020/10/5
 * @Description: 冷却器实体转换器
 */
public class ShieldGeneratorConverter implements Converter<ShieldGeneratorVO> {

    @Override
    public ShieldGeneratorVO convert(Object source) {
        ShieldGeneratorEntity entity = (ShieldGeneratorEntity) source;
        ShieldGeneratorVO vo = ShipItemMapper.INSTANCE.shieldGeneratorEntityToVO(entity);
        ComponentConvertor.convert(vo);
        //由于在展示时需要区分伤害类型,所以先写成多个
        vo.setPhysicalResistanceMin(entity.getPhysicalResistance().getMin());
        vo.setPhysicalResistanceMax(entity.getPhysicalResistance().getMax());
        vo.setEnergyResistanceMin(entity.getEnergyResistance().getMin());
        vo.setEnergyResistanceMax(entity.getEnergyResistance().getMax());
        vo.setDistortionResistanceMin(entity.getDistortionResistance().getMin());
        vo.setDistortionResistanceMax(entity.getDistortionResistance().getMax());
        vo.setThermalResistanceMin(entity.getThermalResistance().getMin());
        vo.setThermalResistanceMax(entity.getThermalResistance().getMax());
        vo.setBiochemicalResistanceMin(entity.getBiochemicalResistance().getMin());
        vo.setBiochemicalResistanceMax(entity.getBiochemicalResistance().getMax());
        vo.setStunResistanceMin(entity.getStunResistance().getMin());
        vo.setStunResistanceMax(entity.getStunResistance().getMax());

        vo.setPhysicalAbsorptionMin(entity.getPhysicalAbsorption().getMin());
        vo.setPhysicalAbsorptionMax(entity.getPhysicalAbsorption().getMax());
        vo.setEnergyAbsorptionMin(entity.getEnergyAbsorption().getMin());
        vo.setEnergyAbsorptionMax(entity.getEnergyAbsorption().getMax());
        vo.setDistortionAbsorptionMin(entity.getDistortionAbsorption().getMin());
        vo.setDistortionAbsorptionMax(entity.getDistortionAbsorption().getMax());
        vo.setThermalAbsorptionMin(entity.getThermalAbsorption().getMin());
        vo.setThermalAbsorptionMax(entity.getThermalAbsorption().getMax());
        vo.setBiochemicalAbsorptionMin(entity.getBiochemicalAbsorption().getMin());
        vo.setBiochemicalAbsorptionMax(entity.getBiochemicalAbsorption().getMax());
        vo.setStunAbsorptionMin(entity.getStunAbsorption().getMin());
        vo.setStunAbsorptionMax(entity.getStunAbsorption().getMax());
        return vo;
    }
}
