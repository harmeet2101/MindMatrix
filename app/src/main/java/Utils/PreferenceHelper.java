package Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import Constants.Const;

//import android.util.Log;


public class PreferenceHelper {

    private SharedPreferences app_prefs;
    private Context context;

    private final String DEVICE_TOKEN = "device_token";
    public static final String PROPERTY_APP_VERSION = "appVersion";

    public static final String API_TOKEN = "api_token";
    public static final String USER_DETAILS = "user_details";

    public PreferenceHelper(Context context) {
        app_prefs = context.getSharedPreferences(Const.PREF_NAME,
                Context.MODE_PRIVATE);
        this.context = context;
    }


    public void putDeviceToken(String deviceToken) {
        Editor edit = app_prefs.edit();
        edit.putString(DEVICE_TOKEN, deviceToken);
        edit.commit();
    }

    public String getDeviceToken() {

        Log.d("pavan", "RegID " + app_prefs.getString(DEVICE_TOKEN, null));

        return app_prefs.getString(DEVICE_TOKEN, "");
    }


    public void putAppVersion(int version) {
        Editor edit = app_prefs.edit();
        edit.putInt(PROPERTY_APP_VERSION, version);
        edit.apply();
    }

    public int getAppVersion() {
        return app_prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
    }

    public void putApiToken(String apiToken) {
        Editor edit = app_prefs.edit();
        edit.putString(API_TOKEN, apiToken);
        edit.apply();
    }

    public String getApiToken() {
        return app_prefs.getString(API_TOKEN, "");
    }

    public void putUserDetails(String user_detials) {
        Editor edit = app_prefs.edit();
        edit.putString(USER_DETAILS, user_detials);
        edit.apply();
    }

    public String getUser_detials() {
        return app_prefs.getString(USER_DETAILS, "");
    }


}
