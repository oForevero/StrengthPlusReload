package top.mccat.pojo.list;

import org.yaml.snakeyaml.Yaml;
import top.mccat.utils.ColorParseUtils;

import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * @author Raven
 * @date 2022/09/05 17:42
 */
public class LoreList extends ArrayList<String> {
    public LoreList() {
        Yaml yaml = new Yaml();
        yaml.load(new InputStreamReader(LoreList.class.getClassLoader().getResourceAsStream("strength-")));
    }

    @Override
    public boolean add(String string) {
        return super.add(ColorParseUtils.parseColorStr(string));
    }


}
