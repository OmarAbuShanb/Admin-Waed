package waed.dev.adminhoria.utils;

import android.app.Application;
public class AppController extends Application {

    private static volatile AppController Instance;

    @Override
    public void onCreate() {
        super.onCreate();
        Instance = this;
    }

    public static AppController getInstance() {
        if (Instance != null) {
            return Instance;
        }
        return null;
    }

}
