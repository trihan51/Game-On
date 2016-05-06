package com.example.ttpm.game_on.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.example.ttpm.game_on.PollService;
import com.example.ttpm.game_on.activities.SessionActivity;

/**
 * Created by Tri Han on 2/13/2016.
 */
public abstract class VisibleFragment extends android.support.v4.app.Fragment {
    private static final String TAG = "VisibleFragment";

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(PollService.ACTION_SHOW_NOTIFICATION);
        getActivity().registerReceiver(mOnShowNotification, filter, PollService.PERM_PRIVATE, null);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(mOnShowNotification);
    }

    private BroadcastReceiver mOnShowNotification = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "canceling notification");
            setResultCode(Activity.RESULT_CANCELED);

            int requestCode = intent.getIntExtra(PollService.REQUEST_CODE, 0);
            switch(requestCode) {
                case PollService.REQUEST_CODE_ALERT_USER_OF_SESSION_UPDATES:
                    performActionBasedOnSessionUpdated();
                    break;
                case PollService.REQUEST_CODE_ALERT_USER_OF_SESSION_START:
                    performActionBasedOnSessionStarted();
                    break;
                case PollService.REQUEST_CODE_ALERT_USER_OF_SESSION_CANCELLED:
                    performActionBasedOnSessionCancelled();
                    break;
                default:
                    Log.d(TAG, "Request code not valid. THIS IS A BUG! FIX IT!");
                    break;
            }
        }
    };

    protected abstract void performActionBasedOnSessionUpdated();

    protected abstract void performActionBasedOnSessionStarted();

    protected abstract void performActionBasedOnSessionCancelled();
}
