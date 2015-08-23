package com.jorizci.evernoteclient;

import android.app.Application;

import com.evernote.client.android.EvernoteSession;

/**
 * Main application class. We overload the standard application model to configure and initialize
 * Evernote API Service as singleton.
 * <p/>
 * Created by Jorge Izquierdo on 19/08/2015.
 */
public class EvernoteClientApp extends Application {

    public static final String APP_LOG_CODE = "EvernoteClientApp";

    @Override
    public void onCreate() {
        super.onCreate();

        //Get Evernote builder from this context.
        EvernoteSession.Builder evernoteBuilder = new EvernoteSession.Builder(this);

        //Build a Evernote Service Handler as singleton to use in dalvik app scope.
        //At the moment we are gonna use the developer token to comprehend the
        //API Service calls. User login will be postponed as it doesn't seem to work
        //in the base example of the sdk right now.
        evernoteBuilder.setEvernoteService(BuildConfig.EVERNOTE_SERVICE)
                .setForceAuthenticationInThirdPartyApp(true)
                .build(BuildConfig.EVERNOTE_CONSUMER_KEY, BuildConfig.EVERNOTE_CONSUMER_SECRET)
                .asSingleton();
        registerActivityLifecycleCallbacks(new EvernoteClientLifecycle());
    }

}
