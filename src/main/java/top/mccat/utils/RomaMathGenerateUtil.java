package top.mccat.utils;

import top.mccat.enums.RomaMath;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Raven
 * @date 2022/09/21 22:22
 */
public class RomaMathGenerateUtil {
    int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
    String[] symbols = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

    public String intToRoman(int num) {
        StringBuffer roman = new StringBuffer();
        for (int i = 0; i < values.length; ++i) {
            int value = values[i];
            String symbol = symbols[i];
            while (num >= value) {
                num -= value;
                roman.append(symbol);
            }
            if (num == 0) {
                break;
            }
        }
        return roman.toString();
    }

    public int romanToInt(String s) {
        int sum = 0;
        char[] ch = s.toCharArray();

        for (int i = ch.length-2; i >=0 ; i--) {
            int j = i+1;
            if (getValue(ch[j])>getValue(ch[i])){
                sum = sum - getValue(ch[i]);
            }
            else if (getValue(ch[j]) <= getValue(ch[i])){
                sum = sum + getValue(ch[i]);
            }

        }
        sum = sum + getValue(ch[ch.length-1]);
        return sum;
    }
    private int getValue(char ch){
        switch(ch){
            case 'I':return 1;
            case 'V':return 5;
            case 'X':return 10;
            case 'L':return 50;
            case 'C':return 100;
            case 'D':return 500;
            case 'M':return 1000;
            default: return 0;

        }
    }
}
