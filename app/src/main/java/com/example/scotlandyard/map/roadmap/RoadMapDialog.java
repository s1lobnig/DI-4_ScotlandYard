package com.example.scotlandyard.map.roadmap;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.example.scotlandyard.R;

public class RoadMapDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.road_map_dialog, null);

        builder.setView(dialogView);
        Bundle args = getArguments();
        RoadMap roadMap = (RoadMap) args.getSerializable("ROAD_MAP");
        LinearLayout linearLayout = dialogView.findViewById(R.id.road_map_list);
        for (Entry e : roadMap.getEntries()) {
            Log.d("VIEW", e.getView(getContext()).toString());
            linearLayout.addView(e.getView(getContext()));
        }
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }
}