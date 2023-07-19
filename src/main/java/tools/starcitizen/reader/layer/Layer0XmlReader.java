package tools.starcitizen.reader.layer;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import tools.starcitizen.config.SourceConfig;
import tools.starcitizen.entity.layer.LayerEntity;
import tools.starcitizen.reader.common.StarCitizenReader;
import tools.starcitizen.util.PropertiesReadUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: jiawei
 * @Date: 2020/10/7
 * @Description: 读取贸易文件,与普通的组件解析不通用
 */
@Slf4j
public class Layer0XmlReader extends StarCitizenReader<LayerEntity> {

    private static final String BATH_PATH = SourceConfig.BATH_PATH;
    private static final String FILE_PATH = "/Libs/Subsumption/Platforms/PU/System/Stanton/stantonsystem/Layer0.xml";

    @Override
    public Map<Class<LayerEntity>, Map<String, LayerEntity>> read() {
        File file = new File(BATH_PATH + FILE_PATH);
        SAXReader reader = new SAXReader();
        Map<Class<LayerEntity>, Map<String, LayerEntity>> result = new ConcurrentHashMap<>();
        Map<String, LayerEntity> data = new HashMap<>();
        result.put(LayerEntity.class,data);
        try {
            Element rootEle = reader.read(file).getRootElement();
            List<Node> nodes = rootEle.selectNodes("//Variable[@DefaultValue]");
            if (ObjectUtils.isNotEmpty(nodes)){
                for (Node node : nodes) {
                    LayerEntity layerEntity = new LayerEntity();
                    Element element = (Element) node;
                    PropertiesReadUtil.readSameNameProperties(element,layerEntity);
                    data.put(layerEntity.getID(),layerEntity);
                }
            }
        } catch (DocumentException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return result;
    }

    public static void main(String[] args) {
        new Layer0XmlReader().read();
    }

}
