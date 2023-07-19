package tools.starcitizen.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author: wftank
 * @Date: 2020/10/5
 * @Description: 制造商属性
 */
@Getter
@Setter
@ToString(callSuper = true)
public class ManufacturerEntity extends StarCitizenEntity {

    private String nameLocalKey;
    private String desLocalKey;
}
