package top.mccat.pojo.config;

import top.mccat.anno.Value;
import top.mccat.pojo.bean.Attribute;

import java.util.List;
import java.util.Map;

/**
 * @author Kevin Li
 * @date 2022/9/9
 * @description
 */
public class StrengthAttribute {
    @Value("title")
    private String title;
    @Value("divider")
    private String divider;
    @Value("levelIcon")
    private String levelIcon;
    @Value("nextLineCount")
    private int nextLineCount;
    @Value(value = "especialAttribute.melee", classType = Attribute.class)
    private Map<String, Attribute> meleeAttribute;
    @Value(value = "especialAttribute.remote", classType = Attribute.class)
    private Map<String, Attribute> remoteAttribute;
    @Value(value = "especialAttribute.defence", classType = Attribute.class)
    private Map<String, Attribute> defenceAttribute;
    @Value(value = "attribute.meleeDamage")
    private String meleeDamage;
    @Value(value = "attribute.remotelyDamage")
    private String remotelyDamage;
    @Value(value = "attribute.armorDefence")
    private String armorDefence;

    public StrengthAttribute() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDivider() {
        return divider;
    }

    public void setDivider(String divider) {
        this.divider = divider;
    }

    public String getLevelIcon() {
        return levelIcon;
    }

    public void setLevelIcon(String levelIcon) {
        this.levelIcon = levelIcon;
    }

    public int getNextLineCount() {
        return nextLineCount;
    }

    public void setNextLineCount(int nextLineCount) {
        this.nextLineCount = nextLineCount;
    }

    public Map<String, Attribute> getMeleeAttribute() {
        return meleeAttribute;
    }

    public void setMeleeAttribute(Map<String, Attribute> meleeAttribute) {
        this.meleeAttribute = meleeAttribute;
    }

    public Map<String, Attribute> getRemoteAttribute() {
        return remoteAttribute;
    }

    public void setRemoteAttribute(Map<String, Attribute> remoteAttribute) {
        this.remoteAttribute = remoteAttribute;
    }

    public Map<String, Attribute> getDefenceAttribute() {
        return defenceAttribute;
    }

    public void setDefenceAttribute(Map<String, Attribute> defenceAttribute) {
        this.defenceAttribute = defenceAttribute;
    }

    public String getMeleeDamage() {
        return meleeDamage;
    }

    public void setMeleeDamage(String meleeDamage) {
        this.meleeDamage = meleeDamage;
    }

    public String getRemotelyDamage() {
        return remotelyDamage;
    }

    public void setRemotelyDamage(String remotelyDamage) {
        this.remotelyDamage = remotelyDamage;
    }

    public String getArmorDefence() {
        return armorDefence;
    }

    public void setArmorDefence(String armorDefence) {
        this.armorDefence = armorDefence;
    }

    @Override
    public String toString() {
        return "StrengthAttribute{" +
                "title='" + title + '\'' +
                ", divider='" + divider + '\'' +
                ", levelIcon='" + levelIcon + '\'' +
                ", nextLineCount=" + nextLineCount +
                ", meleeAttribute=" + meleeAttribute +
                ", remoteAttribute=" + remoteAttribute +
                ", defenceAttribute=" + defenceAttribute +
                ", meleeDamage='" + meleeDamage + '\'' +
                ", remotelyDamage='" + remotelyDamage + '\'' +
                ", armorDefence='" + armorDefence + '\'' +
                '}';
    }
}
