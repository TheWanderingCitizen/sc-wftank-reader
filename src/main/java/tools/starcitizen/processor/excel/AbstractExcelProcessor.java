package tools.starcitizen.processor.excel;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import tools.starcitizen.config.ExcelConfig;
import tools.starcitizen.entity.StarCitizenEntity;
import tools.starcitizen.util.ExcelUtil;
import tools.starcitizen.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.RecursiveTask;

/**
 * @Author: wftank
 * @Date: 2020/10/7
 * @Description: 通用Excel生成器
 */
@Slf4j
public abstract class AbstractExcelProcessor<T extends StarCitizenEntity> implements ExcelProcessor<T> {


    //将map转换为list
    public abstract List convertData(Map<String, T> data);
    //获取转换的数据的类型
    public abstract String getTypeName(Map<String, T> data);

    @Override
    public void process(Map<String, T> data, Workbook workbook) {
        //随便拿一个，用来获取模块类型作为sheet的名称
        String typeName = getTypeName(data);
        Sheet sheet = workbook.createSheet(typeName);
        List voList = convertData(data);
        writeSheet(sheet,voList);
        //自适应列宽
    }

    /**
     * 向sheet中写数据
     * @author jw
     * @date 2020/10/7
     * @param sheet
     * @param voList
     * @return void
     */
    private void writeSheet(Sheet sheet, List voList) {
        //获取要转换的类在配置文件中的配置前缀
        //获取vo类的所有属性
        List<Field> excelColumnFields = new ArrayList<>();
        scanFields(voList.get(0).getClass(),excelColumnFields);
        //对标题列按照注解顺序排序(数值越小越靠前)
        Collections.sort(excelColumnFields,
                Comparator.comparingInt(field -> {
                    //配置文件中的key
                    ExcelPrefix anno = field.getDeclaringClass().getAnnotation(ExcelPrefix.class);
                    String prefix = anno.value();
                    String key = prefix + "." + field.getName();
                    //获取配置文件中的映射
                    String config = ExcelConfig.get(key);
                    String[] split = config.split(":");
                    //冒号左边是顺序配置
                    return Integer.valueOf(split[0]);
                }));

        //至少有一个属性需要展示时才会有内容
        if (excelColumnFields.size() > 0){
            //写标题行
            Row titleRow = sheet.createRow(0);
            for (int i = 0; i < excelColumnFields.size(); i++) {
                Cell cell = titleRow.createCell(i);
                //属性
                Field field = excelColumnFields.get(i);
                //配置文件中的key
                ExcelPrefix anno = field.getDeclaringClass().getAnnotation(ExcelPrefix.class);
                String prefix = anno.value();
                String key = prefix + "." + field.getName();
                //获取配置文件中的映射
                String config = ExcelConfig.get(key);
                String[] split = config.split(":");
                cell.setCellValue(split[1]);
            }
            ExcelUtil.setSizeColumn(sheet,1);
            //将内容写入map,
            List<Map<Field,Object>> dataMapList = new ArrayList<>(voList.size());
            voToFieldMap(dataMapList,voList);
            ListIterator<Map<Field, Object>> dataIterator = dataMapList.listIterator();
            int index = 1;
            while (dataIterator.hasNext()){
                Row row = sheet.createRow(index);
                Map<Field, Object> rowMap = dataIterator.next();
                for (int j = 0; j < excelColumnFields.size(); j++) {
                    Cell cell = row.createCell(j);
                    Object value = rowMap.get(excelColumnFields.get(j));
                    writeCell(cell, value);
                }
                index++;
            }
//            ForkJoinPool forkJoinPool = new ForkJoinPool();
//            forkJoinPool
//                    .submit(new ExcelWriteTask(dataMapList,excelColumnFields,0,100,sheet)).join();
        }
    }

    public class ExcelWriteTask extends RecursiveTask<Void>{
        private List<Map<Field,Object>> dataMapList;
        private List<Field> excelColumnFields;
        private int start;
        private int limit;
        private Sheet sheet;

        public ExcelWriteTask(List<Map<Field, Object>> dataMapList, List<Field> excelColumnFields,
                              int start, int limit, Sheet sheet) {
            this.dataMapList = dataMapList;
            this.excelColumnFields = excelColumnFields;
            this.start = start;
            this.limit = limit;
            this.sheet = sheet;
        }

        @Override
        protected Void compute() {
            try {
                int end = dataMapList.size() - 1;
                ExcelWriteTask child = null;
                if (start + limit - 1 < end){
                    end = start + limit - 1;
                    child = new ExcelWriteTask(dataMapList, excelColumnFields,
                            end + 1, limit, sheet);
                    child.fork();
                }
                log.info("正在多线程写入数据,此线程正在写{}~{},总共:{}",start+1,end+1,dataMapList.size());
                for (int i = start; i < end+1; i++) {
                    Row row;
                    synchronized (dataMapList){
                        row = sheet.createRow(i+1);
                    }
                    Map<Field, Object> rowMap = dataMapList.get(i);
                    for (int j = 0; j < excelColumnFields.size(); j++) {
                        Cell cell = row.createCell(j);
                        Object value = rowMap.get(excelColumnFields.get(j));
                        writeCell(cell, value);
                    }
                }
                if  (child != null) child.join();
            } catch (Exception e) {
                log.error("写入数据异常,异常详情:{}",ExceptionUtils.getStackTrace(e));
            }
            return null;
        }
    }

    private void voToFieldMap( List<Map<Field,Object>> dataMapList,  List voList) {
        for (int i = 0; i < voList.size(); i++) {
            Map<Field,Object> rowMap = new HashMap<>();
            dataMapList.add(rowMap);
            Object o = voList.get(i);
            reursiveWriteData(rowMap,o);
        }

    }

    private void reursiveWriteData( Map<Field,Object> rowMap, Object o) {
        List<Field> fieldsList = ReflectionUtil.getAllFields(o.getClass());
        ListIterator<Field> iterator = fieldsList.listIterator();
        while (iterator.hasNext()){
            Field field = iterator.next();
            field.setAccessible(true);
            //只添加打了ExcelColumn注解的类
            try {
                if (field.isAnnotationPresent(ExcelColumn.class)){
                    //如果是基本类型、包装类型或字符串，则添加，否则递归
                    if (ClassUtils.isPrimitiveOrWrapper(field.getType())
                            || field.getType().isAssignableFrom(String.class)){
                        rowMap.put(field,field.get(o));
                    }else{
                        Object fieldObj = field.get(o);
                        reursiveWriteData(rowMap,fieldObj);
                    }
                }
            }catch (Exception e){
                log.info(ExceptionUtils.getStackTrace(e));
            }

        }
    }

    private void writeCell(Cell cell, Object value) {
        try {
            if (value != null){
                if (value.getClass().isAssignableFrom(String.class)){
                    cell.setCellValue(value.toString());
                }else{
                    cell.setCellValue(Double.valueOf(value.toString()));
                }
            }else{
                cell.setCellValue("");
            }
        }catch (Exception e){
            log.error("插入数值时出现异常，异常信息:{}", ExceptionUtils.getStackTrace(e));
        }
    }


    /**
     * 扫描类包括父类中所有包含ExcelColumn注解的类
     * @author jw
     * @date 2020/10/6
     * @param clazz
     * @param excelColumnFields
     * @return void
     */
    private void scanFields(Class clazz, List<Field> excelColumnFields) {
        List<Field> fieldsList = ReflectionUtil.getAllFields(clazz);
        ListIterator<Field> iterator = fieldsList.listIterator();
        while (iterator.hasNext()){
            Field field = iterator.next();
            //只添加打了ExcelColumn注解的类
            if (field.isAnnotationPresent(ExcelColumn.class)){
                //如果是基本类型、包装类型或字符串，则添加，否则递归
                if (ClassUtils.isPrimitiveOrWrapper(field.getType())
                        || field.getType().isAssignableFrom(String.class)){
                    field.setAccessible(true);
                    excelColumnFields.add(field);
                }else{
                    scanFields(field.getType(),excelColumnFields);
                }
            }

        }
    }


}
