package tools.starcitizen.entity.system;

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
public class PlatformEntity extends StarCitizenEntity {

    private String iD;
    private String name;
}
