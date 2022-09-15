package top.mccat.pojo.bean;

import top.mccat.anno.Value;

import java.util.List;
import java.util.Map;

/**
 * @author Distance
 * @date 2022/9/7
 */
//之前没有对这个进行修改，明天查看一下修改
@Value(value = "stones",classType = Map.class)
public class StrengthStone {
    @Value("name")
    private String name;
    @Value(value = "lore", classType = List.class)
    private List<String> lore;
    @Value("safe")
    private boolean safe = false;
    @Value("success")
    private boolean success = false;
    @Value("admin")
    private boolean admin = false;
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

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
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

    public void setChanceExtra(int chanceExtra) {
        this.chanceExtra = chanceExtra;
    }

    @Override
    public String toString() {
        return "StrengthStone{" +
                "  name='" + name + '\'' +
                ", lore=" + lore +
                ", safe=" + safe +
                ", success=" + success +
                ", admin=" + admin +
                ", chanceExtra=" + chanceExtra +
                '}';
    }
}
