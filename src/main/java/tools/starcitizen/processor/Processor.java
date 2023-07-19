package tools.starcitizen.processor;

import tools.starcitizen.entity.StarCitizenEntity;

import java.util.Map;

/**
 * @Author: wftank
 * @Date: 2020/10/5
 * @Description: 解析结果处理器
 */
public interface Processor {

    void process(Map<Class,Map<String, StarCitizenEntity>> data);

}
