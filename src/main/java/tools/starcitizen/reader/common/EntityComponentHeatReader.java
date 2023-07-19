package tools.starcitizen.reader.common;

import lombok.extern.slf4j.Slf4j;
import org.dom4j.Element;
import org.dom4j.Node;
import tools.starcitizen.entity.common.EntityComponentHeatParams;
import tools.starcitizen.util.PropertiesReadUtil;

/**
 * @Author: wftank
 * @Date: 2020/10/4
 * @Description: 热相关属性解析
 */
@Slf4j
public class EntityComponentHeatReader {

    public EntityComponentHeatParams read(Node node) {
        EntityComponentHeatParams params = new EntityComponentHeatParams();
        Element ele = (Element)node;
        PropertiesReadUtil.readSameNameProperties(ele,params);
        return params;
    }

}
