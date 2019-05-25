package com.example.scotlandyard.Tickets;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.example.scotlandyard.R;

public class DoubleTicketDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.double_ticket_dialog, null);
        builder.setView(dialogView);

        // Add the buttons
        builder.setPositiveButton(R.string.USE_TICKET, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                //TODO: activate double ticket
            }
        });
        builder.setNegativeButton(R.string.NOT_USE_TICKET, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.cancel();
            }
        });
        return builder.create();
    }
}
