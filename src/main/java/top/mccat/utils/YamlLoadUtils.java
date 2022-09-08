package top.mccat.utils;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;
import top.mccat.anno.Value;
import top.mccat.pojo.BaseData;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

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
    private final static MsgUtils MSG_UTILS = MsgUtils.newInstance();
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
     * @param configClass obj对象
     * @return Optional 包装器的对象
     * @throws IOException io异常
     * @throws IllegalAccessException 无访问权限异常
     */
    public static Optional<Object> loadYamlAsObject(String fileAddress, String pluginPath, String sectionAddress, Class<top.mccat.pojo.config.BaseConfig> configClass) throws IOException, IllegalAccessException, InstantiationException {
        File file = new File(pluginPath+"/"+fileAddress);
        if (!file.exists()){
            return Optional.empty();
        }
        //记得增加 isExist方法
        try {
            ESSENTIALS_CONFIG.load(file);
        } catch (InvalidConfigurationException e) {
            MSG_UTILS.sendToConsole(BaseData.PLUGIN_PREFIX,"&c错误");
        }
        ConfigurationSection configurationSection = ESSENTIALS_CONFIG.getConfigurationSection(sectionAddress);
        if(configurationSection==null){
            return Optional.empty();
        }
        Field[] fields = configClass.getDeclaredFields();
        Map<String, Object> values = configurationSection.getValues(true);
        Method[] methods = configClass.getMethods();
        String methodName;
        Object o = configClass.newInstance();
        for(Field field : fields){
            for(Method method : methods){
                if ((methodName = (method.getName())).contains("set")){
                    String key = field.getAnnotation(Value.class).value();
                    if(methodName.substring(3).equalsIgnoreCase(field.getName()) && values.containsKey(key)){
                        try {
                            method.invoke(o,values.get(key));
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return Optional.of(o);
    }

    /**
     * 进行yaml数据转换为 object
     * @param fileAddress 文件地址
     * @param pluginPath 插件地址
     * @param idName 类数据名
     * @param sectionAddress 配置文件
     * @param configClass obj对象
     * @return Optional 包装器的对象
     * @throws IOException io异常
     * @throws InvalidConfigurationException 配置文件不存在的异常
     * @throws InvocationTargetException 方法代理异常
     * @throws IllegalAccessException 无访问权限异常
     */
    public static Optional<List<Object>> loadYamlArrayAsObject(String fileAddress, String pluginPath, String idName, String sectionAddress, Class configClass) throws IOException, InvalidConfigurationException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        ESSENTIALS_CONFIG.load(new File(pluginPath+"/"+fileAddress));
        ConfigurationSection configurationSection = ESSENTIALS_CONFIG.getConfigurationSection(sectionAddress);
        if(configurationSection==null){
            return Optional.empty();
        }
        Field[] fields = configClass.getDeclaredFields();
        Map<String, Object> objectMap = configurationSection.getValues(false);
        List<Object> dataList = new ArrayList<>();
        Method[] methods = configClass.getMethods();
        String methodName;
        Map<String, Object> values = configurationSection.getValues(true);
        for (String dataName : objectMap.keySet()){
            Object o = configClass.newInstance();
            if (!dataName.contains(".")){
                configClass.getMethod(idName,String.class).invoke(o,dataName);
            }
            for(Field field : fields){
                for(Method method : methods){
                    if ((methodName = (method.getName())).contains("set")){
                        if(methodName.substring(3).equalsIgnoreCase(field.getName())){
                            String key = dataName+"."+field.getAnnotation(Value.class).value();
                            if(values.containsKey(key)) {
                                method.invoke(o, values.get(key));
                            }
                        }
                    }
                }
            }
            dataList.add(o);
        }
        return Optional.of(dataList);
    }
}
