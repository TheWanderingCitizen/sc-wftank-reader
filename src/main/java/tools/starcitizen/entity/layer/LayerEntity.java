package tools.starcitizen.entity.layer;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tools.starcitizen.entity.StarCitizenEntity;

/**
 * @Author: jiawei
 * @Date: 2020/10/7
 * @Description:
 */
@Getter
@Setter
@ToString
public class LayerEntity extends StarCitizenEntity {

    private String iD;
    private String defaultValue;
}
