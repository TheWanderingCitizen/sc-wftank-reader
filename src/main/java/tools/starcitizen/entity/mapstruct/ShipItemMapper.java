package tools.starcitizen.entity.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tools.starcitizen.entity.ship.item.cooler.CoolerEntity;
import tools.starcitizen.entity.ship.item.fueltanks.FueltanksEntity;
import tools.starcitizen.entity.ship.item.powerplant.PowerplantEntity;
import tools.starcitizen.entity.ship.item.quantumdrive.QuantumDriveEntity;
import tools.starcitizen.entity.ship.item.shield.ShieldGeneratorEntity;
import tools.starcitizen.entity.vo.ship.item.*;

@Mapper
public interface ShipItemMapper {

    ShipItemMapper INSTANCE = Mappers.getMapper( ShipItemMapper.class );

    CoolerVO coolerEntityToVO(CoolerEntity entity);
    FueltanksVO fueltanksEntityToVO(FueltanksEntity entity);
    PowerplantVO powerPlantEntityToVO(PowerplantEntity entity);
    QuantumDriveVO quantumDriveEntityToVO(QuantumDriveEntity entity);
    ShieldGeneratorVO shieldGeneratorEntityToVO(ShieldGeneratorEntity entity);

}
