package top.mccat.pojo.config;

import top.mccat.anno.Value;
import top.mccat.pojo.BaseData;
import top.mccat.pojo.dao.YamlConfigObject;
import top.mccat.utils.YamlLoadUtils;

import java.util.Optional;

/**
 * @author Raven
 * @date 2022/09/05 18:43
 */
public class StrengthExtra implements YamlConfigObject<StrengthExtra> {
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

    public double getCrossBow() {
        return crossBow;
    }

    public void setCrossBow(double crossBow) {
        this.crossBow = crossBow;
    }

    public double getArmorDefence() {
        return armorDefence;
    }

    public void setArmorDefence(double armorDefence) {
        this.armorDefence = armorDefence;
    }

    public double getMinDamage() {
        return minDamage;
    }

    public void setMinDamage(double minDamage) {
        this.minDamage = minDamage;
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

    public static StrengthExtra newInstance() {
        Optional<Object> o = Optional.empty();
        try {
            o = YamlLoadUtils.loadConfigObject("strength-extra.yml", BaseData.BASE_DIR,
                    "strength-extra", StrengthExtra.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return o.map(value -> (StrengthExtra) value).orElseGet(StrengthExtra::new);
    }

    @Override
    public StrengthExtra reloadConfigFile() {
        return newInstance();
    }
}
