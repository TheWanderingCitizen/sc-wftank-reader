package tools.starcitizen.entity.vo.ship.item;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tools.starcitizen.entity.ship.item.shield.ShieldHardening;
import tools.starcitizen.processor.excel.ExcelColumn;
import tools.starcitizen.processor.excel.ExcelPrefix;

/**
 * @Author: wftank
 * @Date: 2020/10/4
 * @Description: 护盾VO
 */
@Getter
@Setter
@ToString(callSuper = true)
@ExcelPrefix("shieldGenerator")
public class ShieldGeneratorVO extends ShipItemVO {

    /**
     * 护盾最大生命值
     */
    @ExcelColumn
    private Double maxShieldHealth;
    /**
     * 护盾最大充能速度
     */
    @ExcelColumn
    private Double maxShieldRegen;
    /**
     * 衰减系数
     */
    @ExcelColumn
    private Double decayRatio;

    /**
     * 受伤充能延迟
     */
    @ExcelColumn
    private Double damagedRegenDelay;

    /**
     * 最大重分配系数
     */
    @ExcelColumn
    private Double maxReallocation;

    /**
     * 重分配速率
     */
    @ExcelColumn
    private Double reallocationRate;

    /**
     * 护盾硬化属性
     */
    @ExcelColumn
    private ShieldHardening shieldHardening;

    /**
     * 物理伤害抗性
     */
    @ExcelColumn
    private Double physicalResistanceMin;

    @ExcelColumn
    private Double physicalResistanceMax;

    /**
     * 能量伤害抗性
     */
    @ExcelColumn
    private Double energyResistanceMin;
    @ExcelColumn
    private Double energyResistanceMax;

    /**
     * 干扰伤害抗性
     */
    @ExcelColumn
    private Double distortionResistanceMin;
    @ExcelColumn
    private Double distortionResistanceMax;

    /**
     * 热伤害抗性
     */
    @ExcelColumn
    private Double thermalResistanceMin;
    @ExcelColumn
    private Double thermalResistanceMax;

    /**
     * 生化伤害抗性
     */
    @ExcelColumn
    private Double biochemicalResistanceMin;
    @ExcelColumn
    private Double biochemicalResistanceMax;

    /**
     * 冲撞伤害抗性
     */
    @ExcelColumn
    private Double stunResistanceMin;
    @ExcelColumn
    private Double stunResistanceMax;
    /**
     * 物理伤害吸收率
     */
    @ExcelColumn
    private Double physicalAbsorptionMin;
    @ExcelColumn
    private Double physicalAbsorptionMax;

    /**
     * 能量伤害吸收率
     */
    @ExcelColumn
    private Double energyAbsorptionMin;
    @ExcelColumn
    private Double energyAbsorptionMax;

    /**
     * 干扰伤害吸收率
     */
    @ExcelColumn
    private Double distortionAbsorptionMin;
    @ExcelColumn
    private Double distortionAbsorptionMax;

    /**
     * 热伤害吸收率
     */
    @ExcelColumn
    private Double thermalAbsorptionMin;
    @ExcelColumn
    private Double thermalAbsorptionMax;

    /**
     * 生化伤害吸收率
     */
    @ExcelColumn
    private Double biochemicalAbsorptionMin;
    @ExcelColumn
    private Double biochemicalAbsorptionMax;

    /**
     * 冲撞伤害吸收率
     */
    @ExcelColumn
    private Double stunAbsorptionMin;
    @ExcelColumn
    private Double stunAbsorptionMax;

}
