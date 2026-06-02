package main.com.cineramamaps.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    private static final String APP_PREF = "iMobilityPreference";
    private static SharedPreferences sp;
    public static String KEY_CURRENCY = "KEY_CURRENCY";
    public static String KEY_EXACHANGE_JSON = "KEY_EXACHANGE_JSON";


    public static void save(Context context, String key, String value) {
        sp = context.getSharedPreferences(APP_PREF, 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static String get(Context context, String key) {
        sp = context.getSharedPreferences(APP_PREF, 0);
        return sp.getString(key, "");
    }

    public static void saveInt(Context context, String key, int value) {
        sp = context.getSharedPreferences(APP_PREF, 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    public static int getInt(Context context, String key) {
        sp = context.getSharedPreferences(APP_PREF, 0);
        return sp.getInt(key, 0);
    }


    public static void saveBool(Context context, String key, Boolean value) {
        sp = context.getSharedPreferences(APP_PREF, 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public static Boolean getBool(Context context, String key) {
        sp = context.getSharedPreferences(APP_PREF, 0);
        return sp.getBoolean(key, false);
    }

    public static void clearPreference(Context mContext) {
        sp = mContext.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.clear();
        edit.apply();
    }








}
