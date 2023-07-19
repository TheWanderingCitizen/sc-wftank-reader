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
@ExcelPrefix("quantumDrive.jumpParames")
public class JumpParams {
   @ExcelColumn
   private Double driveSpeed;
   @ExcelColumn
   private Double cooldownTime;
   @ExcelColumn
   private Double stageOneAccelRate;
   @ExcelColumn
   private Double stageTwoAccelRate;
   @ExcelColumn
   private Double engageSpeed;
   @ExcelColumn
   private Double vFXSpoolEndVelocity;
   @ExcelColumn
   private Double vFXPinchEffectTime;
   @ExcelColumn
   private Double vFXPinchMaxVelocity;
   @ExcelColumn
   private Double vFXEntryFlashVelocity;
   @ExcelColumn
   private Double vFXTrailStartVelocity;
   @ExcelColumn
   private Double vFXTravelEffectStartVelocity;
   @ExcelColumn
   private Double vFXTravelEffectEndVelocity;
   @ExcelColumn
   private Double vFXExitEffectVelocity;
   @ExcelColumn
   private Double shaderNodeEngageVelocity;
   @ExcelColumn
   private Double shaderNodeMaxStrengthVelocity;
   @ExcelColumn
   private Double interdictionEffectTime;
   @ExcelColumn
   private Double calibrationRate;
   @ExcelColumn
   private Double minCalibrationRequirement;
   @ExcelColumn
   private Double maxCalibrationRequirement;
   @ExcelColumn
   private Double calibrationProcessAngleLimit;
   @ExcelColumn
   private Double calibrationWarningAngleLimit;
   @ExcelColumn
   private Double calibrationDelayInSeconds;
   @ExcelColumn
   private Double spoolUpTime;

}
