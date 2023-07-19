package tools.starcitizen.reader.common;

import org.dom4j.Element;
import org.dom4j.Node;
import tools.starcitizen.entity.common.DegradationParams;
import tools.starcitizen.util.PropertiesReadUtil;

/**
 * @Author: wftank
 * @Date: 2020/10/4
 * @Description: 寿命相关属性解析
 */
public class DegradationReader {

    public DegradationParams read(Node node) {
        DegradationParams params = new DegradationParams();
        Element ele = (Element)node;
        PropertiesReadUtil.readSameNameProperties(ele,params);
        return params;
    }
}
