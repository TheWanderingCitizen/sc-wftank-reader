package tools.starcitizen.processor.excel.ship.item;

import lombok.extern.slf4j.Slf4j;
import tools.starcitizen.context.LocalizationContext;
import tools.starcitizen.converter.ship.item.CoolerConverter;
import tools.starcitizen.entity.ship.item.cooler.CoolerEntity;
import tools.starcitizen.entity.vo.ship.item.CoolerVO;
import tools.starcitizen.processor.excel.AbstractExcelProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: wftank
 * @Date: 2020/10/5
 * @Description: 冷却器导出Excel
 */
@Slf4j
public class CoolerExcelProcessor extends AbstractExcelProcessor<CoolerEntity> {

    private String defaultSheetName;
    private CoolerConverter converter = new CoolerConverter();

    public CoolerExcelProcessor(String defaultSheetName) {
        this.defaultSheetName = defaultSheetName;
    }


    @Override
    public List<CoolerVO> convertData(Map<String, CoolerEntity> data) {
        List<CoolerVO> convertData = new ArrayList<>(data.size());
        data.forEach( (ref,entity) -> {
            CoolerVO vo = converter.convert(entity);
            convertData.add(vo);
        });
        return convertData;
    }

    /**
     * 从数据中随便拿一个实体获取类型名
     * @author jw
     * @date 2020/10/5
     * @param data
     * @return java.lang.String
     */
    @Override
    public String getTypeName(Map<String, CoolerEntity> data) {
        CoolerEntity coolerEntity = data.get(data.keySet().iterator().next());
        String displayTypeLocalKey
                = coolerEntity.getItemPurchasableParams().getDisplayTypeLocalKey();
        String typeName = LocalizationContext.getMixed(displayTypeLocalKey.substring(1));
        return typeName == null ? this.defaultSheetName : typeName;
    }

}
