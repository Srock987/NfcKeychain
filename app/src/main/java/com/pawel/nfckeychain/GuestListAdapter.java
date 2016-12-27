package com.pawel.nfckeychain;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pawel.nfckeychain.Activities.EmitNFCActivity;
import com.pawel.nfckeychain.Activities.ManageGuestsActivity;

import java.util.List;

/**
 * Created by Pawel on 2016-12-18.
 */

public class GuestListAdapter extends ArrayAdapter<Guest> {

    private List<Guest> guests;

    public void addGuest(Guest guest){
        if (!guests.contains(guest)) {
            guests.add(guest);
            notifyDataSetChanged();
        }
    }

    public void addGuests(List<Guest> guests){
        this.guests.addAll(guests);
        notifyDataSetChanged();
    }

    private void removeGuest(Guest guest){
        guests.remove(guest);
        notifyDataSetChanged();
    }

    public GuestListAdapter(Context context, List<Guest> guests) {
        super(context, 0, guests);
        this.guests = guests;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (position!=guests.size()) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout
                    .guestlist_item_row, parent, false);
            TextView guestTextView = (TextView) convertView.findViewById(R.id.guestTextView);
            guestTextView.setText(getItem(position).getName());
            ImageButton deleteButton = (ImageButton) convertView.findViewById(R.id.deleteButton);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteGuestDialog(getItem(position));
                }
            });
            ImageButton emitButton = (ImageButton) convertView.findViewById(R.id.emitButton);
            emitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = EmitNFCActivity.startingIntent(getContext(),Utils
                            .NFC_TAG_RECIEVED_GUEST_EMIT,Utils.createEmitGuestMessage(getItem
                            (position)));
                    getContext().startActivity(intent);
                }
            });
        }else {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout
                    .guestlist_add_guest_row, parent, false);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addGuestDialog();
                }
            });
        }
        return convertView;
    }

    private void addGuestDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getContext().getString(R.string.addGuest));
        builder.setMessage(getContext().getString(R.string.enterName));

        final EditText input = new EditText(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);

        builder.setPositiveButton(getContext().getString(R.string.add), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ManageGuestsActivity activity =(ManageGuestsActivity)getContext();
                activity.sendGuestProposition(input.getText().toString());
            }
        });

        builder.setNegativeButton(getContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteGuestDialog(final Guest guest){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setPositiveButton(getContext().getString(R.string.delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeGuest(guest);
                ManageGuestsActivity activity =(ManageGuestsActivity)getContext();
                activity.deleteGuestProposition(guest);
            }
        });
        builder.setNegativeButton(getContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public int getCount() {
        return guests.size()+1;
    }
}
