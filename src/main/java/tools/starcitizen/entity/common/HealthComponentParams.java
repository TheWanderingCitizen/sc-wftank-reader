package tools.starcitizen.entity.common;

import lombok.Data;
import tools.starcitizen.processor.excel.ExcelColumn;
import tools.starcitizen.processor.excel.ExcelPrefix;

/**
 * @Author: wftank
 * @Date: 2020/10/4
 * @Description: 物品耐久相关属性
 */
@Data
@ExcelPrefix("healthComponentParams")
public class HealthComponentParams {

    /**
     * 生命值
     */
    @ExcelColumn
    private Double health;

    /**
     * 物理抗性
     */
    @ExcelColumn
    private DamageResistance physicalResistance;

    /**
     * 能量抗性
     */
    @ExcelColumn
    private DamageResistance energyResistance;

    /**
     * 干扰抗性
     */
    @ExcelColumn
    private DamageResistance distortionResistance;

    /**
     * 热抗性
     */
    @ExcelColumn
    private DamageResistance thermalResistance;

    /**
     * 生化抗性
     */
    @ExcelColumn
    private DamageResistance biochemicalResistance;

    /**
     * 撞击抗性
     */
    @ExcelColumn
    private DamageResistance stunResistance;

}
