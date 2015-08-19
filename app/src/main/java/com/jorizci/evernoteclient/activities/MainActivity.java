package com.jorizci.evernoteclient.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.asyncclient.EvernoteCallback;
import com.evernote.client.android.asyncclient.EvernoteSearchHelper;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.type.User;
import com.jorizci.evernoteclient.EvernoteClientApp;
import com.jorizci.evernoteclient.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(EvernoteClientApp.APP_LOG_CODE, "Main activity - onCreate");

        final EvernoteSearchHelper.Search mSearch = new EvernoteSearchHelper.Search().setMaxNotes(20).setPageSize(10);

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    //AuthenticationResult result = EvernoteSession.getInstance().getEvernoteClientFactory().getUserStoreClient().authenticate("jorizci", "r4gn4r0k", BuildConfig.EVERNOTE_CONSUMER_KEY, BuildConfig.EVERNOTE_CONSUMER_SECRET, false);
                    Log.d(EvernoteClientApp.APP_LOG_CODE, "Tests");
                    Log.d(EvernoteClientApp.APP_LOG_CODE, "Username " + EvernoteSession.getInstance().getEvernoteClientFactory().getUserStoreClient().getUser().getUsername());
                    Log.d(EvernoteClientApp.APP_LOG_CODE, "Name " + EvernoteSession.getInstance().getEvernoteClientFactory().getUserStoreClient().getUser().getName());

                    Log.d(EvernoteClientApp.APP_LOG_CODE, "Nose " + EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient().findNoteCounts(new NoteFilter(), false).getNotebookCounts());


                    try {
                        EvernoteSearchHelper.Result searchResult = EvernoteSession.getInstance()
                                .getEvernoteClientFactory()
                                .getEvernoteSearchHelper().execute(mSearch);
                        Log.d(EvernoteClientApp.APP_LOG_CODE, searchResult.getAllAsNoteRef()+"");
                    } catch (Exception e) {
                        Log.e(EvernoteClientApp.APP_LOG_CODE, "Exception", e);
                    }
                } catch (Exception e) {
                    Log.e(EvernoteClientApp.APP_LOG_CODE, "Exception", e);
                }
                EvernoteSession.getInstance().getEvernoteClientFactory().getUserStoreClient().getUserAsync(new EvernoteCallback<User>() {
                    @Override
                    public void onSuccess(User result) {
                        Log.d(EvernoteClientApp.APP_LOG_CODE, "Async desde fuera de un thread... "+result.getName());
                    }

                    @Override
                    public void onException(Exception exception) {

                    }
                });
            }
        });

        thread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_new_note:
                startActivity(new Intent(this, CreateNote.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
