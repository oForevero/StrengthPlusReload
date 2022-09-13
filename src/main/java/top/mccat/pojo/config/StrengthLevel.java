package top.mccat.pojo.config;

import top.mccat.anno.Value;

/**
 * @author Raven
 * @date 2022/09/05 18:43
 */
public class StrengthLevel {
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

    public StrengthLevel(String notifySuccess, String notifyFail, String broadcastSuccess, String broadcastSafe, String broadcastFail) {
        this.notifySuccess = notifySuccess;
        this.notifyFail = notifyFail;
        this.broadcastSuccess = broadcastSuccess;
        this.broadcastSafe = broadcastSafe;
        this.broadcastFail = broadcastFail;
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
}
