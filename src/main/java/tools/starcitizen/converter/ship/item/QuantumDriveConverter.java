package tools.starcitizen.converter.ship.item;

import tools.starcitizen.converter.ComponentConvertor;
import tools.starcitizen.converter.Converter;
import tools.starcitizen.entity.mapstruct.ShipItemMapper;
import tools.starcitizen.entity.ship.item.quantumdrive.QuantumDriveEntity;
import tools.starcitizen.entity.vo.ship.item.QuantumDriveVO;

/**
 * @Author: wftank
 * @Date: 2020/10/5
 * @Description: 量子引擎实体转换器
 */
public class QuantumDriveConverter implements Converter<QuantumDriveVO> {

    @Override
    public QuantumDriveVO convert(Object source) {
        QuantumDriveEntity entity = (QuantumDriveEntity) source;
        QuantumDriveVO vo = ShipItemMapper.INSTANCE.quantumDriveEntityToVO(entity);
        ComponentConvertor.convert(vo);
        return vo;
    }
}
