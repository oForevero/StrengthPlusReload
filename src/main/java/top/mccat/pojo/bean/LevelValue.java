package top.mccat.pojo.bean;

import top.mccat.anno.Value;
import top.mccat.pojo.BaseData;
import top.mccat.pojo.dao.YamlConfigObject;
import top.mccat.utils.YamlLoadUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Kevin Li
 * @date 2022/9/7
 * @description
 */
@Value(value = "levels",classType = List.class)
public class LevelValue implements YamlConfigObject<List<LevelValue>> {
    @Value("normalStone")
    private int normalStone;
    @Value("loseLevel")
    private boolean loseLevel;
    @Value("canBreak")
    private boolean canBreak;
    @Value("chance")
    private int chance;
    @Value("costStone")
    private List<String> strengthStones;

    public LevelValue() {
    }

    public int getNormalStone() {
        return normalStone;
    }

    public void setNormalStone(int normalStone) {
        this.normalStone = normalStone;
    }

    public boolean isLoseLevel() {
        return loseLevel;
    }

    public void setLoseLevel(boolean loseLevel) {
        this.loseLevel = loseLevel;
    }

    public boolean isCanBreak() {
        return canBreak;
    }

    public void setCanBreak(boolean canBreak) {
        this.canBreak = canBreak;
    }

    public int getChance() {
        return chance;
    }

    public void setChance(int chance) {
        this.chance = chance;
    }

    public List<String> getStrengthStones() {
        return strengthStones;
    }

    public void setStrengthStones(List<String> strengthStones) {
        this.strengthStones = strengthStones;
    }

    @Override
    public String toString() {
        return "LevelValue{" +
                "normalStone=" + normalStone +
                ", loseLevel=" + loseLevel +
                ", canBreak=" + canBreak +
                ", chance=" + chance +
                ", strengthStones=" + strengthStones +
                '}';
    }

    public static List<LevelValue> newInstance() {
        Optional<Object> o = Optional.empty();
        try {
            o = YamlLoadUtils.loadConfigObject("strength-level.yml", BaseData.BASE_DIR,
                    "strength-level", LevelValue.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return o.map(value -> (List<LevelValue>) value).orElseGet(ArrayList::new);
    }

    @Override
    public List<LevelValue> reloadConfigFile() {
        return newInstance();
    }
}
