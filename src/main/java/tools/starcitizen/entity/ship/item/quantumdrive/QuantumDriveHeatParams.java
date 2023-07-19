package tools.starcitizen.entity.ship.item.quantumdrive;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tools.starcitizen.processor.excel.ExcelColumn;
import tools.starcitizen.processor.excel.ExcelPrefix;

/**
 * @Author: wftank
 * @Date: 2020/10/7
 * @Description:
 */
@Getter
@Setter
@ToString(callSuper = true)
@ExcelPrefix("quantumDrive.heatParams")
public class QuantumDriveHeatParams {
   @ExcelColumn
   private Double preRampUpThermalEnergyDraw;
   @ExcelColumn
   private Double rampUpThermalEnergyDraw;
   @ExcelColumn
   private Double inFlightThermalEnergyDraw;
   @ExcelColumn
   private Double rampDownThermalEnergyDraw;
   @ExcelColumn
   private Double postRampDownThermalEnergyDraw;
}
