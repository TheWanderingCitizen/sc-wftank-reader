package tools.starcitizen.reader.common;

import org.dom4j.Element;
import org.dom4j.Node;
import tools.starcitizen.entity.common.AttachableComponentParams;
import tools.starcitizen.entity.common.LocalizationParams;
import tools.starcitizen.util.PropertiesReadUtil;

import java.util.Optional;

/**
 * @Author: wftank
 * @Date: 2020/10/4
 * @Description: 可附加组件信息解析（目前就是基本信息）
 */
public class AttachableComponentReader {

    public AttachableComponentParams read(Node node) {
        AttachableComponentParams params = new AttachableComponentParams();
        Element attachEle = (Element)node.selectSingleNode("//AttachDef");
        //可扩展属性
        PropertiesReadUtil.readSameNameProperties(attachEle,params);
        Element localNode = (Element)attachEle.selectSingleNode("//AttachDef/Localization");
        LocalizationParams localizationParams = new LocalizationParams();
        PropertiesReadUtil.readSameNameProperties(localNode,localizationParams);
        params.setLocalizationParams(localizationParams);
        Element cargoEle = (Element)attachEle.selectSingleNode("//inventoryOccupancyVolume/SCentiCargoUnit");
        Optional.ofNullable(cargoEle)
                .ifPresent(ele -> params.setInventoryOccupancy(Integer.valueOf(cargoEle.attributeValue("centiSCU"))));
        return params;
    }
}
