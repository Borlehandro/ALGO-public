package com.alex_borzikov.newhorizonstourism.dialogs;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

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
        builder.setMessage("Спасибо за то, что вы прошли квест! В настоящий момент приложение " +
                "находится в режиме тестирования, поэтому мы будем благодарны за вашу оценку " +
                "и отзыв на Google Play!" + " Вам начислено " + bonuses + " бонусов!")
                .setNeutralButton("Хорошо", (dialog, id) -> {
                    dismiss();
                });

        return builder.create();
    }
}
