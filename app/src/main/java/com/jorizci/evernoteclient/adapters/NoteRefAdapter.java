package com.jorizci.evernoteclient.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.evernote.client.android.type.NoteRef;
import com.jorizci.evernoteclient.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for NoteRef to view object.
 * Created by Jorge Izquierdo on 20/08/2015.
 */
public class NoteRefAdapter extends BaseAdapter {

    private List<NoteRef> noteRefs;
    private LayoutInflater inflater;

    public NoteRefAdapter(Context context) {
        this.noteRefs = new ArrayList<>();
        inflater = LayoutInflater.from(context);
    }

    public void setNoteRefs(List<NoteRef> noteRefs) {
        this.noteRefs.clear();
        this.noteRefs.addAll(noteRefs);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return noteRefs.size();
    }

    @Override
    public Object getItem(int position) {
        return noteRefs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NoteRef noteRef = (NoteRef) getItem(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.note_ref_item, null);
        }

        TextView noteName = (TextView) convertView.findViewById(R.id.note_ref_title);
        noteName.setText(noteRef.getTitle());

        return convertView;
    }
}
