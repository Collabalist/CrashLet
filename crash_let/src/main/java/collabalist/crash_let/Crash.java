package collabalist.crash_let;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by deepak on 31/8/18.
 */

public class Crash implements Serializable {
    String appVersionName = "", appPackageName = "",
            deviceBrand = "", deviceAPI = "", deviceManufacturer = "", deviceModel = "",
            crashWhere = "", crashReason = "", crashStackTrace = "";
    ArrayList<String> recipients;

    public ArrayList<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(ArrayList<String> recipients) {
        this.recipients = recipients;
    }

    public Crash(String appVersionName, String appPackageName, String deviceBrand, String deviceAPI, String deviceManufacturer, String deviceModel, String crashWhere, String crashReason, String crashStackTrace) {
        this.appVersionName = appVersionName;
        this.appPackageName = appPackageName;
        this.deviceBrand = deviceBrand;
        this.deviceAPI = deviceAPI;
        this.deviceManufacturer = deviceManufacturer;
        this.deviceModel = deviceModel;
        this.crashWhere = crashWhere;
        this.crashReason = crashReason;
        this.crashStackTrace = crashStackTrace;
    }

    public String getAppVersionName() {
        return appVersionName;
    }

    public void setAppVersionName(String appVersionName) {
        this.appVersionName = appVersionName;
    }

    public String getAppPackageName() {
        return appPackageName;
    }

    public void setAppPackageName(String appPackageName) {
        this.appPackageName = appPackageName;
    }

    public String getDeviceBrand() {
        return deviceBrand;
    }

    public void setDeviceBrand(String deviceBrand) {
        this.deviceBrand = deviceBrand;
    }

    public String getDeviceAPI() {
        return deviceAPI;
    }

    public void setDeviceAPI(String deviceAPI) {
        this.deviceAPI = deviceAPI;
    }

    public String getDeviceManufacturer() {
        return deviceManufacturer;
    }

    public void setDeviceManufacturer(String deviceManufacturer) {
        this.deviceManufacturer = deviceManufacturer;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getCrashWhere() {
        return crashWhere;
    }

    public void setCrashWhere(String crashWhere) {
        this.crashWhere = crashWhere;
    }

    public String getCrashReason() {
        return crashReason;
    }

    public void setCrashReason(String crashReason) {
        this.crashReason = crashReason;
    }

    public String getCrashStackTrace() {
        return crashStackTrace;
    }

    public void setCrashStackTrace(String crashStackTrace) {
        this.crashStackTrace = crashStackTrace;
    }
}
