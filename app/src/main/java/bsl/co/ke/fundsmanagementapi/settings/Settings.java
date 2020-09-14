package bsl.co.ke.fundsmanagementapi.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

public class Settings {
    private SharedPreferences settings;

    public Settings(Context context) {
        settings = context.getSharedPreferences("kephis_settings", Context.MODE_PRIVATE);
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public String getBearerToken() {
        return settings.getString("token", "");
    }

    public void setBearerToken(String token) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("token", token);
        editor.apply();
    }

    public String getFullName() {
        return settings.getString("name", "");
    }

    public void setFullName(String token) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("name", token);
        editor.apply();
    }

    public String getEmail() {
        return settings.getString("email", "");
    }

    public void setEmail(String email) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("email", email);
        editor.apply();
    }

    public Boolean IsloggedIn() {
        return settings.getBoolean("is_logged_in", false);
    }

    public void SetIsloggedIn(boolean isloggedin) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("is_logged_in", isloggedin);
        editor.apply();
    }

    public Boolean IsFirstTime() {
        return settings.getBoolean("is_first_time", true);
    }

    public void SetIsFirstTime(boolean isFirstTime) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("is_first_time", isFirstTime);
        editor.apply();
    }
}

