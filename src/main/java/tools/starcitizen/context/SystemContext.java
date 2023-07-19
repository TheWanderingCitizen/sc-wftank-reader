package tools.starcitizen.context;

import lombok.extern.slf4j.Slf4j;
import tools.starcitizen.config.SystemLocationConfig;
import tools.starcitizen.entity.layer.LayerEntity;
import tools.starcitizen.entity.system.SystemEntity;
import tools.starcitizen.reader.layer.Layer0XmlReader;
import tools.starcitizen.reader.system.SystemXmlReader;

import java.util.*;

/**
 * @Author: wftank
 * @Date: 2020/10/5
 * @Description: 星系位置信息
 */
@Slf4j
public class SystemContext {

    public static final Map<String, SystemEntity> SYSTEM_MAP = new LinkedHashMap<>();
    public static final Map<String, List<LayerEntity>> LAYER_MAP = new HashMap<>();

    static {
        SystemLocationConfig.init();
        SYSTEM_MAP.putAll(new SystemXmlReader().read().get(SystemEntity.class));
        //key为ID属性,不是DefaultValue
        final Map<String, LayerEntity> keyIdMap = new Layer0XmlReader().read().get(LayerEntity.class);
        keyIdMap.forEach( (id,layer) -> {
            List<LayerEntity> layers = LAYER_MAP.get(layer.getDefaultValue());
            if (layers == null){
                layers = new ArrayList<>();
                LAYER_MAP.put(layer.getDefaultValue(),layers);
            }
            layers.add(layer);
        });
    }
    public static SystemEntity findRoot(){
        SystemEntity value = SYSTEM_MAP.entrySet().iterator().next().getValue();
        return value.findRoot();
    }


    public static List<SystemEntity> getSystemsByLayer(String key){
        List<LayerEntity> layers = LAYER_MAP.get(key);
        if (layers != null){
            List<SystemEntity> systems = new ArrayList<>();
            layers.forEach( layer -> {
                systems.add(SYSTEM_MAP.get(layer.getID()));
            });
            return systems;
        }
        return null;
    }

    public static String appendKey(SystemEntity useEntity) {
        String key = "^"+(useEntity.getLevel()+1)+"["+getSubId(useEntity.getID())+"]"+useEntity.getName();
        if (useEntity.getParent() != null){
            key = appendKey(useEntity.getParent()) + key;
        }
        return key;
    }
    public static String getSubId(String id){
        String subId = id;
        if (id.contains(",")){
            subId = id.substring(id.lastIndexOf(",")+1);
        }
        return subId;
    }

}
