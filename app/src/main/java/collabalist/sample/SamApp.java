package collabalist.sample;

import android.app.Application;

import collabalist.crash_let.CrashLet;

/**
 * Created by deepak on 31/8/18.
 */

public class SamApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashLet.with(this)
                .addRecipient("deepak.mylist@gmail.com")
                .addRecipient("collbalist@gmail.com")
                .init();
    }
}
