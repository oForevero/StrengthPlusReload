package top.mccat.pojo.bean;

import top.mccat.anno.Value;

import java.util.Map;

/**
 * @author Kevin Li
 * @date 2022/9/9
 * @description
 */
@Value(classType = Map.class)
public class Attribute {
    @Value("name")
    private String name;
    @Value("enable")
    private boolean enable;
    private int level = 0;
    public Attribute(String name, boolean enable) {
        this.name = name;
        this.enable = enable;
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

    public void setEnable(boolean enable) {
        this.enable = enable;
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
                "name='" + name + '\'' +
                ", enable=" + enable +
                ", level=" + level +
                '}';
    }
}
