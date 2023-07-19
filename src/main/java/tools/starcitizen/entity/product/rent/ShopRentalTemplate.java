package tools.starcitizen.entity.product.rent;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author: jiawei
 * @Date: 2020/10/20
 * @Description: 飞船租用价格模板
 */
@Getter
@Setter
@ToString
public class ShopRentalTemplate {
    //模板id
    private String iD;
    private String name;
    //租赁价格百分比
    private Double percentageOfSalePrice;
    //租赁天数
    private Integer rentalDuration;


}
