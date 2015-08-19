package com.jorizci.evernoteclient.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.asyncclient.EvernoteSearchHelper;
import com.evernote.edam.notestore.NoteFilter;
import com.jorizci.evernoteclient.EvernoteClientApp;
import com.jorizci.evernoteclient.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
