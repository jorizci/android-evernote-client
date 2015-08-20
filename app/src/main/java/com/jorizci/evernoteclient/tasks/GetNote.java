package com.jorizci.evernoteclient.tasks;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.evernote.client.android.EvernoteSession;
import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.type.Note;
import com.evernote.thrift.TException;
import com.jorizci.evernoteclient.EvernoteClientApp;

/**
 * Async Task to load a Note by guid.
 * Created by Jorge Izquierdo on 20/08/2015.
 */
public class GetNote extends AsyncTaskLoader<Note> {

    private final String guid;

    public GetNote(Context context, String guid) {
        super(context);
        this.guid = guid;
    }

    @Override
    public Note loadInBackground() {
        try {
            return EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient().getNote(guid,true,false,false,false);
        } catch (EDAMUserException | EDAMSystemException | EDAMNotFoundException | TException e) {
            Log.e(EvernoteClientApp.APP_LOG_CODE,e.getMessage(),e);
            return null;
        }
    }
}
