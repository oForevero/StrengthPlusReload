package top.mccat.pojo.dao;

import java.io.IOException;
import java.util.Optional;

/**
 * @author Distance
 * @date 2022/9/20
 */
public interface YamlConfigObject<T> {
    /**
     * 重载配置文件
     * @return Optional包装类
     */
    public T reloadConfigFile();
}
