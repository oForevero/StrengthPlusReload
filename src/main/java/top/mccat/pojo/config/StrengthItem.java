package top.mccat.pojo.config;

import javafx.scene.paint.Material;
import top.mccat.anno.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Raven
 * @date 2022/09/05 18:43
 */
public class StrengthItem {
    @Value("melee")
    private List<Material> melee;

    @Value("remote")
    private List<Material> remote;

    @Value("defence")
    private List<Material> defence;

    public StrengthItem() {
    }

    public List<Material> getStrengthItem() {
        return melee;
    }

    public void setStrengthItem(List<Material> strengthItem) {
        this.melee = strengthItem;
    }

    /**
     * 近战物品是否允许强化方法
     * @param material material 对象
     * @return 是否可以强化
     */
    public boolean meleeCanBeStrength(Material material){
        return melee.contains(material);
    }

    /**
     * 远程物品是否允许强化方法
     * @param material material 对象
     * @return 是否可以强化
     */
    public boolean remoteCanBeStrength(Material material){
        return melee.contains(material);
    }

    /**
     * 防御物品是否允许强化方法
     * @param material material 对象
     * @return 是否可以强化
     */
    public boolean defenceCanBeStrength(Material material){
        return melee.contains(material);
    }

    public StrengthItem(List<Material> melee, List<Material> remote, List<Material> defence) {
        this.melee = melee;
        this.remote = remote;
        this.defence = defence;
    }

    public List<Material> getMelee() {
        return melee;
    }

    public void setMelee(List<Material> melee) {
        this.melee = melee;
    }

    public List<Material> getRemote() {
        return remote;
    }

    public void setRemote(List<Material> remote) {
        this.remote = remote;
    }

    public List<Material> getDefence() {
        return defence;
    }

    public void setDefence(List<Material> defence) {
        this.defence = defence;
    }
}
