package tools.starcitizen.processor.excel.ship.item;

import lombok.extern.slf4j.Slf4j;
import tools.starcitizen.context.LocalizationContext;
import tools.starcitizen.converter.ship.item.FueltanksConverter;
import tools.starcitizen.entity.common.ItemPurchasableParams;
import tools.starcitizen.entity.ship.item.fueltanks.FueltanksEntity;
import tools.starcitizen.entity.vo.ship.item.FueltanksVO;
import tools.starcitizen.processor.excel.AbstractExcelProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: wftank
 * @Date: 2020/10/5
 * @Description: 油箱导出Excel
 */
@Slf4j
public class FueltanksProcessor extends AbstractExcelProcessor<FueltanksEntity> {

    private String defaultSheetName;
    private FueltanksConverter converter = new FueltanksConverter();

    public FueltanksProcessor(String defaultSheetName) {
        this.defaultSheetName = defaultSheetName;
    }


    @Override
    public List<FueltanksVO> convertData(Map<String, FueltanksEntity> data) {
        List<FueltanksVO> convertData = new ArrayList<>(data.size());
        data.forEach( (ref,entity) -> {
            FueltanksVO vo = converter.convert(entity);
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
    public String getTypeName(Map<String, FueltanksEntity> data) {
        FueltanksEntity FueltanksEntity = data.get(data.keySet().iterator().next());
        ItemPurchasableParams itemPurchasableParams = FueltanksEntity.getItemPurchasableParams();
        if (itemPurchasableParams == null) return this.defaultSheetName;
        String displayTypeLocalKey = itemPurchasableParams.getDisplayTypeLocalKey();
        String typeName = LocalizationContext.getMixed(displayTypeLocalKey.substring(1));
        return typeName == null ? this.defaultSheetName : typeName;
    }

}
