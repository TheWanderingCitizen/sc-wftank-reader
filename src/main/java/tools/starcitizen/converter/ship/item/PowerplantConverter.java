package tools.starcitizen.converter.ship.item;

import tools.starcitizen.converter.ComponentConvertor;
import tools.starcitizen.converter.Converter;
import tools.starcitizen.entity.mapstruct.ShipItemMapper;
import tools.starcitizen.entity.ship.item.powerplant.PowerplantEntity;
import tools.starcitizen.entity.vo.ship.item.PowerplantVO;

/**
 * @Author: wftank
 * @Date: 2020/10/5
 * @Description: 冷却器实体转换器
 */
public class PowerplantConverter implements Converter<PowerplantVO> {

    @Override
    public PowerplantVO convert(Object source) {
        PowerplantEntity entity = (PowerplantEntity) source;
        PowerplantVO vo = ShipItemMapper.INSTANCE.powerPlantEntityToVO(entity);
        ComponentConvertor.convert(vo);
        return vo;
    }
}
