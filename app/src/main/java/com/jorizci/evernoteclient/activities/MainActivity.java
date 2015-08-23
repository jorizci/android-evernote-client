package com.jorizci.evernoteclient.activities;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.evernote.client.android.type.NoteRef;
import com.evernote.edam.notestore.NoteMetadata;
import com.jorizci.evernoteclient.EvernoteClientApp;
import com.jorizci.evernoteclient.R;
import com.jorizci.evernoteclient.adapters.NoteMetadataAdapter;
import com.jorizci.evernoteclient.tasks.GetAllNotes;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Main activity retrieves all NoteRef and displays to the user a list with the title
 * of its accessible notes.
 *
 */
public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NoteMetadata>> {

    private static final int NOTE_LOADER = 0;

    private NoteMetadataAdapter noteMetadataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(EvernoteClientApp.APP_LOG_CODE, "Main activity - onCreate");

        setContentView(R.layout.activity_main);

        //Initialize adapter and assign to the listView.
        noteMetadataAdapter = new NoteMetadataAdapter(this);
        ListView noteRefList = (ListView) findViewById(R.id.note_ref_list);
        noteRefList.setAdapter(noteMetadataAdapter);
        noteRefList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NoteMetadata noteMetadata = (NoteMetadata) ((NoteMetadataAdapter)parent.getAdapter()).getItem(position);
                ReadNote.startActivity(MainActivity.this,noteMetadata);
            }
        });

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
                return true;
            case R.id.action_new_note_ocr:
                startActivity(new Intent(this, CreateNoteOcr.class));
                return true;
            case R.id.alphabetically:
                noteMetadataAdapter.orderAlphabetically();
                return true;
            case R.id.creation:
                noteMetadataAdapter.orderCreation();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<NoteMetadata>> onCreateLoader(int id, Bundle args) {
        switch (id){
            case NOTE_LOADER:
                return new GetAllNotes(this);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<NoteMetadata>> loader, List<NoteMetadata> data) {
        //Set note references on adapter.
        noteMetadataAdapter.setData(data);
    }

    @Override
    public void onLoaderReset(Loader<List<NoteMetadata>> loader) {
        //Clean note references
        noteMetadataAdapter.setData(new ArrayList<NoteMetadata>());
    }
}
