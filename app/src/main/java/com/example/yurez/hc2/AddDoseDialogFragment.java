package com.example.yurez.hc2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TimePicker;

public class AddDoseDialogFragment extends DialogFragment {
    NoticeDialogListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.fragment_add_dose_time, null))
                .setPositiveButton(R.string.title_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mListener.onDialogPositiveClick(AddDoseDialogFragment.this);
                    }
                })
                .setNegativeButton(R.string.title_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AddDoseDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mListener = (NoticeDialogListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mListener.onDialogStarted(AddDoseDialogFragment.this);
        ((TimePicker) getDialog().findViewById(R.id.dialog_timePicker)).setIs24HourView(true);
    }

    public interface NoticeDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogStarted(DialogFragment dialog);
    }
}
