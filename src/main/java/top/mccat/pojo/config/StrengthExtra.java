package top.mccat.pojo.config;

import top.mccat.anno.Value;

/**
 * @author Raven
 * @date 2022/09/05 18:43
 */
public class StrengthExtra {
    @Value("damage.sword")
    private double swordDamage;
    @Value("damage.bow")
    private double bowDamage;
    @Value("damage.crossbow")
    private double crossBow;
    @Value("defence.armorDefence")
    private double armorDefence;
    @Value("defence.minDamage")
    private double minDamage;

    public StrengthExtra() {
    }

    public double getSwordDamage() {
        return swordDamage;
    }

    public void setSwordDamage(double swordDamage) {
        this.swordDamage = swordDamage;
    }

    public double getBowDamage() {
        return bowDamage;
    }

    public void setBowDamage(double bowDamage) {
        this.bowDamage = bowDamage;
    }

    @Override
    public String toString() {
        return "StrengthExtra{" +
                "swordDamage=" + swordDamage +
                ", bowDamage=" + bowDamage +
                ", crossBow=" + crossBow +
                ", armorDefence=" + armorDefence +
                ", minDamage=" + minDamage +
                '}';
    }
}
