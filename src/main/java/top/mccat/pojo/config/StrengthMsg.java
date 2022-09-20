package top.mccat.pojo.config;

import top.mccat.anno.Value;
import top.mccat.pojo.BaseData;
import top.mccat.pojo.bean.StrengthStone;
import top.mccat.pojo.dao.YamlConfigObject;
import top.mccat.utils.YamlLoadUtils;

import java.io.IOException;
import java.util.Optional;

/**
 * @author Raven
 * @date 2022/09/05 18:43
 */
public class StrengthMsg implements YamlConfigObject<StrengthMsg> {
    @Value("notify.success")
    private String notifySuccess;
    @Value("notify.fail")
    private String notifyFail;
    @Value("broadcast.success")
    private String broadcastSuccess;
    @Value("broadcast.safe")
    private String broadcastSafe;
    @Value("broadcast.fail")
    private String broadcastFail;

    public StrengthMsg() {
    }

    public String getNotifySuccess() {
        return notifySuccess;
    }

    public void setNotifySuccess(String notifySuccess) {
        this.notifySuccess = notifySuccess;
    }

    public String getNotifyFail() {
        return notifyFail;
    }

    public void setNotifyFail(String notifyFail) {
        this.notifyFail = notifyFail;
    }

    public String getBroadcastSuccess() {
        return broadcastSuccess;
    }

    public void setBroadcastSuccess(String broadcastSuccess) {
        this.broadcastSuccess = broadcastSuccess;
    }

    public String getBroadcastSafe() {
        return broadcastSafe;
    }

    public void setBroadcastSafe(String broadcastSafe) {
        this.broadcastSafe = broadcastSafe;
    }

    public String getBroadcastFail() {
        return broadcastFail;
    }

    public void setBroadcastFail(String broadcastFail) {
        this.broadcastFail = broadcastFail;
    }

    @Override
    public String toString() {
        return "StrengthLevel{" +
                "notifySuccess='" + notifySuccess + '\'' +
                ", notifyFail='" + notifyFail + '\'' +
                ", broadcastSuccess='" + broadcastSuccess + '\'' +
                ", broadcastSafe='" + broadcastSafe + '\'' +
                ", broadcastFail='" + broadcastFail + '\'' +
                '}';
    }

    public static StrengthMsg newInstance() {
        Optional<Object> o = Optional.empty();
        try {
            o = YamlLoadUtils.loadConfigObject("strength-msg.yml", BaseData.BASE_DIR,
                    "strength-msg", StrengthMsg.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return o.map(value -> (StrengthMsg) value).orElseGet(StrengthMsg::new);
    }

    @Override
    public StrengthMsg reloadConfigFile(){
        return newInstance();
    }
}
