package top.mccat.enums;

/**
 * @author Raven
 * @date 2022/09/21 22:03
 */
public enum StrengthType {
    /**
     * 对应护甲类型，武器类型，弓箭类型
     */
    ARMOR_TYPE(0),
    WEAPON_TYPE(1),
    BOW_TYPE(2);
    private int type;
    private StrengthType(int type){
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
