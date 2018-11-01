package club.autobug.androidpictures;

import android.app.Application;
import android.content.Context;

public class AppApplication extends Application {
    public static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
    }
}
