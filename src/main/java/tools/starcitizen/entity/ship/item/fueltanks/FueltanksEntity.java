package tools.starcitizen.entity.ship.item.fueltanks;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tools.starcitizen.entity.ship.item.ShipItemEntity;

/**
 * @Author: wftank
 * @Date: 2020/10/4
 * @Description: 邮箱属性
 */
@Getter
@Setter
@ToString(callSuper = true)
public class FueltanksEntity extends ShipItemEntity {
    /**
     * 填充速度
     */
    private Double fillRate;
    /**
     * 消耗速度
     */
    private Double drainRate;
    /**
     * 容量
     */
    private Double capacity;
}
