package top.mccat.utils;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @author Raven
 * @date 2022/09/05 18:07
 *
 */
public class YamlLoadUtils{
    /**
     * Yaml终态对象
     */
    public final static Yaml YAML_OBJ = new Yaml();
    private YamlLoadUtils(){}

    /**
     * 读取yamlMap数据
     * @param fileAddress 文件地址
     * @return yaml Map
     */
    public static Map<String, Object> loadYamlAsMap(String fileAddress) throws IOException {
        InputStream resourceAsStream = YamlLoadUtils.class.getClassLoader().getResourceAsStream(fileAddress);
        return YAML_OBJ.load(resourceAsStream);
    }
}
