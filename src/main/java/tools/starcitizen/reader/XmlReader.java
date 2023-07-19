package tools.starcitizen.reader;

import tools.starcitizen.entity.StarCitizenEntity;

import java.util.Map;

/**
 * @Author jiawei
 * @Date 2020/10/4
 * xml解析器
 */
public interface XmlReader<T extends StarCitizenEntity> {
    Map<Class<T>, Map<String,T>> read();
}
