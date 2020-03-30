package com.alex_borzikov.newhorizonstourism.dialogs;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.alex_borzikov.newhorizonstourism.R;

public class FinishDialog extends DialogFragment {

    private String bonuses;

    public FinishDialog(String bonuses) {
        this.bonuses = bonuses;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Todo Fix text
        builder.setMessage(getString(R.string.completedMessage) + "\n" + getString(R.string.bonusesCollectedTitle) + " " + bonuses)
                .setNeutralButton(getString(R.string.permissionAllow), (dialog, id) -> dismiss());

        return builder.create();
    }
}
