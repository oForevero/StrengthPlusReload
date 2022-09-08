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
    private boolean safe = false;
    @Value("isSuccess")
    private boolean success = false;
    @Value("isAdmin")
    private boolean admin = false;
    @Value("chanceExtra")
    private int chanceExtra = 0;

    public StrengthStone() {
    }

    public void putStoneMaterials(String stoneMaterials) {
        this.stoneMaterials = stoneMaterials;
    }

    public String getStoneMaterials() {
        return stoneMaterials;
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
        return safe;
    }

    public void setSafe(Object safe) {
        this.safe = (boolean) safe;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(Object success) {
        this.success = (boolean) success;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(Object admin) {
        this.admin = (boolean) admin;
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
                ", isSafe=" + safe +
                ", isSuccess=" + success +
                ", isAdmin=" + admin +
                ", chanceExtra=" + chanceExtra +
                '}';
    }
}
