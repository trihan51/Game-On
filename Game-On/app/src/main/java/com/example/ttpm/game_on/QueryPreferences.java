package com.example.ttpm.game_on;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by Tri Han on 2/11/2016.
 */
public class QueryPreferences {

    private static final String PREF_SESSION_ID = "sessionId";
    private static final String PREF_IS_ALARM_ON = "isAlarmOn";

    public static String getStoredSessionId(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_SESSION_ID, null);
    }

    public static void setStoredSessionId(Context context, String sessionId) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_SESSION_ID, sessionId)
                .apply();
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

}
