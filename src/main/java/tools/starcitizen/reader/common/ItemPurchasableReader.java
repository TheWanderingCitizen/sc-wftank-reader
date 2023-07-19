package tools.starcitizen.reader.common;

import org.dom4j.Element;
import org.dom4j.Node;
import tools.starcitizen.entity.common.ItemPurchasableParams;

/**
 * @Author: wftank
 * @Date: 2020/10/4
 * @Description: 物品交易相关属性解析
 */
public class ItemPurchasableReader {

    public ItemPurchasableParams read(Node node) {
        ItemPurchasableParams params = new ItemPurchasableParams();
        Element ele = (Element)node;
        params.setDisplayNameLocalKey(ele.attributeValue("displayName"));
        params.setDisplayTypeLocalKey(ele.attributeValue("displayType"));
        return params;
    }
}
