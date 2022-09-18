package top.mccat.pojo.bean;

import top.mccat.anno.Value;

import java.util.List;

/**
 * @author Kevin Li
 * @date 2022/9/7
 * @description
 */
@Value(value = "levels",classType = List.class)
public class LevelValue {
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
}
