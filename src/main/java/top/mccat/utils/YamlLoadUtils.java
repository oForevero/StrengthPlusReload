package top.mccat.utils;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;
import top.mccat.anno.Value;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;

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
    private final static FileConfiguration ESSENTIALS_CONFIG = new YamlConfiguration();
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

    /**
     * 进行yaml数据转换为 object
     * @param fileAddress 文件地址
     * @param pluginPath 插件地址
     * @param sectionAddress 配置文件
     * @param configObj obj对象
     * @return Optional 包装器的对象
     * @throws IOException io异常
     * @throws InvalidConfigurationException 配置文件不存在的异常
     * @throws InvocationTargetException 方法代理异常
     * @throws IllegalAccessException 无访问权限异常
     */
    public static Optional<Object> loadYamlAsObject(String fileAddress, String pluginPath, String sectionAddress, Object configObj) throws IOException, InvalidConfigurationException, InvocationTargetException, IllegalAccessException {
        ESSENTIALS_CONFIG.load(new File(pluginPath+"/"+fileAddress));
        ConfigurationSection configurationSection = ESSENTIALS_CONFIG.getConfigurationSection(sectionAddress);
        if(configurationSection==null){
            return Optional.empty();
        }
        Class<?> aClass = configObj.getClass();
        Field[] fields = aClass.getDeclaredFields();
        Map<String, Object> values = configurationSection.getValues(true);
        Method[] methods = aClass.getMethods();
        String methodName;
        for(Field field : fields){
            for(Method method : methods){
                if ((methodName = (method.getName())).contains("set")){
                    if(methodName.substring(3).equalsIgnoreCase(field.getName())){
                        String key = field.getAnnotation(Value.class).value();
                        method.invoke(configObj,values.get(key));
                    }
                }
            }
        }
        return Optional.of(configObj);
    }
}
