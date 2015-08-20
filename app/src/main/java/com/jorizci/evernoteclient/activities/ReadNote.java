package com.jorizci.evernoteclient.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.evernote.client.android.type.NoteRef;
import com.jorizci.evernoteclient.EvernoteClientApp;
import com.jorizci.evernoteclient.R;

public class ReadNote extends AppCompatActivity {

    private static final String NOTE_ID = "note_guid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_note);

        if(getIntent().getStringExtra(NOTE_ID)==null || getIntent().getStringExtra(NOTE_ID).isEmpty()){
            //This activity should have a correct note id or should return to main activity.
            NavUtils.navigateUpFromSameTask(this);
        }
        String noteGuid = getIntent().getStringExtra(NOTE_ID);

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
}
