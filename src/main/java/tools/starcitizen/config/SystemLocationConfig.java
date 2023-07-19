package tools.starcitizen.config;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * @Author: wftank
 * @Date: 2020/10/5
 * @Description: 位置信息本地化配置
 */
@Slf4j
public class SystemLocationConfig {

    public static final Properties EN = new Properties();
    public static final Properties CN = new Properties();

    public static final String EN_CONFIG_PATH = "./system_location_en.txt";
    public static final String CN_CONFIG_PATH = "./system_location_cn.txt";

    public static volatile boolean enFlag = false;
    public static volatile boolean cnFlag = false;
    public static volatile boolean initFlag = false;

    public static synchronized void init(){
        if (!initFlag){
            //检查有没有配置文件
            File enFile = new File(EN_CONFIG_PATH);
            File cnFile = new File(CN_CONFIG_PATH);
            //读取配置文件
            enFlag = readConfigFile(EN,enFile);
            cnFlag = readConfigFile(CN,cnFile);
            initFlag = true;
        }


    }

    private static boolean readConfigFile(Properties properties, File file) {
        if (file.exists()){
            try (InputStreamReader is = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)){
                properties.load(is);
            } catch (Exception e) {
                log.info("加载星系位置配置文件出现异常，请确认配置文件存在!,异常详情：{}");
            }
        }
        return file.exists();
    }




}
