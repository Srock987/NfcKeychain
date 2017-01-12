package com.pawel.nfckeychain.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pawel.nfckeychain.CustomCreations.Entry;
import com.pawel.nfckeychain.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Pawel on 2017-01-09.
 */

public class EntryListAdapter extends ArrayAdapter<Entry> {

    private List<Entry> entries;

    public void setEntries(List<Entry> entries){
        this.entries.clear();
        this.entries.addAll(entries);
        notifyDataSetChanged();
    }

    public EntryListAdapter(@NonNull Context context, @NonNull List<Entry> entries) {
        super(context, 0,entries);
        this.entries = entries;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.entry_item_row, parent, false);
        TextView nameTextView = (TextView) convertView.findViewById(R.id.nameTextView);
        TextView timeStampTextView = (TextView) convertView.findViewById(R.id.timeStampTextView);
        nameTextView.setText(getItem(position).getUserName());
        timeStampTextView.setText(getTimeString(getItem(position).getTimeStamp()));
        return convertView;
    }

    private String getTimeString(long time){
        Date inputDate = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy'\n'HH:mm:ss", Locale.GERMAN);
        String dateString = sdf.format(inputDate);
        return  dateString;
    }
}
