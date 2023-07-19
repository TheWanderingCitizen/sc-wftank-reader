package tools.starcitizen.processor.excel;

import java.lang.annotation.*;


/**
 * @author wftank
 * @date 2020/10/5
 * 标记属性的列名
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
public @interface ExcelColumn {

}
