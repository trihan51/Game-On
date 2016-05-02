package com.example.ttpm.game_on;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.ttpm.game_on.activities.SessionActivity;
import com.example.ttpm.game_on.models.GameOnSession;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by Tri Han on 2/12/2016.
 */
public class PollService extends IntentService {
    private static final String TAG = "PollService";

    private static final int POLL_INTERVAL = 1000 * 10; // 10 seconds
    public static final String ACTION_SHOW_NOTIFICATION = "com.example.ttpm.game_on.SHOW_NOTIFICATION";
    /* Recommended way of doing it in the book
    private static final long POLL_INTERVAL = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
     */
    public static final String PERM_PRIVATE = "com.example.ttpm.game_on.PRIVATE";
    public static final String REQUEST_CODE = "REQUEST_CODE";
    public static final String NOTIFICATION = "NOTIFICATION";

    public static final int REQUEST_CODE_ALERT_USER_OF_SESSION_UPDATES = 0;
    public static final int REQUEST_CODE_ALERT_USER_OF_SESSION_CANCELLED = 1;

    public static Intent newIntent(Context context) {
        return new Intent(context, PollService.class);
    }

    public static void setServiceAlarm(Context context, boolean isOn) {
        Intent i = PollService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (isOn) {
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(), POLL_INTERVAL, pi);
        } else {
            alarmManager.cancel(pi);
            pi.cancel();
        }

        QueryPreferences.setAlarmOn(context, isOn);
    }

    public static boolean isServiceAlarmOn(Context context) {
        Intent i = PollService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }

    public PollService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (!isNetworkAvailableAndConnected()) {
            return;
        }

        // the AlarmManager has requested the service of PollService.
        // Here, we query Parse for the current session.
        // if we are returned an object, that means the session is still open. go ahead and broadcast that the session page should be updated.
        // else if there is an error or no object is returned, then broadcast that the session has been cancelled.

        ParseQuery<GameOnSession> query = GameOnSession.getQuery();
        query.whereEqualTo("objectId", QueryPreferences.getStoredSessionId(this));
        query.findInBackground(new FindCallback<GameOnSession>() {
            @Override
            public void done(List<GameOnSession> list, ParseException e) {
                if (e == null && list.size() == 0) { // if the session has been cancelled by the host
                    // Here, send out a Broadcast telling the user that the session has been cancelled.
                    Resources resources = getResources();
                    Intent i = SessionActivity.newIntent(PollService.this);
                    PendingIntent pi = PendingIntent.getActivity(PollService.this,
                            REQUEST_CODE_ALERT_USER_OF_SESSION_CANCELLED, i, 0);

                    Notification notification = new NotificationCompat.Builder(PollService.this)
                            .setTicker(resources.getString(R.string.session_cancelled_title))
                            .setSmallIcon(android.R.drawable.ic_menu_report_image)
                            .setContentTitle(resources.getString(R.string.session_cancelled_title))
                            .setContentText(resources.getString(R.string.session_cancelled_text))
                            .setContentIntent(pi)
                            .setAutoCancel(true)
                            .build();

                    showBackgroundNotification(REQUEST_CODE_ALERT_USER_OF_SESSION_CANCELLED, notification);
                } else if (e == null){
                    // We are returned an object. broadcast that the session page should be refreshed.
                    Log.i(TAG, "We successfully queried Parse from PollService");
                    Resources resources = getResources();
                    Intent i = SessionActivity.newIntent(PollService.this);
                    PendingIntent pi = PendingIntent.getActivity(PollService.this,
                            REQUEST_CODE_ALERT_USER_OF_SESSION_UPDATES, i, 0);

                    Notification notification = new NotificationCompat.Builder(PollService.this)
                            .setTicker(resources.getString(R.string.session_update_title))
                            .setSmallIcon(android.R.drawable.ic_menu_report_image)
                            .setContentTitle(resources.getString(R.string.session_update_title))
                            .setContentText(resources.getString(R.string.session_update_text))
                            .setContentIntent(pi)
                            .setAutoCancel(true)
                            .build();

                    showBackgroundNotification(REQUEST_CODE_ALERT_USER_OF_SESSION_UPDATES, notification);

                } else {
                    Log.e(TAG, "In PollService.java: Error querying Parse");
                }
            }
        });
    }

    private boolean isNetworkAvailableAndConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();

        return isNetworkConnected;
    }

    private void showBackgroundNotification(int requestCode, Notification notification) {
        Intent i = new Intent(ACTION_SHOW_NOTIFICATION);
        i.putExtra(REQUEST_CODE, requestCode);
        i.putExtra(NOTIFICATION, notification);
        sendOrderedBroadcast(i, PERM_PRIVATE, null, null, Activity.RESULT_OK, null, null);
    }
}
