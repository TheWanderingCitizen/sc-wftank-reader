package tools.starcitizen.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import tools.starcitizen.json.TypeIndex;

import java.util.*;

/**
 * @author wftank
 * @date 2021年1月18日18:57:47
 * @description
 **/
@Getter
@AllArgsConstructor
public enum ShowTypeEnum {
    NOITEM_VEHICLE("NOITEM_Vehicle","Vehicle","载具"),
    SHIELD("Shield","Shield","护盾生成器"),
    POWERPLANT("PowerPlant","PowerPlant","发电机"),
    QUANTUMDRIVE("QuantumDrive","QuantumDrive","量子驱动器"),
    WEAPONMINING("WeaponMining","MiningLaser","采矿激光器"),
    WEAPONGUN("WeaponGun","Gun","舰炮"),
    TURRET("Turret","Turret","炮塔"),
    COOLER("Cooler","Cooler","散热器"),
    MISSILELAUNCHER("MissileLauncher","MissileLauncher","导弹架"),
    EXTERNALFUELTANK("ExternalFuelTank","ExternalFuelTank","外部燃料箱"),
    CONTAINER("Container","Container","货柜"),
    BOMB("Bomb","Bomb","炸弹"),

    WEAPON_PERSONAL("WeaponPersonal","WeaponPersonal","FPS武器"),
    WEAPON_ATTACHMENT("WeaponAttachment","WeaponAttachment","FPS武器配件"),

    CHAR_ARMOR_HELMET("Char_Armor_Helmet","Char_Armor_Helmet","护甲-头盔"),
    CHAR_ARMOR_TORSO("Char_Armor_Torso","Char_Armor_Torso","护甲-躯干"),
    CHAR_ARMOR_LEGS("Char_Armor_Legs","Char_Armor_Legs","护甲-腿部"),
    CHAR_ARMOR_ARMS("Char_Armor_Arms","Char_Armor_Arms","护甲-手臂"),
    CHAR_ARMOR_BACKPACK("Char_Armor_Backpack","Char_Armor_Backpack","背包"),


    CHAR_ARMOR_UNDERSUIT("Char_Armor_Undersuit","Char_Armor_Undersuit","基底服"),
    CHAR_CLOTHING_LEGS("Char_Clothing_Legs","Char_Clothing_Legs","裤子"),
    CHAR_CLOTHING_HANDS("Char_Clothing_Hands","Char_Clothing_Hands","手套"),
    CHAR_CLOTHING_FEET("Char_Clothing_Feet","Char_Clothing_Feet","鞋子"),
    CHAR_CLOTHING_HAT("Char_Clothing_Hat","Char_Clothing_Hat","帽子"),
    CHAR_CLOTHING_TORSO_0("Char_Clothing_Torso_0","Char_Clothing_Torso_0","衬衫"),

    MISC("Misc","Misc","杂项"),
    FOOD("Food","Food","食物"),
    MOBIGLAS("MobiGlas","MobiGlas","MobiGlas"),
    GADGET("Gadget","Gadget","手持采矿工具"),

    FPS_CONSUMABLE("FPS_Consumable","FPS_Consumable","补给"),
    MININGMODIFIER("MiningModifier","MiningModifier","采矿模组"),
    MISSILE("Missile","Missile","导弹"),
    PAINTS("Paints","Paints","涂装"),
    UNKNOWN("UNKNOWN","Other","其它"),
    ;
    @JsonValue
    private String typeKey;
    @JsonValue
    private String type;
    @JsonValue
    private String typeCn;

    private static String prefix = "Data\\Libs\\Foundry\\Records\\Entities";

    private static final Map<String, ShowTypeEnum> LOOKUP = new HashMap<>();
    public static final List<TypeIndex> SHOW = new ArrayList<>();
    public static final Map<String,TypeIndex> ShowMap = new HashMap<>();

    static {
        for (ShowTypeEnum e : ShowTypeEnum.values()) {
            LOOKUP.put(e.typeKey, e);
        }
        for (ShowTypeEnum e : ShowTypeEnum.values()) {
            TypeIndex typeIndex = new TypeIndex(e.typeKey, e.type, e.typeCn);
            SHOW.add(typeIndex);
            ShowMap.put(e.typeKey,typeIndex);
        }

    }
    public static ShowTypeEnum get(String typeKey) {
        return Optional.ofNullable(LOOKUP.get(typeKey)).orElse(UNKNOWN);
    }


}
