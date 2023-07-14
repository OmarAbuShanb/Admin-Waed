package waed.dev.adminhoria.Utils;

import android.content.Context;
import android.content.SharedPreferences;


public class AppSharedPreferences {
    private enum SharedPreferencesKeys {
      UID,LoggedIn
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
        return sharedPreferences.getString(SharedPreferencesKeys.UID.name(), "");
    }
    public void removeCurrentUserUID() {
        editor = sharedPreferences.edit();
        editor.remove(SharedPreferencesKeys.UID.name());
        editor.apply();
    }

    // LoggedIn
    public void putIsLoggedIn(boolean isLogged) {
        editor = sharedPreferences.edit();
        editor.putBoolean(SharedPreferencesKeys.LoggedIn.name(),true);
        editor.apply();
    }
    public boolean getIsLoggedIn() {
        return sharedPreferences.getBoolean(SharedPreferencesKeys.LoggedIn.name(), false);
    }
    public void removeIsLoggedIn() {
        editor = sharedPreferences.edit();
        editor.remove(SharedPreferencesKeys.LoggedIn.name());
        editor.apply();
    }


    // when user logout for instance
    public void clear() {
        editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}
