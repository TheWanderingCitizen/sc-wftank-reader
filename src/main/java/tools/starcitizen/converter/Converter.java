package tools.starcitizen.converter;

/**
 * @Author jiawei
 * @Date 2020/10/5
 */
public interface Converter<T> {

    T convert(Object source);

}
