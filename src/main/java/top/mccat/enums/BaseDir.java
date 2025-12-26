package top.mccat.enums;

import java.io.File;

/**
 * @author Raven
 * @date 2022/09/06 21:31
 */

public enum BaseDir {
    /**
     * 对应文件地址
     */
    BASE_DIR("plugins" + File.separator + "StrengthPlus");
    private final String dir;

    BaseDir(String dir) {
        this.dir = dir;
    }

    public String getDir() {
        return dir;
    }
}
