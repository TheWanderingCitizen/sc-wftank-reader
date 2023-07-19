package tools.starcitizen.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * @Author: wftank
 * @Date: 2020/10/7
 * @Description: 加载数据源相关配置
 */
@Slf4j
public class SourceConfig {

    public static final String BATH_PATH;
    private static final String CONFIG_PATH = "./source_config.txt";
    private static final Properties SOURCE_PROPERTIES = new Properties();
    static {
        //检查有没有配置文件，没有生成默认的
        File file = new File(CONFIG_PATH);
        readConfigFile(file);
        BATH_PATH = SOURCE_PROPERTIES.getProperty("base.path");
    }

    private static void readConfigFile(File file) {
        InputStreamReader is = null;
        try {
            is = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
            SOURCE_PROPERTIES.load(is);
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
