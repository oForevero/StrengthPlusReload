package top.mccat.enums;

/**
 * @author Raven
 * @date 2022/09/21 22:29
 */
public enum RomaMath {
    /**
     * 对应罗马数字
     */
    I('I',1),
    V('V',5),
    X('X',10),
    L('L',50),
    C('C',100),
    D('D',500),
    M('M',1000);
    private char mathString;
    private int mathValue;

    private RomaMath(char mathString, int mathValue) {
        this.mathString = mathString;
        this.mathValue = mathValue;
    }

    public char getMathString() {
        return mathString;
    }

    public void setMathString(char mathString) {
        this.mathString = mathString;
    }

    public int getMathValue() {
        return mathValue;
    }

    public void setMathValue(int mathValue) {
        this.mathValue = mathValue;
    }
}
