package waed.dev.adminhoria.Utils;

import android.content.Context;
import android.content.SharedPreferences;


public class AppSharedPreferences {
    private enum SharedPreferencesKeys {
        UID
    }

    private static AppSharedPreferences Instance;
    private final SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private AppSharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
    }

    public static AppSharedPreferences getInstance() {
        if (Instance == null) {
            assert AppController.getInstance() != null;
            Instance = new AppSharedPreferences(AppController.getInstance());
        }
        return Instance;
    }

    public void putCurrentUserUID(String uid) {
        editor = sharedPreferences.edit();
        editor.putString(SharedPreferencesKeys.UID.name(), uid);
        editor.apply();
    }

    public String getCurrentUserUID() {
        return sharedPreferences.getString(SharedPreferencesKeys.UID.name(), null);
    }

    // when user logout for instance
    public boolean clear() {
        editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }
}
