package tools.starcitizen.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.dom4j.Element;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.math.BigDecimal;

/**
 * @Author: wftank
 * @Date: 2020/10/5
 * @Description:
 */
@Slf4j
public class PropertiesReadUtil {

    /**
     * 当实体类属性至少有一个为节点的属性时，可用此方法
     * @author jw
     * @date 2020/10/4
     * @param ele
     * @param params
     * @return void
     */
    public static void readSameNameProperties(Element ele, Object params) {
        try {
            //获取类的信息
            BeanInfo bi= Introspector.getBeanInfo(params.getClass(),Object.class);
            //拿到类的所有属性
            PropertyDescriptor[] properties = bi.getPropertyDescriptors();
            for (int i = 0; i < properties.length; i++) {
                PropertyDescriptor property = properties[i];
                String name = property.getName();
                //根据属性名获取属性值
                String value;
                //有的属性名开头小写，有的属性名开头大写，所以这里做个判断
                if ((value = ele.attributeValue(name)) == null){
                    //属性名首字母变大写，因为xml中首字母大写
                    name = name.substring(0,1).toUpperCase()+name.substring(1);
                    value = ele.attributeValue(name);
                }
                if (value != null){
                    //值不为空属性名直接能够匹配节点中的属性
                    autoSetParam(ele, params, property, value);
                }else {
                    /**
                     * 如果value为null，则没有获取到属性值，说明类的属性名和xml中的不一致，
                     * 判断下是不是结尾为able的，因为bollean属性避免is开头，把is去掉了
                     */
                    if(name.endsWith("able")){
                        name = "Is"+name;
                        value = ele.attributeValue(name);
                        autoSetParam(ele, params, property, value);
                    }
                }
            }
        } catch (Exception e) {
            log.info(ExceptionUtils.getStackTrace(e));
        }
    }

    public static void readSameNameProperties(String prefix, Element ele, Object params) {
        try {
            //获取类的信息
            BeanInfo bi= Introspector.getBeanInfo(params.getClass(),Object.class);
            //拿到类的所有属性
            PropertyDescriptor[] properties = bi.getPropertyDescriptors();
            for (int i = 0; i < properties.length; i++) {
                PropertyDescriptor property = properties[i];
                String name = property.getName();
                if (name.startsWith(prefix)){
                    name = name.replace(prefix,"");
                    //根据属性名获取属性值
                    String value;
                    //有的属性名开头小写，有的属性名开头大写，所以这里做个判断
                    if ((value = ele.attributeValue(name)) == null){
                        //属性名首字母变大写，因为xml中首字母大写
                        name = name.substring(0,1).toUpperCase()+name.substring(1);
                        value = ele.attributeValue(name);
                    }
                    if (value != null){
                        //值不为空属性名直接能够匹配节点中的属性
                        autoSetParam(ele, params, property, value);
                    }else {
                        /**
                         * 如果value为null，则没有获取到属性值，说明类的属性名和xml中的不一致，
                         * 判断下是不是结尾为able的，因为bollean属性避免is开头，把is去掉了
                         */
                        if(name.endsWith("able")){
                            name = "Is"+name;
                            value = ele.attributeValue(name);
                            autoSetParam(ele, params, property, value);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.info(ExceptionUtils.getStackTrace(e));
        }
    }


    private static void autoSetParam(Element ele, Object params,
                                     PropertyDescriptor property, String value) {
        try {
            if (Number.class.isAssignableFrom(property.getPropertyType())){
                //如果是数值类型，通过找到valueOf方法直接设置属性值
                Method valueOfMethod = property.getPropertyType().getMethod("valueOf", String.class);
                //因为值存在科学计数法，所以转换成普通数字字符串防止出错
                String plainString = new BigDecimal(value).toPlainString();
                property.getWriteMethod()
                        .invoke(params,valueOfMethod.invoke(params,plainString));
            }else{
                property.getWriteMethod()
                        .invoke(params,value);
            }
        }catch (Exception e){
            log.error("转换类：{} 属性：{} 时出现异常：{}！",
                    params.getClass().getName(),
                    property.getName(),
                    ExceptionUtils.getStackTrace(e));
        }
    }
}
