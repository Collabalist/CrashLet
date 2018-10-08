package collabalist.crash_let;

import android.content.Context;
import android.content.pm.ApplicationInfo;

/**
 * Created by deepak on 8/10/18.
 */

public class Utils {
    public static String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }

    public static String getCrashBody(Crash crash) {
        String body = "";
        body = body + "Where:\n"
                + crash.getCrashWhere();
        body = body + "\n\nReason:\n"
                + crash.getCrashReason();
        body = body + "\n\nStackTrace:\n"
                + crash.getCrashStackTrace();
        body = body + "\n\nDevice Info:\n"
                + "Package: " + crash.getAppPackageName()
                + "\nVersion: " + crash.getAppVersionName()
                + "\nBrand: " + crash.getDeviceBrand()
                + "\nAPI Version: " + crash.getDeviceAPI()
                + "\nManufacturer: " + crash.getDeviceManufacturer()
                + "\nModel: " + crash.getDeviceModel();
        return body;
    }
}
