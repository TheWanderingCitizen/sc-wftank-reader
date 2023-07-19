package tools.starcitizen.entity.common;

import lombok.Data;
import tools.starcitizen.processor.excel.ExcelColumn;
import tools.starcitizen.processor.excel.ExcelPrefix;

/**
 * @Author: wftank
 * @Date: 2020/10/4
 * @Description: 物理控制相关属性
 */
@Data
@ExcelPrefix("physicsControllerParams")
public class EntityPhysicsControllerParams {

    /**
     * 质量
     */
    @ExcelColumn
    private Double mass;

}
