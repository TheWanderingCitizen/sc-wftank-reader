package tools.starcitizen.entity.ship.item.shield;

import lombok.Getter;
import lombok.Setter;
import tools.starcitizen.processor.excel.ExcelColumn;
import tools.starcitizen.processor.excel.ExcelPrefix;

/**
 * @Author: wftank
 * @Date: 2020/10/4
 * @Description: 护盾硬化属性
 */
@Getter
@Setter
@ExcelPrefix("shieldHardening")
public class ShieldHardening {

    /**
     * 系数
     */
    @ExcelColumn
    private Double Factor;
    /**
     * 持续时间
     */
    @ExcelColumn
    private Double duration;
    /**
     * 冷却时间
     */
    @ExcelColumn
    private Double cooldown;
}
