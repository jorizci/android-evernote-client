package com.jorizci.evernoteclient.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.EvernoteUtil;
import com.evernote.client.android.asyncclient.EvernoteCallback;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;
import com.jorizci.evernoteclient.EvernoteClientApp;
import com.jorizci.evernoteclient.R;

import java.util.ArrayList;
import java.util.List;

public class CreateNote extends AppCompatActivity implements EvernoteCallback<List<Notebook>> {

    private List<Notebook> notebooks;
    private MenuItem acceptButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient().listNotebooksAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_note, menu);

        //Disable create new note by default until elements have been loaded.
        acceptButton = menu.findItem(R.id.accept);
        acceptButton.setEnabled(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.accept:
                createNote();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createNote() {
        EditText fieldNoteTitle = (EditText) findViewById(R.id.field_note_title);
        EditText fieldNoteContent = (EditText) findViewById(R.id.field_note_content);
        Spinner fieldNotebookSpinner = (Spinner) findViewById(R.id.notebook_spinner);

        //Check if title is empty and abort the process if it is.
        if(fieldNoteTitle.getText().toString().isEmpty()){
            showError(R.string.empty_title);
            return;
        }

        Note note = new Note();
        note.setTitle(fieldNoteTitle.getText().toString());
        note.setNotebookGuid(notebooks.get(fieldNotebookSpinner.getSelectedItemPosition()).getGuid());
        note.setContent(EvernoteUtil.NOTE_PREFIX + fieldNoteContent.getText().toString() + EvernoteUtil.NOTE_SUFFIX);

        EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient().createNoteAsync(note, new EvernoteCallback<Note>() {
            @Override
            public void onSuccess(Note result) {
                Toast.makeText(getApplicationContext(), "Note Created", Toast.LENGTH_LONG).show();

                Intent data = new Intent();
                setResult(RESULT_OK);
                NavUtils.navigateUpFromSameTask(CreateNote.this);
            }

            @Override
            public void onException(Exception exception) {
                Log.e(EvernoteClientApp.APP_LOG_CODE, "Exception creating note ", exception);
                Toast.makeText(getApplicationContext(), "Unexpected error", Toast.LENGTH_LONG).show();
                NavUtils.navigateUpFromSameTask(CreateNote.this);
            }
        });
    }

    private void showError(final @StringRes int title) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(CreateNote.this);
                alertBuilder.setMessage(title);
                alertBuilder.setCancelable(true);

                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }
        });
    }

    @Override
    public void onSuccess(List<Notebook> result) {
        notebooks = result;
        setNoteCreatorUiContent();
    }

    private void setNoteCreatorUiContent() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setContentView(R.layout.activity_create_note);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                //Notebook initialization
                List<String> list = generateNotebookNameList();
                Log.d(EvernoteClientApp.APP_LOG_CODE, "Notebooks available " + list);
                ArrayAdapter<String> notebookAdapter = new ArrayAdapter<String>(CreateNote.this, android.R.layout.simple_spinner_item, list);
                notebookAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                Spinner notebookSpinner = (Spinner) findViewById(R.id.notebook_spinner);
                notebookSpinner.setAdapter(notebookAdapter);
                notebookSpinner.setSelection(0);

                //Enable create new note.
                acceptButton.setEnabled(true);
            }
        });
    }

    private List<String> generateNotebookNameList() {
        List<String> notebookNames = new ArrayList<>();
        for (Notebook notebook : notebooks) {
            notebookNames.add(notebook.getName());
        }
        return notebookNames;
    }


    @Override
    public void onException(Exception exception) {
        NavUtils.navigateUpFromSameTask(this);
    }
}
