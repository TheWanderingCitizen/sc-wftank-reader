package tools.starcitizen.entity.ship.item.shield;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tools.starcitizen.entity.ship.item.ShipItemEntity;

/**
 * @Author: wftank
 * @Date: 2020/10/4
 * @Description: 护盾属性
 */
@Getter
@Setter
@ToString(callSuper = true)
public class ShieldGeneratorEntity extends ShipItemEntity {

    /**
     * 护盾最大生命值
     */
    private Double maxShieldHealth;
    /**
     * 护盾最大充能速度
     */
    private Double maxShieldRegen;
    /**
     * 衰减系数
     */
    private Double decayRatio;

    /**
     * 受伤充能延迟
     */
    private Double damagedRegenDelay;

    /**
     * 最大重分配系数
     */
    private Double maxReallocation;

    /**
     * 重分配速率
     */
    private Double reallocationRate;

    /**
     * 护盾硬化属性
     */
    private ShieldHardening shieldHardening;

    /**
     * 物理伤害抗性
     */
    private ShieldResistance physicalResistance;

    /**
     * 能量伤害抗性
     */
    private ShieldResistance energyResistance;

    /**
     * 干扰伤害抗性
     */
    private ShieldResistance distortionResistance;

    /**
     * 热伤害抗性
     */
    private ShieldResistance thermalResistance;

    /**
     * 生化伤害抗性
     */
    private ShieldResistance biochemicalResistance;

    /**
     * 冲撞伤害抗性
     */
    private ShieldResistance stunResistance;
    /**
     * 物理伤害吸收率
     */
    private ShieldAbsorption physicalAbsorption;

    /**
     * 能量伤害吸收率
     */
    private ShieldAbsorption energyAbsorption;

    /**
     * 干扰伤害吸收率
     */
    private ShieldAbsorption distortionAbsorption;

    /**
     * 热伤害吸收率
     */
    private ShieldAbsorption thermalAbsorption;

    /**
     * 生化伤害吸收率
     */
    private ShieldAbsorption biochemicalAbsorption;

    /**
     * 冲撞伤害吸收率
     */
    private ShieldAbsorption stunAbsorption;

}
