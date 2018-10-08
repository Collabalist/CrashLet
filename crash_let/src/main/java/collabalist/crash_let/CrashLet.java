package collabalist.crash_let;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by deepak on 31/8/18.
 */

public class CrashLet {

    Application application;
    boolean showStackTrace = false;

    CrashLet(Application application) {
        this.application = application;
    }

    public static CrashLet with(Application application) {
        return new CrashLet(application);
    }

    ArrayList<String> recipients = null;

    public CrashLet addRecipient(String recipient) {
        if (recipients == null)
            recipients = new ArrayList<>();
        recipients.add(recipient);
        return this;
    }

    public CrashLet showStackTrace(boolean show) {
        this.showStackTrace = show;
        return this;
    }

    public void init() {
        if (recipients == null)
            recipients = new ArrayList<>();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                StringBuilder factsBuilder = new StringBuilder();
                String placeOfCrash;

                factsBuilder.append(throwable.getLocalizedMessage());
                factsBuilder.append("\n\n");
                factsBuilder.append(stackTrace(throwable.getStackTrace()));
                if (throwable.getCause() != null) {
                    factsBuilder.append("\n\n");
                    factsBuilder.append("Caused By: ");
                    StackTraceElement[] stackTrace = throwable.getCause().getStackTrace();
                    placeOfCrash = getCrashOriginatingClass(stackTrace);
                    factsBuilder.append(stackTrace(stackTrace));
                } else {
                    placeOfCrash = getCrashOriginatingClass(throwable.getStackTrace());
                }

                Crash crash = new Crash(getAppVersionName(application),
                        application.getPackageName(),
                        Build.BRAND,
                        String.valueOf(Build.VERSION.SDK_INT),
                        Build.MANUFACTURER,
                        Build.MODEL,
                        placeOfCrash,
                        throwable.getLocalizedMessage(),
                        factsBuilder.toString());
                crash.setRecipients(recipients);
                if (showStackTrace) {
                    application.startActivity(new Intent(application, ReportActivity.class)
                            .putExtra("crash", crash)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } else {
                    Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                    PackageManager pm = application.getPackageManager();
                    List<ResolveInfo> matches = pm.queryIntentActivities(intent, 0);
                    ResolveInfo best = null;
                    for (ResolveInfo info : matches)
                        if (info.activityInfo.packageName.endsWith(".gm") ||
                                info.activityInfo.name.toLowerCase().contains("gmail"))
                            best = info;
                    if (best != null) {
                        intent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
                        intent.setType("text/plain");
                    } else {
                        intent.setType("message/rfc822");
                    }
                    if (!crash.getRecipients().isEmpty()) {
                        String[] res = new String[crash.getRecipients().size()];
                        res = crash.getRecipients().toArray(res);
                        intent.putExtra(Intent.EXTRA_EMAIL, res);
                    }
                    intent.putExtra(Intent.EXTRA_TEXT, Utils.getCrashBody(crash));
                    intent.putExtra(Intent.EXTRA_SUBJECT, Utils.getApplicationName(application) + " Crash Report");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    application.startActivity(intent);
                }
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(10);
            }
        });
    }

    static String getAppVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "Not Found";
        }
    }

    static String stackTrace(StackTraceElement[] stackTrace) {
        List<StackTraceElement> stackTraceElements = Arrays.asList(stackTrace);
        StringBuilder builder = new StringBuilder();
        for (StackTraceElement stackTraceElement : stackTraceElements) {
            builder.append("at ");
            builder.append(stackTraceElement.toString());
            builder.append("\n");
        }
        return builder.toString();
    }

    static String getCrashOriginatingClass(StackTraceElement[] stackTraceElements) {
        if (stackTraceElements.length > 0) {
            StackTraceElement stackTraceElement = stackTraceElements[0];
            return String.format("%s:%d", stackTraceElement.getClassName(), stackTraceElement.getLineNumber());
        }
        return "";
    }
}
