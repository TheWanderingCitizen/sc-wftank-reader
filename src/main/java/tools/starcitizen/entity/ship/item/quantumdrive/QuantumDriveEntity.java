package tools.starcitizen.entity.ship.item.quantumdrive;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tools.starcitizen.entity.ship.item.ShipItemEntity;

/**
 * @Author: wftank
 * @Date: 2020/10/4
 * @Description: 量子引擎属性
 */
@Getter
@Setter
@ToString(callSuper = true)
public class QuantumDriveEntity extends ShipItemEntity {

    /**
     * 油耗
     */
    private Double quantumFuelRequirement;
    /**
     * 跳跃距离
     */
    private Double jumpRange;
    /**
     *
     */
    private Double disconnectRange;

    private JumpParams jumpParams;

    private SplineJumpParams splineJumpParams;

    private QuantumDriveHeatParams quantumDriveHeatParams;


}
