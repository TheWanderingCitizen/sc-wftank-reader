package tools.starcitizen.processor.excel.ship.item;

import lombok.extern.slf4j.Slf4j;
import tools.starcitizen.context.LocalizationContext;
import tools.starcitizen.converter.ship.item.ShieldGeneratorConverter;
import tools.starcitizen.entity.ship.item.shield.ShieldGeneratorEntity;
import tools.starcitizen.entity.vo.ship.item.ShieldGeneratorVO;
import tools.starcitizen.processor.excel.AbstractExcelProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: wftank
 * @Date: 2020/10/5
 * @Description: 护盾导出Excel
 */
@Slf4j
public class ShieldGeneratorExcelProcessor extends AbstractExcelProcessor<ShieldGeneratorEntity> {

    private String defaultSheetName;
    private ShieldGeneratorConverter converter = new ShieldGeneratorConverter();

    public ShieldGeneratorExcelProcessor(String defaultSheetName) {
        this.defaultSheetName = defaultSheetName;
    }


    @Override
    public List<ShieldGeneratorVO> convertData(Map<String, ShieldGeneratorEntity> data) {
        List<ShieldGeneratorVO> convertData = new ArrayList<>(data.size());
        data.forEach( (ref,entity) -> {
            ShieldGeneratorVO vo = converter.convert(entity);
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
    public String getTypeName(Map<String, ShieldGeneratorEntity> data) {
        ShieldGeneratorEntity entity = data.get(data.keySet().iterator().next());
        String displayTypeLocalKey
                = entity.getItemPurchasableParams().getDisplayTypeLocalKey();
        String typeName = LocalizationContext.getMixed(displayTypeLocalKey.substring(1));
        return typeName == null ? this.defaultSheetName : typeName;
    }

}
