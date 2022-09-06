package top.mccat.enums;

/**
 * @author Distance
 */

public enum BaseData {
    /**
     * 对应文件地址
     */
    BASE_DIR("plugins/strengthPlus");
    private final String dir;

    BaseData(String dir) {
        this.dir = dir;
    }

    public String getDir() {
        return dir;
    }
}
