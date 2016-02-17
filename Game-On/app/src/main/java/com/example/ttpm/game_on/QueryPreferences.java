package com.example.ttpm.game_on;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by Tri Han on 2/11/2016.
 */
public class QueryPreferences {

    private static final String PREF_SESSION_ID = "sessionId";

    public static String getStoredSessionId(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_SESSION_ID, null);
    }

    public static void setStoredSessionId(Context context, String sessionId) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_SESSION_ID, sessionId)
                .apply();
    }

}
