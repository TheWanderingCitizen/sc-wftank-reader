package tools.starcitizen.entity.ship.item.cooler;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tools.starcitizen.entity.ship.item.ShipItemEntity;

/**
 * @Author: wftank
 * @Date: 2020/10/4
 * @Description: 冷却器属性
 */
@Getter
@Setter
@ToString(callSuper = true)
public class CoolerEntity extends ShipItemEntity {

    /**
     * 散热量
     */
    private Integer coolingRate;
    /**
     * 抑制红外因子
     */
    private Double suppressionIRFactor;
    /**
     * 抑制热量因子
     */
    private Double suppressionHeatFactor;

}
