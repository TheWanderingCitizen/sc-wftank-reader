package tools.starcitizen.entity.common;

import lombok.Data;
import tools.starcitizen.processor.excel.ExcelColumn;
import tools.starcitizen.processor.excel.ExcelPrefix;

/**
 * @Author: wftank
 * @Date: 2020/10/4
 * @Description: 老化相关属性
 */
@Data
@ExcelPrefix("degradationParams")
public class DegradationParams {

    /**
     * 最大使用时间
     */
    @ExcelColumn
    private Double maxLifetimeHours;

    /**
     * 初始寿命系数
     */
    @ExcelColumn
    private Double initialAgeRatio;

    /**
     * 是否只在受伤害时降低耐久 1是 0 否
     */
    @ExcelColumn
    private Integer degradeOnlyWhenAttached;

}
