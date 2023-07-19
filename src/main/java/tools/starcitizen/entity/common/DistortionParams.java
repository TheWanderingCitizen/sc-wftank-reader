package tools.starcitizen.entity.common;

import lombok.Data;
import tools.starcitizen.processor.excel.ExcelColumn;
import tools.starcitizen.processor.excel.ExcelPrefix;

/**
 * @Author: wftank
 * @Date: 2020/10/4
 * @Description: 干扰属性
 */
@Data
@ExcelPrefix("distortionParams")
public class DistortionParams {

    /**
     * 衰减系数
     */
    @ExcelColumn
    private Double decayRate;

    /**
     * 最大值
     */
    @ExcelColumn
    private Double maximum;
    /**
     * 过载系数
     */
    @ExcelColumn
    private Double overloadRatio;
    /**
     * 恢复系数
     */
    @ExcelColumn
    private Double recoveryRatio;

    /**
     * 恢复时间
     */
    @ExcelColumn
    private Double recoveryTime;
}
