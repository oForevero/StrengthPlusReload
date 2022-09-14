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

    public static Optional<Object> loadConfigObject(String fileAddress, String pluginPath, String sectionAddress, Class<?> objClass) throws IOException, InstantiationException, IllegalAccessException {
        ConfigurationSection configurationSection = loadConfigSection(fileAddress, pluginPath, sectionAddress);
        if(configurationSection == null){
            return Optional.empty();
        }
        Value valueAnnotation = objClass.getAnnotation(Value.class);
        List<Method> setMethods = loadSetMethods(objClass);
        Field[] declaredFields = objClass.getDeclaredFields();
        Map<String, Object> objectMap = configurationSection.getValues(true);
        /*Set<String> keySet = objectMap.keySet();
        for (String s : keySet) {
            System.out.println("key :"+s+" value:"+objectMap.get(s));
        }*/
        if(objClass.getAnnotation(Value.class)==null){
            Object objResult = objClass.newInstance();
            for(Field field : declaredFields) {
                Value annotation = field.getAnnotation(Value.class);
                if (annotation == null) {
                    continue;
                }
                if(annotation.classType()[0] != Object.class){
                    if (annotation.classType()[0] == Map.class){
                        Optional<Object> optional = readClassAnnotationData(valueAnnotation, configurationSection, objClass, objectMap, setMethods, declaredFields);
                        Object o = optional.get();
                        invokeBaseMethod(setMethods,objResult,o,field);
                    }
                    if(annotation.classType()[0].getAnnotation(Value.class).classType()[0] == Map.class){
                        //Value firstAnnotation = annotation.classType()[0].getAnnotation(Value.class);
                        if(annotation.value().contains(".")) {
                            Class<?> firstClass = annotation.classType()[0];
                            Optional<Object> optional = readClassAnnotationData(annotation, configurationSection.getConfigurationSection(annotation.value()),
                                    firstClass, objectMap, loadSetMethods(firstClass), firstClass.getDeclaredFields());
                            Object o = optional.get();
                            invokeBaseMethod(setMethods, objResult, o, field);
                        }
                    }
                    if(annotation.classType()[0].getAnnotation(Value.class).classType()[0] == List.class){
                        Value firstAnnotation = annotation.classType()[0].getAnnotation(Value.class);
                        Optional<Object> optional = readClassAnnotationData(firstAnnotation, configurationSection,
                                ArrayList.class, objectMap, setMethods, declaredFields);
                        Object o = optional.get();
                        invokeBaseMethod(setMethods,objResult,o,field);
                    }
                    continue;
                }
                String value = annotation.value();
                Object o = objectMap.get(value);
                invokeBaseMethod(setMethods,objResult,o,field);
            }
            return Optional.of(objResult);
        }else {
            //如果类含有attribute
            return readClassAnnotationData(valueAnnotation,configurationSection,objClass,objectMap,setMethods,declaredFields);
        }
        /*List<Method> setMethods = loadSetMethods(objClass);
        Field[] declaredFields = objClass.getDeclaredFields();
        Map<String,Object> objectMap = configurationSection.getValues(true);
        Object objResult = objClass.newInstance();
        for(Field field : declaredFields){
            Value annotation = field.getAnnotation(Value.class);
            if(annotation == null){
                continue;
            }
            String value = annotation.value();
            Class<?>[] types = annotation.classType();
            if(types.length>1){
                //针对list里内嵌Map的情况
                for(Class<?> type : types){
                    if(type == Map.class){
                        Map<String, Object> resultMap = new HashMap<>(64);
                        Map<String, Object> keyMap = configurationSection.getValues(false);
                        Set<String> keySet = keyMap.keySet();
                        for (String s : keySet) {
                            //读取子对象方法

                            List<Method> sonMethods = loadSetMethods(sonClass);
                            Field[] sonFields = sonClass.getDeclaredFields();
                            for (Field sonField : sonFields) {
                                Value sonAnnotation = sonField.getAnnotation(Value.class);
                                if(sonAnnotation == null){
                                    continue;
                                }
                                String sonValue = sonAnnotation.value();
                                Object o = objectMap.get(s + "." + sonValue);
                                invokeBaseMethod(sonMethods,valueObj,o,sonField);
                            }
                            resultMap.put(s,valueObj);
                        }
                        return Optional.of(resultMap);
                    }else if(type == List.class){
                        List<?> list = configurationSection.getList(value);
                        invokeBaseMethod(setMethods,objResult,list,field);
                    }
                }
            }else {
                //针对map情况
                if(types[0] == Map.class){
                    ConfigurationSection section = configurationSection.getConfigurationSection(value);
                    if(section==null){
                        continue;
                    }
                    invokeBaseMethod(setMethods,objResult,section.getValues(true),field);
                }else if(types[0] == List.class){
                    List<?> list = configurationSection.getList(value);
                    invokeBaseMethod(setMethods,objResult,list,field);
                }else {
                    Object o = objectMap.get(value);
                    invokeBaseMethod(setMethods,objResult,o,field);
                }
            }
        }*/
    }

    private static Optional<Object> readClassAnnotationData(Value valueAnnotation, ConfigurationSection configurationSection,
                                                            Class<?> objClass, Map<String,Object> objectMap, List<Method> setMethods,
                                                            Field[] declaredFields) throws InstantiationException, IllegalAccessException {
        //获取子类
        Class<?> classType = valueAnnotation.classType()[0].getAnnotation(Value.class).classType()[0];
        //执行map方法
        if(classType == Map.class){
            Map<String, Object> resultMap = new HashMap<>(64);
            Map<String, Object> keyMap = configurationSection.getValues(false);
            Set<String> keySet = keyMap.keySet();
            for (String s : keySet) {
                Object valueObj = objClass.newInstance();
                readListAndMap(configurationSection.getValues(true),valueObj,loadSetMethods(objClass),objClass.getDeclaredFields(),s);
                resultMap.put(s,valueObj);
            }
            return Optional.of(resultMap);
        }else if(classType == List.class){
            //执行list init方法，目前list仍需修复问题
            List<Object> resultList = new ArrayList<>();
            Map<String, Object> keyMap = configurationSection.getValues(false);
            Set<String> keySet = keyMap.keySet();
            for (String s : keySet) {
                Object valueObj = objClass.newInstance();
                readListAndMap(objectMap,valueObj,setMethods,declaredFields,s);
                resultList.add(valueObj);
            }
            return Optional.of(resultList);
        }
        return Optional.empty();
    }

    /**
     * 读取List和Map对象
     * @param objectMap map对象
     * @param valueObj 放入容器的对象参数
     * @param fatherKey 封装入Map或List的字段
     */
    private static void readListAndMap(Map<String, Object> objectMap, Object valueObj, List<Method> methods, Field[] declaredFields, String fatherKey) {
        for (Field sonField : declaredFields) {
            Value sonAnnotation = sonField.getAnnotation(Value.class);
            if(sonAnnotation == null){
                continue;
            }
            String sonValue = sonAnnotation.value();
            Object o = objectMap.get(fatherKey + "." + sonValue);
            invokeBaseMethod(methods,valueObj,o,sonField);
        }
    }

    /**
     * 执行反射方法
     * @param setMethods 方法列表
     * @param objResult 类对象
     * @param value 获取的json键值
     * @param field 属性名
     */
    public static void invokeBaseMethod(List<Method> setMethods, Object objResult, Object value, Field field){
        for (Method setMethod : setMethods) {
            if(setMethod.getName().contains("set")){
                if(setMethod.getName().substring(3).equalsIgnoreCase(field.getName())){
                    try {
                        setMethod.invoke(objResult,value);
                    } catch (InvocationTargetException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 获取config session
     * @param fileAddress 文件地址
     * @param pluginPath 插件文件夹地址
     * @param sectionAddress json文件头
     * @return ConfigurationSection 配置文件堆
     * @throws IOException io异常
     */
    public static ConfigurationSection loadConfigSection(String fileAddress, String pluginPath, String sectionAddress) throws IOException {
        File file = new File(pluginPath + "/" + fileAddress);
        if (!file.exists()) {
            return null;
        }
        //记得增加 isExist方法
        try {
            ESSENTIALS_CONFIG.load(file);
        } catch (InvalidConfigurationException e) {
            MSG_UTILS.sendToConsole(BaseData.PLUGIN_PREFIX, "&c错误");
        }
        return ESSENTIALS_CONFIG.getConfigurationSection(sectionAddress);
    }

    /**
     * 获取配置文件map
     * @param fileAddress 文件地址
     * @param pluginPath 插件文件夹地址
     * @param sectionAddress json文件头
     * @return Optional对象
     * @throws IOException Io异常
     */
    public static Optional<Map<String,Object>> loadConfigMap(String fileAddress, String pluginPath, String sectionAddress) throws IOException {
        File file = new File(pluginPath + "/" + fileAddress);
        if (!file.exists()) {
            return Optional.empty();
        }
        //记得增加 isExist方法
        try {
            ESSENTIALS_CONFIG.load(file);
        } catch (InvalidConfigurationException e) {
            MSG_UTILS.sendToConsole(BaseData.PLUGIN_PREFIX, "&c错误");
        }
        ConfigurationSection configurationSection = ESSENTIALS_CONFIG.getConfigurationSection(sectionAddress);
        if (configurationSection == null) {
            return Optional.empty();
        }
        return Optional.of(configurationSection.getValues(true));
    }

    /**
     * 读取所有的set方法
     * @param objClass
     * @return
     */
    public static List<Method> loadSetMethods(Class<?> objClass){
        Method[] methods = objClass.getMethods();
        List<Method> methodList = new ArrayList<>();
        for(Method method : methods){
            if (method.getName().contains("set") && method.getParameterCount() == 1){
                methodList.add(method);
            }
        }
        return methodList;
    }
}
