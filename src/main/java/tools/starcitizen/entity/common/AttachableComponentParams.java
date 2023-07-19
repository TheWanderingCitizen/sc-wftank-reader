package tools.starcitizen.entity.common;

import lombok.Data;
import tools.starcitizen.processor.excel.ExcelColumn;
import tools.starcitizen.processor.excel.ExcelPrefix;

/**
 * @Author: wftank
 * @Date: 2020/10/4
 * @Description:
 */
@Data
@ExcelPrefix("attachableComponentParams")
public class AttachableComponentParams {

    /**
     * 组件名
     */
    @ExcelColumn
    private LocalizationParams localizationParams;
    /**
     * 组件类型
     */
    @ExcelColumn
    private String type;
    /**
     * 组件子类型
     */
    @ExcelColumn
    private String subType;

    /**
     * 生产商引用key
     */
    private String manufacturer;
    /**
     * 尺寸
     */
    @ExcelColumn
    private Integer size;
    /**
     * 等级
     */
    @ExcelColumn
    private Integer grade;


    /**
     * 库存占用量 单位centSCU
     */
    @ExcelColumn
    private Integer inventoryOccupancy;

}
