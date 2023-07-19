package tools.starcitizen.reader.ship;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import tools.starcitizen.entity.ComponentEntity;
import tools.starcitizen.reader.common.AttachableComponentReader;
import tools.starcitizen.reader.common.StarCitizenReader;

import java.io.File;

/**
 * @Author: wftank
 * @Date: 2020/10/4
 * @Description: 组件通用解析
 */
@Slf4j
public abstract class ComponentXmlReader<T extends ComponentEntity> extends StarCitizenReader<T> {

    private AttachableComponentReader attachableReader = new AttachableComponentReader();

    public void readFile(File file, ComponentEntity entity){
        SAXReader reader = new SAXReader();
        try {
            Document doc = reader.read(file);
            super.readFile(file,entity);
            //可扩展属性
            Node attachableNode = doc.selectSingleNode("//SAttachableComponentParams");
            entity.setAttachableParams(attachableReader.read(attachableNode));

        } catch (DocumentException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }
}
