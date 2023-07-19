package tools.starcitizen.reader.ship.item;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import tools.starcitizen.entity.ship.item.ShipItemEntity;
import tools.starcitizen.reader.common.*;
import tools.starcitizen.reader.ship.ComponentXmlReader;

import java.io.File;

/**
 * @Author: wftank
 * @Date: 2020/10/4
 * @Description:
 */
@Slf4j
public abstract class ShipItemXmlReader<T extends ShipItemEntity> extends ComponentXmlReader<T> {
    protected String baseDir;
    public static final String GAME_DCB_PATH = "/Game.dcb";
    public static final String SHIP_ITEM_PATH = "/libs/foundry/records/entities/scitem/ships";

    private AttachableComponentReader attachableReader = new AttachableComponentReader();
    private EntityPhysicsControllerReader physicsReader = new EntityPhysicsControllerReader();
    private ItemPurchasableReader itemPurchasableReader = new ItemPurchasableReader();
    private HealthComponentReader healthReader = new HealthComponentReader();
    private EntityComponentPowerReader powerReader = new EntityComponentPowerReader();
    private EntityComponentHeatReader heatReader = new EntityComponentHeatReader();
    private DegradationReader degradationReader = new DegradationReader();
    private DistortionReader distortionReader = new DistortionReader();

    public ShipItemXmlReader(String baseDir) {
        this.baseDir = baseDir+SHIP_ITEM_PATH;
    }

    public void readFile(File file, ShipItemEntity entity){
        SAXReader reader = new SAXReader();
        try {
            Document shipItemDoc = reader.read(file);
            super.readFile(file,entity);
            //可扩展属性
            Node attachableNode = shipItemDoc.selectSingleNode("//SAttachableComponentParams");
            if (attachableNode != null) entity.setAttachableParams(attachableReader.read(attachableNode));

            //物理属性
            Node physicsNode = shipItemDoc.selectSingleNode("//SEntityPhysicsControllerParams");
            if (physicsNode != null) entity.setPhysicsControllerParams(physicsReader.read(physicsNode));

            //物品交易属性
            Node itemPurchasableNode = shipItemDoc.selectSingleNode("//SCItemPurchasableParams");
            if (itemPurchasableNode != null) entity.setItemPurchasableParams(itemPurchasableReader.read(itemPurchasableNode));

            //耐久相关属性
            Node healthNode = shipItemDoc.selectSingleNode("//SHealthComponentParams");
            if (healthNode != null) entity.setHealthParams(healthReader.read(healthNode));

            //电力属性
            Node powerNode = shipItemDoc.selectSingleNode("//EntityComponentPowerConnection");
            if (powerNode != null) entity.setPowerParams(powerReader.read(powerNode));

            //热属性
            Node heatNode = shipItemDoc.selectSingleNode("//EntityComponentHeatConnection");
            if (heatNode != null) entity.setHeatParams(heatReader.read(heatNode));

            //老化属性
            Node degradationNode = shipItemDoc.selectSingleNode("//SDegradationParams");
            if (degradationNode != null) entity.setDegradationParams(degradationReader.read(degradationNode));

            //热属性
            Node distortionNode = shipItemDoc.selectSingleNode("//SDistortionParams");
            if (distortionNode != null) entity.setDistortionParams(distortionReader.read(distortionNode));

        } catch (DocumentException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }
}
