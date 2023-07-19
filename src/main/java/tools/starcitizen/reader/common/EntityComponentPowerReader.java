package tools.starcitizen.reader.common;

import lombok.extern.slf4j.Slf4j;
import org.dom4j.Element;
import org.dom4j.Node;
import tools.starcitizen.entity.common.EntityComponentPowerParams;
import tools.starcitizen.util.PropertiesReadUtil;

/**
 * @Author: wftank
 * @Date: 2020/10/4
 * @Description: 电力相关信息解析
 */
@Slf4j
public class EntityComponentPowerReader {

    public EntityComponentPowerParams read(Node node) {
        EntityComponentPowerParams params = new EntityComponentPowerParams();
        Element ele = (Element)node;
        PropertiesReadUtil.readSameNameProperties(ele,params);
        return params;
    }

}
