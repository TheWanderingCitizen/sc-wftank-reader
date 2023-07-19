package tools.starcitizen.entity.common;

import lombok.Data;
import tools.starcitizen.processor.excel.ExcelColumn;
import tools.starcitizen.processor.excel.ExcelPrefix;

/**
 * @Author: wftank
 * @Date: 2020/10/4
 * @Description: 热相关属性
 */
@Data
@ExcelPrefix("entityComponentHeatParams")
public class EntityComponentHeatParams {

    /**
     * 温度-红外系数
     */
    @ExcelColumn
    private Double temperatureToIR;

    /**
     * 超电后产生热量
     */
    @ExcelColumn
    private Double overpowerHeat;
    /**
     * 超频后的最小热量阈值
     */
    @ExcelColumn
    private Double overclockThresholdMinHeat;
    /**
     * 超频后的最大热量阈值
     */
    @ExcelColumn
    private Double overclockThresholdMaxHeat;

    /**
     * 热能标准值
     */
    @ExcelColumn
    private Double thermalEnergyBase;
    /**
     * 热能最大值
     */
    @ExcelColumn
    private Double thermalEnergyDraw;

    /**
     * 导热系数
     */
    @ExcelColumn
    private Double thermalConductivity;

    /**
     * 比热容
     */
    @ExcelColumn
    private Double specificHeatCapacity;

    /**
     * 质量
     */
    @ExcelColumn
    private Double mass;

    /**
     * 表面积
     */
    @ExcelColumn
    private Double surfaceArea;

     /**
     * 开始进行散热的温度
     */
     @ExcelColumn
    private Integer startCoolingTemperature;

    /**
     * 最大散热系数
     */
    @ExcelColumn
    private Double maxCoolingRate;

    /**
     * 最高温度
     */
    @ExcelColumn
    private Integer maxTemperature;

    /**
     * 过热温度
     */
    @ExcelColumn
    private Integer overheatTemperature;
    /**
     * 恢复温度
     */
    @ExcelColumn
    private Integer recoveryTemperature;
    /**
     * 最低温度
     */
    @ExcelColumn
    private Integer minTemperature;
    /**
     * 最低失火温度
     */
    @ExcelColumn
    private Integer misfireMinTemperature;

    /**
     * 最高失火温度
     */
    @ExcelColumn
    private Integer misfireMaxTemperature;


}
