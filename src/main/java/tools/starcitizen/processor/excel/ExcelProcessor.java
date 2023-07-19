package tools.starcitizen.processor.excel;

import org.apache.poi.ss.usermodel.Workbook;
import tools.starcitizen.entity.StarCitizenEntity;

import java.util.Map;

/**
 * @Author: wftank
 * @Date: 2020/10/5
 * @Description:
 */
public interface ExcelProcessor<T extends StarCitizenEntity> {

    void process(Map<String, T> data, Workbook workbook);
}
