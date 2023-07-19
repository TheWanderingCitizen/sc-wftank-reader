package tools.starcitizen.entity.ship.item;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tools.starcitizen.entity.ComponentEntity;
import tools.starcitizen.entity.common.*;

/**
 * @Author: wftank
 * @Date: 2020/10/5
 * @Description: 舰船组件通用属性
 */
@Getter
@Setter
@ToString(callSuper = true)
public class ShipItemEntity extends ComponentEntity {
    private EntityPhysicsControllerParams physicsControllerParams;
    private ItemPurchasableParams itemPurchasableParams;
    private HealthComponentParams healthParams;
    private EntityComponentPowerParams powerParams;
    private EntityComponentHeatParams heatParams;
    private DegradationParams degradationParams;
    private DistortionParams distortionParams;
}
