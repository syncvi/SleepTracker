package com.example.sleeptrackerproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DialogFragment extends AppCompatDialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Alarm")
                .setMessage("It's time to wake up!")
                .setPositiveButton("Turn off", (dialogInterface, i) -> {
                    // leaving it empty so system dismises it
                });
        return builder.create();
    }
}
