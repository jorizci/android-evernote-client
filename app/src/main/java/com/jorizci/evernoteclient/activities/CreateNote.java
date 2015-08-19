package com.jorizci.evernoteclient.activities;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.jorizci.evernoteclient.EvernoteClientApp;
import com.jorizci.evernoteclient.R;

import java.util.ArrayList;
import java.util.List;

public class CreateNote extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_note);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Notebook initialization
        List<String> list = new ArrayList<String>();
        list.add("Notebook 1");
        list.add("Notebook 2");
        list.add("Notebook 3");
        ArrayAdapter<String> notebookAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        notebookAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner notebookSpinner = (Spinner) findViewById(R.id.notebook_spinner);
        notebookSpinner.setAdapter(notebookAdapter);
        notebookSpinner.setSelection(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.accept:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
