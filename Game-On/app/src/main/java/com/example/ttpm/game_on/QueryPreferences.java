package com.example.ttpm.game_on;

import android.content.Context;
import android.preference.Preference;
import android.preference.PreferenceManager;

/**
 * Created by Tri Han on 2/11/2016.
 */
public class QueryPreferences {

    private static final String PREF_SESSION_ID = "sessionId";
    private static final String PREF_IS_ALARM_ON = "isAlarmOn";
    private static final String PREF_IS_NEW_PROFILE_PIC = "isNewProfilePic";
    private static final String PREF_SEARCH_RANGE = "searchRange";
    private static final String PREF_CUR_LAT = "currentLatitude";
    private static final String PREF_CUR_LNG = "currentLongitude";

    public static String getStoredSessionId(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_SESSION_ID, null);
    }

    public static void setStoredSessionId(Context context, String sessionId) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_SESSION_ID, sessionId)
                .apply();
    }

    public static void removeStoredSessionId(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .remove(PREF_SESSION_ID)
                .commit();
    }

    public static boolean isAlarmOn(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_IS_ALARM_ON, false);
    }

    public static void setAlarmOn(Context context, boolean isOn) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_IS_ALARM_ON, isOn)
                .apply();
    }

    public static boolean isNewProfilePic(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_IS_NEW_PROFILE_PIC, false);
    }

    public static void setNewProfilePic(Context context, boolean isSet) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_IS_NEW_PROFILE_PIC, isSet)
                .apply();
    }

    public static void setSearchRange(Context context, String searchRange) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_SEARCH_RANGE, searchRange)
                .apply();
    }

    public static String getSearchRange(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(PREF_SEARCH_RANGE, "");
    }

    public static Double getStoredLat(Context context) {
        Float lat = PreferenceManager.getDefaultSharedPreferences(context).getFloat(PREF_CUR_LAT, 0);
        return new Double(lat);
    }

    public static void setStoredLat(Context context, Double lat) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putFloat(PREF_CUR_LAT, new Float(lat))
                .apply();
    }

    public static Double getStoredLng(Context context) {
        Float lng = PreferenceManager.getDefaultSharedPreferences(context).getFloat(PREF_CUR_LNG, 0);
        return new Double(lng);
    }

    public static void setStoredLng(Context context, Double lng) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putFloat(PREF_CUR_LNG, new Float(lng))
                .apply();
    }
}
