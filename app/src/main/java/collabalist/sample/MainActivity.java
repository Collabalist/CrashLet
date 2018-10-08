package collabalist.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Map;

import collabalist.retroletLib.Callbacks.RequestListener;
import collabalist.retroletLib.RequestHelper.RetroLet;

public class MainActivity extends AppCompatActivity {

    Button buttonPanel;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonPanel = (Button) findViewById(R.id.buttonPanel);
        buttonPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RetroLet.post("http://demo2server.com/risenet/api/", "home-feeds")
                        .addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjM2NSwiaXNzIjoiaHR0cDovL2RlbW8yc2VydmVyLmNvbS9yaXNlbmV0L2FwaS92YXJpZnktcGhvbmUtbnVtYmVyIiwiaWF0IjoxNTM4NDYwMjUwLCJleHAiOjE1Mzk2Njk4NTAsIm5iZiI6MTUzODQ2MDI1MCwianRpIjoiMmw2cUxjM1FkZzNQcTVaQSJ9.dAd--mRXZIU36W4c_ocJ1RW3serukgFXFPTpAGzK_BQ")
                        .addQuery("keyword", "")
                        .addQuery("latitude", "26.890784")
                        .addQuery("longitude", "75.7420913")
                        .addQuery("page", 1 + "")
                        .build(MainActivity.this)
                        .execute(new RequestListener() {
                            @Override
                            public void beforeExecuting(String s, Map map, int i) {

                            }

                            @Override
                            public void onResponse(String s, int i) {
                                Log.e("response", s);

                                title.setText(s);
                            }

                            @Override
                            public void onError(String s, String s1, int i) {

                            }
                        });
            }
        });
    }
}
