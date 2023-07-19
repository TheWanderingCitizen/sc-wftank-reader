package tools.starcitizen.util;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @Author: wftank
 * @Date: 2020/10/6
 * @Description: 反射工具类
 */
public class ReflectionUtil {

    public static  Set<Class<?>> scanByTypeAnnotation(String path, Class<? extends Annotation> clazz) {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(path))
                .setScanners(new SubTypesScanner(),
                        new TypeAnnotationsScanner())
                .filterInputsBy(new FilterBuilder().includePackage(path)));
        return reflections.getTypesAnnotatedWith(clazz);
    }

    public static  Set<Class<?>> scanBySubTypesOf(String path, Class clazz) {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(path))
                .setScanners(new SubTypesScanner().filterResultsBy(className -> true),
                        new TypeAnnotationsScanner())
                .filterInputsBy(new FilterBuilder().includePackage(path)));
        return reflections.getSubTypesOf(clazz);
    }

    public static List<Field> getAllFields(Class tailClazz){
        Class clazz = tailClazz;
        List<Field> fieldList = new ArrayList<>();
        while (clazz != Object.class){
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        return fieldList;
    }
}
