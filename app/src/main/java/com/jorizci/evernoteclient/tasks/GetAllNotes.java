package com.jorizci.evernoteclient.tasks;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.asyncclient.EvernoteSearchHelper;
import com.evernote.client.android.type.NoteRef;
import com.evernote.edam.type.Note;
import com.jorizci.evernoteclient.EvernoteClientApp;

import java.util.ArrayList;
import java.util.List;

/**
 * @link AsyncTaskLoader to get all notes by calling EvernoteSearchApi.
 * Right now -all notes- seem to be impossible. API crashes when asked
 * for more than 100000 notes Even though it should support Integer.MAX_VALUE
 * as a get all records indication. It could be changed to calls with offsets.
 *
 * Created by Jorge Izquierdo on 20/08/2015.
 */
public class GetAllNotes extends AsyncTaskLoader<List<NoteRef>> {

    private static final int SEARCH_PAGE_SIZE = 20;
    //Query fails with any number over 100000
    private static final int ALL_NOTES = 100000;

    public GetAllNotes(Context context) {
        super(context);
    }

    @Override
    public List<NoteRef> loadInBackground() {

        EvernoteSearchHelper.Search search = new EvernoteSearchHelper.Search();
        search.setMaxNotes(ALL_NOTES).setPageSize(SEARCH_PAGE_SIZE);

        try {
            EvernoteSearchHelper.Result searchResult = EvernoteSession.getInstance()
                    .getEvernoteClientFactory()
                    .getEvernoteSearchHelper().execute(search);

            return searchResult.getAllAsNoteRef();
        } catch (Exception e) {
            Log.e(EvernoteClientApp.APP_LOG_CODE,e.getMessage(),e);
            return new ArrayList<>();
        }
    }
}
