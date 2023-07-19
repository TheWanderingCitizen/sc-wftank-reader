package tools.starcitizen.reader.common;

import org.dom4j.Element;
import org.dom4j.Node;
import tools.starcitizen.entity.common.EntityPhysicsControllerParams;

/**
 * @Author: wftank
 * @Date: 2020/10/4
 * @Description: 物理信息解析
 */
public class EntityPhysicsControllerReader {

    public EntityPhysicsControllerParams read(Node node) {
        EntityPhysicsControllerParams params = new EntityPhysicsControllerParams();
        Element entityRigidPhysicsControllerEle = (Element)node.selectSingleNode("//PhysType/SEntityRigidPhysicsControllerParams");
        params.setMass(Double.valueOf(entityRigidPhysicsControllerEle.attributeValue("Mass")));
        return params;
    }
}
