package collabalist.crash_let;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

/**
 * Created by deepak on 31/8/18.
 */

public class ReportActivity extends AppCompatActivity {
    TextView where, reason, stackTrace, deviceInfo;

    Crash crash;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crash_activity);
        where = (TextView) findViewById(R.id.crashWhereTV);
        reason = (TextView) findViewById(R.id.crashReasonTV);
        stackTrace = (TextView) findViewById(R.id.crashStackTraceTV);
        deviceInfo = (TextView) findViewById(R.id.deviceInfoTV);
        crash = (Crash) getIntent().getSerializableExtra("crash");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        where.setText(crash.getCrashWhere());
        reason.setText(crash.getCrashReason());
        stackTrace.setText(crash.getCrashStackTrace());
        deviceInfo.setText("Package: " + crash.getAppPackageName()
                + "\nVersion: " + crash.getAppVersionName()
                + "\nBrand: " + crash.getDeviceBrand()
                + "\nAPI Version: " + crash.getDeviceAPI()
                + "\nManufacturer: " + crash.getDeviceManufacturer()
                + "\nModel: " + crash.getDeviceModel());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_t, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.shareReport) {
            setG_Mail();
        }
        return super.onOptionsItemSelected(item);
    }


    void setG_Mail() {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        PackageManager pm = getPackageManager();
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
        intent.putExtra(Intent.EXTRA_TEXT, getCrashBody());
        intent.putExtra(Intent.EXTRA_SUBJECT, getApplicationName(ReportActivity.this) + " Crash Report");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        ReportActivity.this.finish();
    }

    String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }


    String getCrashBody() {
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
