package top.mccat.exception;

import top.mccat.utils.ColorParseUtils;

/**
 * @author Kevin Li
 * @date 2022/9/27
 * @description
 */
public class ItemCanBeStrengthException extends Exception {

    public ItemCanBeStrengthException(String message) {
        super(ColorParseUtils.parseColorStr(message));
    }
}
