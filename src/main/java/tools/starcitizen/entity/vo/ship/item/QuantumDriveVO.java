package tools.starcitizen.entity.vo.ship.item;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tools.starcitizen.entity.ship.item.quantumdrive.JumpParams;
import tools.starcitizen.entity.ship.item.quantumdrive.QuantumDriveHeatParams;
import tools.starcitizen.entity.ship.item.quantumdrive.SplineJumpParams;
import tools.starcitizen.processor.excel.ExcelColumn;
import tools.starcitizen.processor.excel.ExcelPrefix;

/**
 * @Author: wftank
 * @Date: 2020/10/4
 * @Description: 护盾VO
 */
@Getter
@Setter
@ToString(callSuper = true)
@ExcelPrefix("quantumDrive")
public class QuantumDriveVO extends ShipItemVO {

    /**
     * 油耗
     */
    @ExcelColumn
    private Double quantumFuelRequirement;
    /**
     * 跳跃距离
     */
    @ExcelColumn
    private Double jumpRange;
    /**
     *
     */
    @ExcelColumn
    private Double disconnectRange;
    @ExcelColumn
    private JumpParams jumpParams;
    @ExcelColumn
    private SplineJumpParams splineJumpParams;
    @ExcelColumn
    private QuantumDriveHeatParams quantumDriveHeatParams;


}
