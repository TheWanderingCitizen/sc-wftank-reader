package tools.starcitizen.entity.vo.ship.item;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tools.starcitizen.entity.common.*;
import tools.starcitizen.entity.vo.ComponentVO;
import tools.starcitizen.processor.excel.ExcelColumn;

/**
 * @Author: wftank
 * @Date: 2020/10/5
 * @Description: 舰船组件通用视图
 */
@Getter
@Setter
@ToString
public class ShipItemVO extends ComponentVO {
    @ExcelColumn
    private EntityPhysicsControllerParams physicsControllerParams;
    @ExcelColumn
    private ItemPurchasableParams itemPurchasableParams;
    @ExcelColumn
    private HealthComponentParams healthParams;
    @ExcelColumn
    private EntityComponentPowerParams powerParams;
    @ExcelColumn
    private EntityComponentHeatParams heatParams;
    @ExcelColumn
    private DegradationParams degradationParams;
    @ExcelColumn
    private DistortionParams distortionParams;
}
