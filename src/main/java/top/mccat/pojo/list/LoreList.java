package top.mccat.pojo.list;

import top.mccat.utils.ColorParseUtils;

import java.util.ArrayList;

/**
 * @author Raven
 * @date 2022/09/05 17:42
 */
public class LoreList<T> extends ArrayList<String> {
    public LoreList() {

    }

    @Override
    public boolean add(String string) {
        return super.add(ColorParseUtils.parseColorStr(string));
    }

}
