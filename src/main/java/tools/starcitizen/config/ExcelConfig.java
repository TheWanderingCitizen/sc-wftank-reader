package tools.starcitizen.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import tools.starcitizen.processor.excel.ExcelColumn;
import tools.starcitizen.processor.excel.ExcelPrefix;
import tools.starcitizen.util.ReflectionUtil;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

/**
 * @Author: wftank
 * @Date: 2020/10/5
 * @Description: excel列名配置
 */
@Slf4j
public class ExcelConfig {

    private static final Properties COLUMN_PROPERTIES = new Properties();

    public static final String CONFIG_PATH = "./excel_config.txt";

    public static void init(){
        //检查有没有配置文件，没有生成默认的
        File file = new File(CONFIG_PATH);
        writeDefaultExcelConfigFile(file);
        //读取配置文件
        readConfigFile(file);
    }



    public static String getPrefix(Class clazz){
        ExcelPrefix anno = (ExcelPrefix)clazz.getAnnotation(ExcelPrefix.class);
        if (anno == null){
            return null;
        }else{
            return anno.value();
        }
    }

    public static String get(String key){
        return COLUMN_PROPERTIES.getProperty(key);
    }
    public static String getOrDefault(String key,String defaultVal){
        return COLUMN_PROPERTIES.getProperty(key,defaultVal);
    }
    private static void writeDefaultExcelConfigFile(File file) {
        if (!file.exists()){
            try {
                //扫描项目下所有打了注解的类
                Set<Class<?>> classes =
                        ReflectionUtil.scanByTypeAnnotation("tools.starcitizen", ExcelPrefix.class);
                if (ObjectUtils.isNotEmpty(classes)){
                    file.createNewFile();
                    OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
                    Iterator<Class<?>> iterator = classes.iterator();
                    //遍历所有类，生成默认的配置文件
                    while (iterator.hasNext()){
                        Class<?> clazz = iterator.next();
                        String prefix = ExcelConfig.getPrefix(clazz);
                        Field[] fields = clazz.getDeclaredFields();
                        osw.write("#"+prefix+System.lineSeparator());
                        for (int i = 0; i < fields.length; i++) {
                            if (fields[i].isAnnotationPresent(ExcelColumn.class)
                                    &&( ClassUtils.isPrimitiveOrWrapper(fields[i].getType())
                                    || fields[i].getType().isAssignableFrom(String.class))){
                                String key = prefix + "."+fields[i].getName();
                                String value = fields[i].getName();
                                osw.write(key+"="+10+":"+value+System.lineSeparator());
                            }
                        }
                        osw.write(System.lineSeparator());
                    }
                    osw.close();
                }

            }catch (Exception e){
                log.error("Excel执行器初始化异常，异常详情;{}",ExceptionUtils.getStackTrace(e));
            }
        }

    }
    private static void readConfigFile(File file) {
        InputStreamReader is = null;
        try {
            is = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
            COLUMN_PROPERTIES.load(is);
        } catch (Exception e) {
            log.info("加载Excel配置文件出现异常，请确认配置文件存在!,异常详情：{}");
        }finally {
            try {
                is.close();
            } catch (IOException e) {
                log.error(ExceptionUtils.getStackTrace(e));
            }
        }
    }




}
