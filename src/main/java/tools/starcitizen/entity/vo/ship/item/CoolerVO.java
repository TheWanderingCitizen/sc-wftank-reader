package tools.starcitizen.entity.vo.ship.item;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tools.starcitizen.processor.excel.ExcelColumn;
import tools.starcitizen.processor.excel.ExcelPrefix;

/**
 * @Author: wftank
 * @Date: 2020/10/5
 * @Description: 散热器视图
 */
@Getter
@Setter
@ToString
@ExcelPrefix("cooler")
public class CoolerVO extends ShipItemVO {
    @ExcelColumn
    private Integer coolingRate;
    @ExcelColumn
    private Double suppressionIRFactor;
    @ExcelColumn
    private Double suppressionHeatFactor;
}
