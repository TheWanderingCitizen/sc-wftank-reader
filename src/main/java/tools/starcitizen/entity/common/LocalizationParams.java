package tools.starcitizen.entity.common;

import lombok.Data;
import tools.starcitizen.processor.excel.ExcelPrefix;

/**
 * @Author: wftank
 * @Date: 2020/10/4
 * @Description:
 */
@Data
@ExcelPrefix("localizationParams")
public class LocalizationParams {

    /**
     * 组件名
     */
    private String name;
    /**
     * 组件描述
     */
    private String description;

}
