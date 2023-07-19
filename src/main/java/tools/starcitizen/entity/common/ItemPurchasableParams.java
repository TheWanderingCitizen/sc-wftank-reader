package tools.starcitizen.entity.common;

import lombok.Data;

/**
 * @Author: wftank
 * @Date: 2020/10/4
 * @Description: 物品交易相关属性
 */
@Data
public class ItemPurchasableParams {

    /**
     * 商店显示名称（本地化key）
     */
    private String displayNameLocalKey;
    /**
     * 商店显示类型（本地化key）
     */
    private String displayTypeLocalKey;

}
