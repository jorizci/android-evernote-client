package com.jorizci.evernoteclient;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.evernote.client.android.EvernoteSession;
import com.jorizci.evernoteclient.activities.LoginActivity;
import com.jorizci.evernoteclient.activities.MainActivity;

/**
 * Controls different lifecycle occurrences in the application life.
 */
public class EvernoteClientLifecycle implements Application.ActivityLifecycleCallbacks {

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Log.d(EvernoteClientApp.APP_LOG_CODE, "Activity " + activity.getClass().getName() + " created");
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.d(EvernoteClientApp.APP_LOG_CODE, "Activity " + activity.getClass().getName() + " started");
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.d(EvernoteClientApp.APP_LOG_CODE, "Activity " + activity.getClass().getName() + " resumed");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.d(EvernoteClientApp.APP_LOG_CODE, "Activity " + activity.getClass().getName() + " paused");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.d(EvernoteClientApp.APP_LOG_CODE, "Activity " + activity.getClass().getName() + " stopped");
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Log.d(EvernoteClientApp.APP_LOG_CODE, "Activity " + activity.getClass().getName() + " save instance state");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.d(EvernoteClientApp.APP_LOG_CODE, "Activity " + activity.getClass().getName() + " destroyed");
        if (activity instanceof LoginActivity && EvernoteSession.getInstance().isLoggedIn()) {
            //Login activity has finished and user is logged correctly then transit to MainActivity
            activity.startActivity(new Intent(activity, MainActivity.class));
        }
    }
}
