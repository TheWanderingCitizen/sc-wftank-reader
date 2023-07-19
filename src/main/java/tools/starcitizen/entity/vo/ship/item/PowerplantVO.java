package tools.starcitizen.entity.vo.ship.item;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tools.starcitizen.processor.excel.ExcelPrefix;

/**
 * @Author: wftank
 * @Date: 2020/10/4
 * @Description: 电池VO
 */
@Getter
@Setter
@ToString(callSuper = true)
@ExcelPrefix("powerplant")
public class PowerplantVO extends ShipItemVO {

}
