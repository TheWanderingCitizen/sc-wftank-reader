package tools.starcitizen.converter.ship.item;

import tools.starcitizen.converter.ComponentConvertor;
import tools.starcitizen.converter.Converter;
import tools.starcitizen.entity.mapstruct.ShipItemMapper;
import tools.starcitizen.entity.ship.item.fueltanks.FueltanksEntity;
import tools.starcitizen.entity.vo.ship.item.FueltanksVO;

/**
 * @Author: wftank
 * @Date: 2020/10/5
 * @Description: 油箱实体转换器
 */
public class FueltanksConverter implements Converter<FueltanksVO> {

    @Override
    public FueltanksVO convert(Object source) {
        FueltanksEntity entity = (FueltanksEntity) source;
        FueltanksVO vo = ShipItemMapper.INSTANCE.fueltanksEntityToVO(entity);
        ComponentConvertor.convert(vo);
        return vo;
    }
}
