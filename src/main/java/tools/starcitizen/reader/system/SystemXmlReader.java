package tools.starcitizen.reader.system;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import tools.starcitizen.config.SourceConfig;
import tools.starcitizen.entity.system.PlatformEntity;
import tools.starcitizen.entity.system.Position;
import tools.starcitizen.entity.system.Rotation;
import tools.starcitizen.entity.system.SystemEntity;
import tools.starcitizen.reader.common.StarCitizenReader;
import tools.starcitizen.util.PropertiesReadUtil;

import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: jiawei
 * @Date: 2020/10/7
 * @Description: 读取行星系统
 */
@Slf4j
public class SystemXmlReader extends StarCitizenReader<SystemEntity> {

    private static final String BATH_PATH = SourceConfig.BATH_PATH;
    private static final String FILE_PATH = "/Libs/Subsumption/Platforms/PU/System/Stanton/stantonsystem.xml";


    @Override
    public Map<Class<SystemEntity>, Map<String, SystemEntity>> read() {
        File file = new File(BATH_PATH + FILE_PATH);
        SAXReader reader = new SAXReader();
        Map<Class<SystemEntity>, Map<String, SystemEntity>> result = new HashMap<>();
        ConcurrentHashMap<String, SystemEntity> data = new ConcurrentHashMap<>();
        result.put(SystemEntity.class,data);
        try {
            Element rootEle = reader.read(file).getRootElement();
            Iterator<Element> platformIterator = rootEle.elementIterator("Platform");
            int level = 0 ;
            while (platformIterator.hasNext()) {
                Element platformEle = platformIterator.next();
                PlatformEntity platform = new PlatformEntity();
                PropertiesReadUtil.readSameNameProperties(platformEle,platform);
                Element variablesEle = platformEle.element("Variables");
                Element varibleEle = (Element)variablesEle.selectSingleNode("./Variable[@Index='0']");
                SystemEntity systemEntity = recursiveReadChild(null, varibleEle, level, platform, data);
                data.put(systemEntity.getID(),systemEntity);

            }
        } catch (DocumentException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return result;
    }

    private SystemEntity recursiveReadChild(SystemEntity parent, Element ele, int level,PlatformEntity platform
            , Map<String, SystemEntity> data) {
        SystemEntity systemEntity = null;
        if (ele != null){
            systemEntity = new SystemEntity();
            PropertiesReadUtil.readSameNameProperties(ele,systemEntity);
            String name = systemEntity.getName();
            //去掉没用的名称
            if (name.indexOf("{") > 0){
                name = name.substring(0,name.indexOf("{")-1);
                systemEntity.setName(name);
            }
            //特殊处理id,只拿到属于自己独特的那部分
            if (parent != null){
                systemEntity.setParent(parent);
                String currentId = systemEntity.getID().replace(parent.getID(), "");
                systemEntity.setCurrentId(currentId.substring(1));
            }
            //解析当前节点
            Element positionEle = ele.element("Position");
            Element rotationEle = ele.element("Rotation");
            if (positionEle != null) {
                Position position = new Position();
                PropertiesReadUtil.readSameNameProperties(positionEle,position);
                systemEntity.setPosition(position);
            }
            Rotation rotation = new Rotation();
            if (rotationEle != null) {
                PropertiesReadUtil.readSameNameProperties(rotationEle,rotation);
                systemEntity.setRotation(rotation);
            }
            //设置级别
            systemEntity.setLevel(level);
            //属于哪个系统(星系)
            systemEntity.setPlatform(platform);
            data.put(systemEntity.getID(),systemEntity);
            //解析子节点
            List<Node> childNodes = ele.selectNodes("./Variables/Variable");
            if (ObjectUtils.isNotEmpty(childNodes)){
                List<SystemEntity> childs = new ArrayList<>();
                systemEntity.setChilds(childs);
                level++;
                CompletableFuture<SystemEntity>[] completableFutures = new CompletableFuture[childNodes.size()];
                //截止到多少层时使用多线程
                boolean cocurrentFlag = level < 2;
                if (cocurrentFlag){
                    log.info("共创建:{}个线程解析:{}的子节点",childNodes.size(),systemEntity.getName());
                }
                for (int i = 0; i < childNodes.size(); i++) {
                    Element childEle = (Element)childNodes.get(i);
                    if (cocurrentFlag){
                        SystemEntity finalSystemEntity = systemEntity;
                        int finalLevel = level;
                        completableFutures[i] =
                                CompletableFuture.supplyAsync(
                                        () -> recursiveReadChild(finalSystemEntity, childEle, finalLevel,platform,data));
                    }else{
                        SystemEntity childEntity = recursiveReadChild(systemEntity, childEle,level,platform,data);
                        childs.add(childEntity);
                    }
                }
                if (cocurrentFlag){
                    CompletableFuture.allOf(completableFutures).join();
                    for (int i = 0; i < completableFutures.length; i++) {
                        childs.add(completableFutures[i].getNow(null));
                    }
                }

            }
        }
        return systemEntity;
    }
}
