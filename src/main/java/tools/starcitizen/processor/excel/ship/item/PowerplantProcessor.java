package tools.starcitizen.processor.excel.ship.item;

import lombok.extern.slf4j.Slf4j;
import tools.starcitizen.context.LocalizationContext;
import tools.starcitizen.converter.ship.item.PowerplantConverter;
import tools.starcitizen.entity.ship.item.powerplant.PowerplantEntity;
import tools.starcitizen.entity.vo.ship.item.PowerplantVO;
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
public class PowerplantProcessor extends AbstractExcelProcessor<PowerplantEntity> {

    private String defaultSheetName;
    private PowerplantConverter converter = new PowerplantConverter();

    public PowerplantProcessor(String defaultSheetName) {
        this.defaultSheetName = defaultSheetName;
    }


    @Override
    public List<PowerplantVO> convertData(Map<String, PowerplantEntity> data) {
        List<PowerplantVO> convertData = new ArrayList<>(data.size());
        data.forEach( (ref,entity) -> {
            PowerplantVO vo = converter.convert(entity);
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
    public String getTypeName(Map<String, PowerplantEntity> data) {
        PowerplantEntity PowerplantEntity = data.get(data.keySet().iterator().next());
        String displayTypeLocalKey
                = PowerplantEntity.getItemPurchasableParams().getDisplayTypeLocalKey();
        String typeName = LocalizationContext.getMixed(displayTypeLocalKey.substring(1));
        return typeName == null ? this.defaultSheetName : typeName;
    }

}
