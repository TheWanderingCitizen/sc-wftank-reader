package tools.starcitizen.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tools.starcitizen.entity.common.AttachableComponentParams;

/**
 * @Author: wftank
 * @Date: 2020/10/5
 * @Description: 组件通用实体
 */
@Getter
@Setter
@ToString(callSuper = true)
public class ComponentEntity extends StarCitizenEntity {
    private AttachableComponentParams attachableParams;
}
