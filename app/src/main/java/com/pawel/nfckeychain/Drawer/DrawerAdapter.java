package com.pawel.nfckeychain.Drawer;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pawel.nfckeychain.R;

/**
 * Created by Pawel on 2016-11-07.
 */

public class DrawerAdapter extends ArrayAdapter<DrawerItems> {



    public DrawerAdapter(Context context, DrawerItems[] objects) {
        super(context, R.layout.drawer_item_row , objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = ((Activity)getContext()).getLayoutInflater().inflate(R.layout
                .drawer_item_row,parent,false);
        TextView textViewItem = (TextView) convertView.findViewById(R.id.rowTextView);
        textViewItem.setText(getItem(position).getResourceId());
        return convertView;
    }

}
