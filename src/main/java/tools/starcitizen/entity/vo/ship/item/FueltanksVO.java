package tools.starcitizen.entity.vo.ship.item;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tools.starcitizen.entity.vo.ComponentVO;
import tools.starcitizen.processor.excel.ExcelColumn;
import tools.starcitizen.processor.excel.ExcelPrefix;

/**
 * @Author: wftank
 * @Date: 2020/10/4
 * @Description: 电池VO
 */
@Getter
@Setter
@ToString(callSuper = true)
@ExcelPrefix("fueltanks")
public class FueltanksVO extends ComponentVO {

    /**
     * 填充速度
     */
    @ExcelColumn
    private Double fillRate;
    /**
     * 消耗速度
     */
    @ExcelColumn
    private Double drainRate;
    /**
     * 容量
     */
    @ExcelColumn
    private Double capacity;

}
