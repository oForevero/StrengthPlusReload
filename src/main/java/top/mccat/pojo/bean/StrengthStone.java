package top.mccat.pojo.bean;

import top.mccat.anno.Value;
import top.mccat.pojo.list.LoreList;

/**
 * @author Distance
 * @date 2022/9/7
 */
public class StrengthStone {
    @Value("name")
    private String name;
    @Value("lore")
    private LoreList<String> lore;
    @Value("isSafe")
    private boolean isSafe;
    @Value("isSuccess")
    private boolean isSuccess;
    @Value("isAdmin")
    private boolean isAdmin;
    @Value("chanceExtra")
    private int chanceExtra;

    public StrengthStone() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LoreList<String> getLore() {
        return lore;
    }

    public void setLore(LoreList<String> lore) {
        this.lore = lore;
    }

    public boolean isSafe() {
        return isSafe;
    }

    public void setSafe(boolean safe) {
        isSafe = safe;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public int getChanceExtra() {
        return chanceExtra;
    }

    public void setChanceExtra(int chanceExtra) {
        this.chanceExtra = chanceExtra;
    }
}
