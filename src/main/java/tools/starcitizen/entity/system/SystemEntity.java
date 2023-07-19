package tools.starcitizen.entity.system;

import lombok.Getter;
import lombok.Setter;
import tools.starcitizen.entity.StarCitizenEntity;

import java.util.List;

/**
 * @Author: jiawei
 * @Date: 2020/10/7
 * @Description:
 */
@Getter
@Setter
public class SystemEntity extends StarCitizenEntity {

    private String iD;
    private Integer index;
    private String name;
    private String type;
    private String variableTypeID;
    private String fileName;
    private Double shopMaxPremiumPercentage;
    private Double shopInventory;
    private Position position;
    private Rotation rotation;

    private SystemEntity parent;
    private String currentId;
    private List<SystemEntity> childs;
    private Integer level;
    private PlatformEntity platform;

    public SystemEntity findRoot(){
        if (this.parent == null){
            return this;
        }
        SystemEntity parent = this.parent;
        while (parent.getParent() != null){
            parent = parent.getParent();
        }
        return parent;
    }

    @Override
    public String toString() {
        return "SystemEntity{" +
                "iD='" + iD + '\'' +
                ", index=" + index +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", variableTypeID='" + variableTypeID + '\'' +
                ", fileName='" + fileName + '\'' +
                ", shopMaxPremiumPercentage=" + shopMaxPremiumPercentage +
                ", shopInventory=" + shopInventory +
                ", position=" + position +
                ", rotation=" + rotation +
                ", currentId='" + currentId + '\'' +
                ", level=" + level +
                ", platform=" + platform +
                '}';
    }
}
