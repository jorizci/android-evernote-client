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
import android.webkit.WebView;
import android.widget.TextView;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.EvernoteUtil;
import com.evernote.client.android.asyncclient.EvernoteHtmlHelper;
import com.evernote.client.android.type.NoteRef;
import com.evernote.edam.notestore.NoteMetadata;
import com.evernote.edam.type.Note;
import com.jorizci.evernoteclient.EvernoteClientApp;
import com.jorizci.evernoteclient.R;
import com.jorizci.evernoteclient.tasks.GetNote;

import org.scribe.builder.api.EvernoteApi;

public class ReadNote extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Note> {

    private static final String NOTE_ID = "note_guid";
    private static final int NOTE_LOADER = 0;
    private static final String HTML_MIME = "text/html";
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

    public static void startActivity(Context context, NoteMetadata noteMetadata) {
        Intent intent = new Intent(context, ReadNote.class);
        intent.putExtra(NOTE_ID, noteMetadata.getGuid());
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
    public void onLoadFinished(Loader<Note> loader, final Note data) {
        Log.d(EvernoteClientApp.APP_LOG_CODE, "Note: "+data.getTitle()+" "+data.getContent());
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Set title
                TextView noteTitle = (TextView) findViewById(R.id.note_title_text);
                noteTitle.setText(data.getTitle());

                //Remove XML headers and pass to webview.
                String content = new String(data.getContent());
                int startIndex = content.indexOf("<en-note>");
                if(startIndex!=-1){
                    content = content.substring(startIndex+9,
                            content.length()-EvernoteUtil.NOTE_SUFFIX.length());
                }

                WebView webView = (WebView) findViewById(R.id.note_content_webview);
                webView.loadData(content, HTML_MIME, null);
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<Note> loader) {
        //Do nothing
    }
}
