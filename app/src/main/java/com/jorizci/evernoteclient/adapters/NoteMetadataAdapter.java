package com.jorizci.evernoteclient.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.evernote.edam.notestore.NoteMetadata;
import com.jorizci.evernoteclient.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Adapter for NoteRef to view object.
 * Created by Jorge Izquierdo on 20/08/2015.
 */
public class NoteMetadataAdapter extends BaseAdapter {

    enum OrderType {
        ALPHA_ASC,
        ALPHA_DES,
        CREATION_ASC,
        CREATION_DES,
    }

    ;

    private List<NoteMetadata> data;
    private LayoutInflater inflater;
    private OrderType currentOrder;

    public NoteMetadataAdapter(Context context) {
        this.data = new ArrayList<>();
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<NoteMetadata> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NoteMetadata noteMetadata = (NoteMetadata) getItem(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.note_ref_item, null);
        }

        TextView noteName = (TextView) convertView.findViewById(R.id.note_ref_title);
        noteName.setText(noteMetadata.getTitle());

        return convertView;
    }

    public void orderAlphabetically() {
        if (currentOrder == null || currentOrder != OrderType.ALPHA_ASC) {
            Collections.sort(data, new Comparator<NoteMetadata>() {
                @Override
                public int compare(NoteMetadata lhs, NoteMetadata rhs) {
                    return lhs.getTitle().compareToIgnoreCase(rhs.getTitle());
                }
            });
            currentOrder = OrderType.ALPHA_ASC;
        } else {
            Collections.sort(data, new Comparator<NoteMetadata>() {
                @Override
                public int compare(NoteMetadata lhs, NoteMetadata rhs) {
                    return rhs.getTitle().compareToIgnoreCase(lhs.getTitle());
                }
            });
            currentOrder = OrderType.ALPHA_DES;
        }
        notifyDataSetChanged();
    }

    public void orderCreation() {
        if (currentOrder == null || currentOrder != OrderType.CREATION_ASC) {
            Collections.sort(data, new Comparator<NoteMetadata>() {
                @Override
                public int compare(NoteMetadata lhs, NoteMetadata rhs) {
                    return lhs.getCreated() < rhs.getCreated() ? -1 : (lhs.getCreated() == rhs.getCreated() ? 0 : 1);
                }
            });
            currentOrder = OrderType.CREATION_ASC;
        } else {
            Collections.sort(data, new Comparator<NoteMetadata>() {
                @Override
                public int compare(NoteMetadata lhs, NoteMetadata rhs) {
                    return rhs.getCreated() < lhs.getCreated() ? -1 : (rhs.getCreated() == lhs.getCreated() ? 0 : 1);
                }
            });
            currentOrder = OrderType.CREATION_DES;
        }
        notifyDataSetChanged();
    }
}
