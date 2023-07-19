package tools.starcitizen.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author: wftank
 * @Date: 2020/10/5
 * @Description: 星际公民通用实体
 */
@Getter
@Setter
@ToString
public class StarCitizenEntity {
    /**
     * 引用键
     */
    private String ref;

    private String fileName;
}
