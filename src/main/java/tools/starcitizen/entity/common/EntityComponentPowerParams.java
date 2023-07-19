package tools.starcitizen.entity.common;

import lombok.Data;
import tools.starcitizen.processor.excel.ExcelColumn;
import tools.starcitizen.processor.excel.ExcelPrefix;

/**
 * @Author: wftank
 * @Date: 2020/10/4
 * @Description: 电力相关属性
 */
@Data
@ExcelPrefix("entityComponentPowerParams")
public class EntityComponentPowerParams {

    /**
     * 标准功率
     */
    @ExcelColumn
    private Double powerBase;

    /**
     * 极限功率
     */
    @ExcelColumn
    private Double powerDraw;

    /**
     * 上电时间（组件开启到最大功率的时间）
     */
    @ExcelColumn
    private Double timeToReachDrawRequest;

    /**
     * 能否通过节流阀控制 1是 0否
     */
    @ExcelColumn
    private Integer throttleable;

    /**
     * 能否超频
     */
    @ExcelColumn
    private Integer overclockable;

    /**
     * 超频最高阈值
     */
    @ExcelColumn
    private Double overclockThresholdMax;
    /**
     * 超电后的性能提升
     */
    @ExcelColumn
    private Double overpowerPerformance;

    /**
     * 超频后的性能提升
     */
    @ExcelColumn
    private Double overclockPerformance;

    /**
     * 功率-电磁辐射因子
     */
    @ExcelColumn
    private Double powerToEM;

    /**
     * 电磁辐射衰减系数
     */
    @ExcelColumn
    private Double decayRateOfEM;

    /**
     * 警报延时时间
     */
    @ExcelColumn
    private Double warningDelayTime;
    /**
     * 警报显示时间
     */
    @ExcelColumn
    private Double warningDisplayTime;



}
