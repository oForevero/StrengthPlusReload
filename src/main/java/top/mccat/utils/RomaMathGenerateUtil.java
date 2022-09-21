package top.mccat.utils;

import top.mccat.enums.RomaMath;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Raven
 * @date 2022/09/21 22:22
 */
public class RomaMathGenerateUtil {
    private static final Map<Integer,String> ROMA_MAP = new HashMap<Integer,String>(13){{
        put(1,"I");
        put(4,"VI");
        put(5,"V");
        put(10,"X");
        put(9,"XI");
        put(40,"LX");
        put(50,"L");
        put(90,"CX");
        put(100,"C");
        put(400,"DC");
        put(500,"D");
        put(900,"MC");
        put(1000,"M");
    }};

    public static String intToRomanString(int num) {
        StringBuilder romanDigit = new StringBuilder();
        int level = 1;
        while(num!=0){
            int digit = num%10;

            if(digit>5){
                if(digit==9){
                    romanDigit.append(ROMA_MAP.get(9 * level));
                }else{
                    digit = digit-5;
                    while(digit!=0){
                        digit--;
                        romanDigit.append(ROMA_MAP.get(level));
                    }
                    romanDigit.append(ROMA_MAP.get(5 * level));}
            }else {
                if(digit==4||digit==5){
                    romanDigit.append(ROMA_MAP.get(digit * level));
                }else if(digit!=0){
                    while(digit!=0){
                        digit--;
                        romanDigit.append(ROMA_MAP.get(level));
                    }
                }
            }
            level*=10;
            num/=10;
        }
        return new StringBuilder(romanDigit.toString()).reverse().toString();
    }
}
