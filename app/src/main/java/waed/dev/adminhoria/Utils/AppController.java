package waed.dev.adminhoria.Utils;

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
