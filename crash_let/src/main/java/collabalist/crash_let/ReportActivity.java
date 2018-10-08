package collabalist.crash_let;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by deepak on 31/8/18.
 */

public class ReportActivity extends AppCompatActivity {
    TextView where, reason, stackTrace, deviceInfo;
    TabLayout tabLayout;
    NestedScrollView crashNSV,nsvT;
    LinearLayout requestLL;
    TextView methodType, urlTxt, responseCode, responseTV;
    RecyclerView params;
    //Non ui vars
    Crash crash;
    ArrayList<ItemParams> parmasList;

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

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        crashNSV = (NestedScrollView) findViewById(R.id.crashNSV);
        requestLL = (LinearLayout) findViewById(R.id.requestLL);
        methodType = (TextView) findViewById(R.id.methodType);
        urlTxt = (TextView) findViewById(R.id.urlTxt);
        responseCode = (TextView) findViewById(R.id.responseCode);
        responseTV = (TextView) findViewById(R.id.responseTV);
        params = (RecyclerView) findViewById(R.id.params);
        nsvT= (NestedScrollView) findViewById(R.id.nsvT);
        params.setLayoutManager(new LinearLayoutManager(ReportActivity.this, LinearLayoutManager.VERTICAL, false));
        params.setNestedScrollingEnabled(false);


        where.setText(crash.getCrashWhere());
        reason.setText(crash.getCrashReason());
        stackTrace.setText(crash.getCrashStackTrace());
        deviceInfo.setText("Package: " + crash.getAppPackageName()
                + "\nVersion: " + crash.getAppVersionName()
                + "\nBrand: " + crash.getDeviceBrand()
                + "\nAPI Version: " + crash.getDeviceAPI()
                + "\nManufacturer: " + crash.getDeviceManufacturer()
                + "\nModel: " + crash.getDeviceModel());
        setUpdetails();
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
        intent.putExtra(Intent.EXTRA_TEXT, Utils.getCrashBody(crash));
        intent.putExtra(Intent.EXTRA_SUBJECT, Utils.getApplicationName(ReportActivity.this) + " Crash Report");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        ReportActivity.this.finish();
    }

    void setUpdetails() {
        SharedPreferences preferences = ReportActivity.this.getSharedPreferences("collabalist_RetroLet", MODE_PRIVATE);
        JSONObject queries = null, headers = null, files = null;
        String requestType = "", url = "", response = "";
        url = preferences.getString("url", "");
        requestType = preferences.getString("requestType", "");
        response = preferences.getString("response", "");
        if (url.equals("")) {
            tabLayout.setVisibility(View.GONE);
            requestLL.setVisibility(View.GONE);
            crashNSV.setVisibility(View.VISIBLE);
        } else {
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if (tabLayout.getSelectedTabPosition() == 0) {
                        crashNSV.setVisibility(View.VISIBLE);
                        requestLL.setVisibility(View.GONE);
                    } else {
                        requestLL.setVisibility(View.VISIBLE);
                        crashNSV.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
            tabLayout.setVisibility(View.VISIBLE);
            requestLL.setVisibility(View.GONE);
            crashNSV.setVisibility(View.VISIBLE);
            tabLayout.addTab(tabLayout.newTab().setText("CRASH"));
            tabLayout.addTab(tabLayout.newTab().setText("LAST REQUEST"));
            try {
                if (!preferences.getString("queries", "").isEmpty()) {
                    queries = new JSONObject(preferences.getString("queries", ""));
                }
                if (!preferences.getString("headers", "").isEmpty()) {
                    headers = new JSONObject(preferences.getString("headers", ""));
                }
                if (!preferences.getString("files", "").isEmpty()) {
                    files = new JSONObject(preferences.getString("files", ""));
                }

                List<String> queryKeys = new ArrayList<>(), headerKeys = new ArrayList<>(), fileKeys = new ArrayList<>();
                if (queries != null)
                    queryKeys = keysList(queries.keys());
                if (headers != null)
                    headerKeys = keysList(headers.keys());
                if (files != null)
                    fileKeys = keysList(files.keys());

                parmasList = new ArrayList<>();
                if (!headerKeys.isEmpty()) {
                    parmasList.add(new ItemParams("title", "Headers", ""));
                    for (int i = 0; i < headerKeys.size(); i++)
                        parmasList.add(new ItemParams("param", headerKeys.get(i), headers.getString(headerKeys.get(i))));
                }
                if (!queryKeys.isEmpty()) {
                    parmasList.add(new ItemParams("title", "Parameters", ""));
                    for (int i = 0; i < queryKeys.size(); i++)
                        parmasList.add(new ItemParams("param", queryKeys.get(i), queries.getString(queryKeys.get(i))));
                }
                if (!fileKeys.isEmpty()) {
                    parmasList.add(new ItemParams("title", "Files", ""));
                    for (int i = 0; i < fileKeys.size(); i++)
                        parmasList.add(new ItemParams("param", fileKeys.get(i), files.getString(fileKeys.get(i))));
                }
                if (!parmasList.isEmpty()) {
                    params.setAdapter(new ParamsAdapter(ReportActivity.this, parmasList));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            urlTxt.setText(url);
            methodType.setText(requestType + ".");
            responseTV.setText(response);
        }
    }

    List<String> keysList(Iterator<String> t) {
        List<String> keys = new ArrayList<>();
        Iterator<String> q = t;
        while (q.hasNext()) {
            keys.add(q.next());
        }
        return keys;
    }
}
