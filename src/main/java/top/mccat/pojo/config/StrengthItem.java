package top.mccat.pojo.config;

import javafx.scene.paint.Material;
import top.mccat.anno.Value;
import top.mccat.pojo.BaseData;
import top.mccat.pojo.dao.YamlConfigObject;
import top.mccat.utils.YamlLoadUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Raven
 * @date 2022/09/05 18:43
 */
public class StrengthItem implements YamlConfigObject<StrengthItem> {
    @Value(value = "melee")
    private List<String> melee;

    @Value(value = "remote")
    private List<String> remote;

    @Value(value = "defence")
    private List<String> defence;

    public StrengthItem() {
    }

    /**
     * 近战物品是否允许强化方法
     * @param materialName material 对象
     * @return 是否可以强化
     */
    public boolean meleeCanBeStrength(String materialName){
        return melee.contains(materialName);
    }

    /**
     * 远程物品是否允许强化方法
     * @param materialName material 对象
     * @return 是否可以强化
     */
    public boolean remoteCanBeStrength(String materialName){
        return melee.contains(materialName);
    }

    /**
     * 防御物品是否允许强化方法
     * @param materialName material 对象
     * @return 是否可以强化
     */
    public boolean defenceCanBeStrength(String materialName){
        return melee.contains(materialName);
    }

    public List<String> getMelee() {
        return melee;
    }

    public void setMelee(List<String> melee) {
        this.melee = melee;
    }

    public List<String> getRemote() {
        return remote;
    }

    public void setRemote(List<String> remote) {
        this.remote = remote;
    }

    public List<String> getDefence() {
        return defence;
    }

    public void setDefence(List<String> defence) {
        this.defence = defence;
    }

    @Override
    public String toString() {
        return "StrengthItem{" +
                "melee=" + melee +
                ", remote=" + remote +
                ", defence=" + defence +
                '}';
    }

    public static StrengthItem newInstance() {
        Optional<Object> o = Optional.empty();
        try {
            o = YamlLoadUtils.loadConfigObject("strength-item.yml", BaseData.BASE_DIR,
                    "strength-item", StrengthItem.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return o.map(value -> (StrengthItem) value).orElseGet(StrengthItem::new);
    }

    @Override
    public StrengthItem reloadConfigFile() {
        return newInstance();
    }
}
