package top.mccat.pojo.bean;

import top.mccat.anno.Value;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Kevin Li
 * @date 2022/9/9
 * @description
 */
@Value(value = "especialAttribute",classType = Map.class)
public class Attribute implements Serializable {
    @Value("tag")
    private String tag;
    @Value("name")
    private String name;
    @Value("enable")
    private boolean enable;
    private int level = 1;

    public Attribute() {
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(Object enable) {
        this.enable = (boolean) enable;
    }

    public int getLevel() {
        return level;
    }

    public void putLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "Attribute{" +
                "tag='" + tag + '\'' +
                ", name='" + name + '\'' +
                ", enable=" + enable +
                ", level=" + level +
                '}';
    }
}
