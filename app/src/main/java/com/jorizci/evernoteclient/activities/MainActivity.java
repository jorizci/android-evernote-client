package com.jorizci.evernoteclient.activities;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.EvernoteUtil;
import com.evernote.client.android.asyncclient.EvernoteCallback;
import com.evernote.client.android.asyncclient.EvernoteSearchHelper;
import com.evernote.client.android.type.NoteRef;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.type.NoteSortOrder;
import com.evernote.edam.type.User;
import com.jorizci.evernoteclient.EvernoteClientApp;
import com.jorizci.evernoteclient.R;
import com.jorizci.evernoteclient.adapters.NoteRefAdapter;
import com.jorizci.evernoteclient.tasks.GetAllNotes;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Main activity retrieves all NoteRef and displays to the user a list with the title
 * of its accessible notes.
 *
 */
public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NoteRef>> {

    private static final int NOTE_LOADER = 0;

    private NoteRefAdapter noteRefAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(EvernoteClientApp.APP_LOG_CODE, "Main activity - onCreate");

        setContentView(R.layout.activity_main);

        //Initialize adapter and asign to the listView.
        noteRefAdapter = new NoteRefAdapter(this);
        ListView noteRefList = (ListView) findViewById(R.id.note_ref_list);
        noteRefList.setAdapter(noteRefAdapter);

        //Prepare note loader.
        getLoaderManager().initLoader(NOTE_LOADER, null, this).forceLoad();
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

    @Override
    public Loader<List<NoteRef>> onCreateLoader(int id, Bundle args) {
        switch (id){
            case NOTE_LOADER:
                return new GetAllNotes(this);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<NoteRef>> loader, List<NoteRef> data) {
        //Set note references on adapter.
        noteRefAdapter.setNoteRefs(data);
    }

    @Override
    public void onLoaderReset(Loader<List<NoteRef>> loader) {
        //Clean note references
        noteRefAdapter.setNoteRefs(new ArrayList<NoteRef>());
    }
}
