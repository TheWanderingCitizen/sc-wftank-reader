package tools.starcitizen.converter.ship.item;

import tools.starcitizen.converter.ComponentConvertor;
import tools.starcitizen.converter.Converter;
import tools.starcitizen.entity.mapstruct.ShipItemMapper;
import tools.starcitizen.entity.ship.item.cooler.CoolerEntity;
import tools.starcitizen.entity.vo.ship.item.CoolerVO;

/**
 * @Author: wftank
 * @Date: 2020/10/5
 * @Description: 冷却器实体转换器
 */
public class CoolerConverter implements Converter<CoolerVO> {

    @Override
    public CoolerVO convert(Object source) {
        CoolerEntity entity = (CoolerEntity) source;
        CoolerVO vo = ShipItemMapper.INSTANCE.coolerEntityToVO(entity);
        ComponentConvertor.convert(vo);
        return vo;
    }
}
