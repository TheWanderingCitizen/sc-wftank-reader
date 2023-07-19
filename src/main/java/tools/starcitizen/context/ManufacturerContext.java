package tools.starcitizen.context;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import tools.starcitizen.config.SourceConfig;
import tools.starcitizen.entity.ManufacturerEntity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: wftank
 * @Date: 2020/10/5
 * @Description: 品牌缓存
 */
@Slf4j
public class ManufacturerContext {
    public static final String BASE_PATH= SourceConfig.BATH_PATH;

    private static final String GLOBAL_PATH = "/Libs/Foundry/Records/scitemmanufacturer";

    private static final Map<String,ManufacturerEntity> manufacturerMap = new HashMap<>();
    private static final SAXReader reader = new SAXReader();

    static {
        init();
    }

    public static void init(){
        //品牌所在目录
        File dir = new File(BASE_PATH+GLOBAL_PATH);
        resolve(dir);
    }

    private static void resolve(File dir) {
        File[] files = dir.listFiles();
        if (ObjectUtils.isNotEmpty(files)){
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()){
                    resolve(files[i]);
                }else{
                    File file = files[i];
                    try {
                        ManufacturerEntity entity = new ManufacturerEntity();
                        Document doc = reader.read(file);
                        String ref = doc.getRootElement().attributeValue("__ref");
                        entity.setRef(ref);
                        Element localEle = (Element)doc.selectSingleNode("//Localization");
                        entity.setNameLocalKey(localEle.attributeValue("Name"));
                        entity.setDesLocalKey(localEle.attributeValue("Description"));
                        manufacturerMap.put(ref,entity);
                    } catch (Exception e) {
                        log.error("解析制造商时出错，详情：{}", ExceptionUtils.getStackTrace(e));
                    }
                }
            }
        }

    }

    public static ManufacturerEntity getEntity(String ref){
        return manufacturerMap.get(ref);
    }


}
