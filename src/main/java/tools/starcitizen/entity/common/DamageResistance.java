package tools.starcitizen.entity.common;

import lombok.Data;
import tools.starcitizen.processor.excel.ExcelColumn;
import tools.starcitizen.processor.excel.ExcelPrefix;

/**
 * @Author: wftank
 * @Date: 2020/10/4
 * @Description: 伤害抗性
 */
@Data
@ExcelPrefix("damageResistance")
public class DamageResistance {

    /**
     * 因子
     */
    @ExcelColumn
    private Double multiplier;
    /**
     * 阈值
     */
    @ExcelColumn
    private Double threshold;

}
