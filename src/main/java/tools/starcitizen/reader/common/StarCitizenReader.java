package tools.starcitizen.reader.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import tools.starcitizen.entity.StarCitizenEntity;
import tools.starcitizen.reader.XmlReader;

import java.io.File;

/**
 * @Author: wftank
 * @Date: 2020/10/4
 * @Description: 组件通用解析
 */
@Slf4j
public abstract class StarCitizenReader<T extends StarCitizenEntity> implements XmlReader<T> {

    public void readFile(File file, StarCitizenEntity entity){
        SAXReader reader = new SAXReader();
        try {
            Document doc = reader.read(file);
            Element rootEle = doc.getRootElement();
            //该组件的引用
            entity.setRef(rootEle.attributeValue("__ref"));
            entity.setFileName(file.getName());
        } catch (DocumentException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }
}
