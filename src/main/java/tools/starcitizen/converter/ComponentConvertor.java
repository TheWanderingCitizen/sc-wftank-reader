package tools.starcitizen.converter;

import tools.starcitizen.context.LocalizationContext;
import tools.starcitizen.context.ManufacturerContext;
import tools.starcitizen.entity.ManufacturerEntity;
import tools.starcitizen.entity.common.LocalizationParams;
import tools.starcitizen.entity.vo.ComponentVO;

/**
 * @Author: wftank
 * @Date: 2020/10/7
 * @Description:
 */
public class ComponentConvertor {

    public static void convert(ComponentVO vo){
        LocalizationParams localizationParams =
                vo.getAttachableParams().getLocalizationParams();
        String nameKey = localizationParams.getName().substring(1);
        String desKey = localizationParams.getDescription().substring(1);
        String manufacturerRef  = vo.getAttachableParams().getManufacturer();
        vo.setName(LocalizationContext.get(nameKey));
        vo.setNameCn(LocalizationContext.getCn(nameKey));
        vo.setDescription(LocalizationContext.get(desKey));
        vo.setDescriptionCn(LocalizationContext.getCn(desKey));
        ManufacturerEntity ma = ManufacturerContext.getEntity(manufacturerRef);
        if (ma != null){
            vo.setManufacturer(LocalizationContext.get(ma.getNameLocalKey().substring(1)));
            vo.setManufacturerCn(LocalizationContext.getCn(ma.getNameLocalKey().substring(1)));
        }else{
            vo.setManufacturer("none");
            vo.setManufacturerCn("无");
        }

    }

    public static boolean isKey(String manufacturer) {
        String[] split = manufacturer.split("-");
        if (split.length != 5) return false;
        if (split[0].length() != 8) return false;
        if (split[1].length() != 4) return false;
        if (split[2].length() != 4) return false;
        if (split[3].length() != 4) return false;
        if (split[4].length() != 12) return false;
        return true;
    }

    public static boolean isRef(String name) {
        return name.startsWith("@");
    }

    ;
}
