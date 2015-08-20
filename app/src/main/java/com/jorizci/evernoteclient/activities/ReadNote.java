package com.jorizci.evernoteclient.activities;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.evernote.client.android.type.NoteRef;
import com.evernote.edam.type.Note;
import com.jorizci.evernoteclient.EvernoteClientApp;
import com.jorizci.evernoteclient.R;
import com.jorizci.evernoteclient.tasks.GetNote;

public class ReadNote extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Note> {

    private static final String NOTE_ID = "note_guid";
    private static final int NOTE_LOADER = 0;
    private String noteGuid = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_note);

        if(getIntent().getStringExtra(NOTE_ID)==null || getIntent().getStringExtra(NOTE_ID).isEmpty()){
            //This activity should have a correct note id or should return to main activity.
            NavUtils.navigateUpFromSameTask(this);
        }
        noteGuid = getIntent().getStringExtra(NOTE_ID);

        //Prepare note loader.
        getLoaderManager().initLoader(NOTE_LOADER, null, this).forceLoad();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    public static void startActivity(Context context, NoteRef noteRef) {
        Intent intent = new Intent(context, ReadNote.class);
        intent.putExtra(NOTE_ID, noteRef.getGuid());
        context.startActivity(intent);
    }

    @Override
    public Loader<Note> onCreateLoader(int id, Bundle args) {
        switch (id){
            case NOTE_LOADER:
                return new GetNote(this,noteGuid);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Note> loader, Note data) {
        Log.d(EvernoteClientApp.APP_LOG_CODE, "Note: "+data.getTitle()+" "+data.getContent());
    }

    @Override
    public void onLoaderReset(Loader<Note> loader) {

    }
}
