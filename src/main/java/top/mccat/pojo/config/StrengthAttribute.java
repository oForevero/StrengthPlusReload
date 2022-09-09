package top.mccat.pojo.config;

import top.mccat.anno.Value;
import top.mccat.pojo.bean.Attribute;

import java.util.List;
import java.util.Map;

/**
 * @author Kevin Li
 * @date 2022/9/9
 * @description
 */
public class StrengthAttribute {
    @Value("title")
    private String title;
    @Value("divider")
    private String divider;
    @Value("levelIcon")
    private String levelIcon;
    @Value("nextLineCount")
    private int nextLineCount;
    @Value("especialAttribute")
    private List<Map<String, Attribute>> especialAttribute;
    @Value("attribute")
    private Map<String,String> attribute;

    public StrengthAttribute() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDivider() {
        return divider;
    }

    public void setDivider(String divider) {
        this.divider = divider;
    }

    public String getLevelIcon() {
        return levelIcon;
    }

    public void setLevelIcon(String levelIcon) {
        this.levelIcon = levelIcon;
    }

    public int getNextLineCount() {
        return nextLineCount;
    }

    public void setNextLineCount(int nextLineCount) {
        this.nextLineCount = nextLineCount;
    }

    public List<Map<String, Attribute>> getEspecialAttribute() {
        return especialAttribute;
    }

    public void setEspecialAttribute(List<Map<String, Attribute>> especialAttribute) {
        this.especialAttribute = especialAttribute;
    }

    public Map<String, String> getAttribute() {
        return attribute;
    }

    public void setAttribute(Map<String, String> attribute) {
        this.attribute = attribute;
    }
}
