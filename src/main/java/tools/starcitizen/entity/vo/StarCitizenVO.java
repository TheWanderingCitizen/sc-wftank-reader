package tools.starcitizen.entity.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tools.starcitizen.processor.excel.ExcelColumn;
import tools.starcitizen.processor.excel.ExcelPrefix;

/**
 * @Author: wftank
 * @Date: 2020/10/5
 * @Description: 组件通用实体
 */
@Getter
@Setter
@ToString(callSuper = true)
@ExcelPrefix("starcitizen")
public class StarCitizenVO {
    @ExcelColumn
    private String fileName;
}
