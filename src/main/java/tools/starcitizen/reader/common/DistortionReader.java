package tools.starcitizen.reader.common;

import lombok.extern.slf4j.Slf4j;
import org.dom4j.Element;
import org.dom4j.Node;
import tools.starcitizen.entity.common.DistortionParams;
import tools.starcitizen.util.PropertiesReadUtil;

/**
 * @Author: wftank
 * @Date: 2020/10/4
 * @Description: 干扰相关属性解析
 */
@Slf4j
public class DistortionReader {

    public DistortionParams read(Node node) {
        DistortionParams params = new DistortionParams();
        Element ele = (Element)node;
        PropertiesReadUtil.readSameNameProperties(ele,params);
        return params;
    }

}
