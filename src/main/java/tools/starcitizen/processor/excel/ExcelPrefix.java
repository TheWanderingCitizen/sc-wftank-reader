package tools.starcitizen.processor.excel;

import java.lang.annotation.*;


/**
 * @author wtank
 * @date 2020/10/5
 * 配置文件中的前缀
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface ExcelPrefix {

    String value();
}
