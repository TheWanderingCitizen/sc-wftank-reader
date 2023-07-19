package tools.starcitizen.context;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.lang3.exception.ExceptionUtils;
import tools.starcitizen.config.SourceConfig;
import tools.starcitizen.util.FileUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @Author: wftank
 * @Date: 2020/10/5
 * @Description: 本地化文件缓存
 */
@Slf4j
public class LocalizationContext {
    public static final String BASE_PATH= SourceConfig.BATH_PATH;

    private static final String GLOBAL_FILE_PATH = "/Localization/english/global.ini";
    private static final String GLOBAL_CN_FILE_PATH = "/Localization/chineses/global.ini";

    private static final Map<String,String> localMap;
    private static final Map<String,String> localCnMap;

    static {
        BufferedReader br = null;
        try {
            //英文
            File file = new File(BASE_PATH+GLOBAL_FILE_PATH);
            //通过行数初始化map
            localMap = new CaseInsensitiveMap<>(FileUtil.countLines(file.getAbsolutePath()));
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            //按行读文件
            br = new BufferedReader(new InputStreamReader(bis, StandardCharsets.UTF_8));
            String line = "";
            while ((line = br.readLine()) != null){
                int index = line.indexOf("=");
                String key = line.substring(0,index);
                String value = line.length()-1 == index ? "" : line.substring(index+1);
                localMap.put(key, value);
            }
            //中文
            File cnFile = new File(BASE_PATH+GLOBAL_CN_FILE_PATH);
            //通过行数初始化map
            localCnMap = new CaseInsensitiveMap<>(FileUtil.countLines(cnFile.getAbsolutePath()));
            BufferedInputStream cnBis = new BufferedInputStream(new FileInputStream(cnFile));
            //按行读文件
            br = new BufferedReader(new InputStreamReader(cnBis, StandardCharsets.UTF_8));
            String cnLine = "";
            while ((cnLine = br.readLine()) != null){
                int index = cnLine.indexOf("=");
                String key = cnLine.substring(0,index);
                String value = cnLine.length()-1 == index ? "" : cnLine.substring(index+1);
                localCnMap.put(key, value);
            }
        }catch (Exception e){
            throw new RuntimeException("初始化本地文件异常",e);
        }finally {
            try {
                br.close();
            } catch (IOException e) {
                log.error(ExceptionUtils.getStackTrace(e));
            }
        }
    }

    public static String get(String key){
        return localMap.getOrDefault(key,"");
    }
    public static String getCn(String key){
        return localCnMap.getOrDefault(key,"");
    }

    public static String getMixed(String key){
        return getCn(key)+"（"+get(key)+"）";
    }
}
