package tools.starcitizen.entity.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tools.starcitizen.entity.common.AttachableComponentParams;
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
@ExcelPrefix("component")
public class ComponentVO extends StarCitizenVO {

    /**
     * 组件名
     */
    @ExcelColumn
    private String name;
    @ExcelColumn
    private String nameCn;
    /**
     * 组件描述
     */
    @ExcelColumn
    private String description;
    @ExcelColumn
    private String descriptionCn;

    /**
     * 组件名
     */
    @ExcelColumn
    private String manufacturer;
    @ExcelColumn
    private String manufacturerCn;

    @ExcelColumn
    private AttachableComponentParams attachableParams;
}
