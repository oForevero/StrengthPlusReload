package top.mccat.pojo.bean;

import top.mccat.anno.Value;
import top.mccat.pojo.list.LoreList;

import java.util.List;

/**
 * @author Distance
 * @date 2022/9/7
 */
public class StrengthStone {
    private String stoneMaterials;
    @Value("name")
    private String name;
    @Value("lore")
    private List<String> lore;
    @Value("isSafe")
    private boolean isSafe = false;
    @Value("isSuccess")
    private boolean isSuccess = false;
    @Value("isAdmin")
    private boolean isAdmin = false;
    @Value("chanceExtra")
    private int chanceExtra = 0;

    public StrengthStone() {
    }

    public String getName() {
        return name;
    }

    public void setName(Object name) {
        this.name = String.valueOf(name);
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(Object lore) {
        this.lore = (List<String>) lore;
    }

    public boolean isSafe() {
        return isSafe;
    }

    public void setSafe(Object safe) {
        isSafe = (boolean) safe;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(Object success) {
        isSuccess = (boolean) success;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(Object admin) {
        isAdmin = (boolean) admin;
    }

    public int getChanceExtra() {
        return chanceExtra;
    }

    public void setChanceExtra(Object chanceExtra) {
        this.chanceExtra = (int) chanceExtra;
    }

    public boolean isStrengthStone(String stoneMaterials){
        return this.stoneMaterials.equals(stoneMaterials);
    }

    @Override
    public String toString() {
        return "StrengthStone{" +
                "stoneMaterials='" + stoneMaterials + '\'' +
                ", name='" + name + '\'' +
                ", lore=" + lore +
                ", isSafe=" + isSafe +
                ", isSuccess=" + isSuccess +
                ", isAdmin=" + isAdmin +
                ", chanceExtra=" + chanceExtra +
                '}';
    }
}
