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
    @Value("name")
    private List<Material> strengthItem;

    public StrengthItem() {
        strengthItem = new ArrayList<>();
    }

    public List<Material> getStrengthItem() {
        return strengthItem;
    }

    public void setStrengthItem(List<Material> strengthItem) {
        this.strengthItem = strengthItem;
    }

    /**
     * 物品是否允许强化方法
     * @param material material 对象
     * @return 是否可以强化
     */
    public boolean canBeStrength(Material material){
        return strengthItem.contains(material);
    }

    @Override
    public String toString() {
        return "StrengthItem{" +
                "strengthItem=" + strengthItem +
                '}';
    }
}
