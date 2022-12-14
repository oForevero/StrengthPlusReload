package top.mccat.pojo.bean;

import top.mccat.anno.Value;
import top.mccat.pojo.BaseData;
import top.mccat.pojo.dao.YamlConfigObject;
import top.mccat.utils.LoreGenerateUtils;
import top.mccat.utils.YamlLoadUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Distance
 * @date 2022/9/7
 */
@Value(value = "stones",classType = Map.class)
public class StrengthStone implements YamlConfigObject<Map<String,StrengthStone>> {
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
    @Value("extraStone")
    private boolean extraStone = false;
    @Value("item")
    private String item;

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
        this.lore = LoreGenerateUtils.parseColorLore(lore);
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

    public void setExtraStone(Object extraStone) {
        this.extraStone = (boolean) extraStone;
    }

    public boolean getExtraStone(){
        return extraStone;
    }

    public String getItem() {
        return item;
    }

    public void setItem(Object item) {
        this.item = String.valueOf(item);
    }

    @SuppressWarnings("unchecked")
    public static Map<String,StrengthStone> newInstance() {
        Optional<Object> o = Optional.empty();
        try {
            o = YamlLoadUtils.loadConfigObject("strength-stone.yml", BaseData.BASE_DIR,
                    "strength-stone", StrengthStone.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return o.map(value -> (Map<String,StrengthStone>) value).orElseGet(HashMap::new);
    }
    @Override
    public Map<String, StrengthStone> reloadConfigFile() {
        return newInstance();
    }
}
