package tools.starcitizen.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author herokillerJ
 * @Date 2020/3/31
 * @Description
 */
@Slf4j
public class ScanUtil {

    public static final String prefix = new File(Thread.currentThread().getContextClassLoader().getResource("").getPath()).getAbsolutePath()+ File.separator;

    public static List<Class> scanClassByAnnotation(String path, Class annotationClass){

        path = prefix + path.replaceAll("\\.","/");
        File dir = new File(path);

        List<Class> list  = scanAnnotitionClass(dir,annotationClass);
        return list;
    }

    private static List<Class> scanAnnotitionClass(File dir, Class annotationClass){
        List<Class> list = new LinkedList<>();
        if (dir.isDirectory()){
            scanDirectory(dir,list,annotationClass);
        }else{
            putClass2List(dir,list,annotationClass);
        }
        return list;
    }

    private static List scanDirectory(File dir,List<Class> list,Class annotationClass){
        if(list == null) list = new LinkedList();
        if (dir.isDirectory()){
            File[] files = dir.listFiles();
            if (files != null && files.length > 0){
                for (int i = 0; i < files.length; i++) {
                    scanDirectory(files[i],list,annotationClass);
                }
            }
        }else{
            putClass2List(dir,list,annotationClass);
        }
        return list;
    }

    private static void putClass2List(File file,List list,Class annotationClass){
        String path = file.getAbsolutePath();
        path = path.replace(prefix,"");
        path = path.replaceAll("\\\\",".");
        path = path.substring(0,path.lastIndexOf("."));
        try {
            Class clazz = Class.forName(path);
            Annotation annotation = clazz.getAnnotation(annotationClass);
            if (annotation != null) list.add(clazz);
        } catch (ClassNotFoundException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }

}
