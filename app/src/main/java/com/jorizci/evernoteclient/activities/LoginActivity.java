package com.jorizci.evernoteclient.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.asyncclient.EvernoteSearchHelper;
import com.evernote.client.android.login.EvernoteLoginFragment;
import com.evernote.edam.notestore.NoteFilter;
import com.jorizci.evernoteclient.EvernoteClientApp;
import com.jorizci.evernoteclient.R;

/**
 * Current placeholder class for login purposes
 */
public class LoginActivity extends AppCompatActivity  implements EvernoteLoginFragment.ResultCallback{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EvernoteSession.getInstance().authenticate(LoginActivity.this);

        //Finish this activity
        if(EvernoteSession.getInstance().isLoggedIn()){
            Log.d(EvernoteClientApp.APP_LOG_CODE, "User logged correctly");
            finish();
        }
    }

    @Override
    public void onLoginFinished(boolean successful) {
        Log.d(EvernoteClientApp.APP_LOG_CODE, "onLoginFinished "+successful);
        if (successful) {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}
