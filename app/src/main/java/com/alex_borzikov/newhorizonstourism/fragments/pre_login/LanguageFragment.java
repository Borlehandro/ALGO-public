package com.alex_borzikov.newhorizonstourism.fragments.pre_login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import com.alex_borzikov.newhorizonstourism.R;

public class LanguageFragment extends Fragment {

    private static final String TAG = "Borlehandro";

    private String language;

    private NavController controller;

    private RadioGroup languageGroup;

    private Button commitLangButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_language, container, false);

        languageGroup = v.findViewById(R.id.languageGroup);
        commitLangButton = v.findViewById(R.id.commitLanguageButton);
        commitLangButton.setVisibility(View.INVISIBLE);

        languageGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                Log.d(TAG, "onCheckedChanged: " + i );

                switch (i) {
                    case R.id.englishButton :
                        language = "english";
                        break;

                    case R.id.russianButton :
                        language = "russian";
                        break;

                    case R.id.chineseButton :
                        language = "chinese";
                        break;
                }

                commitLangButton.setVisibility(View.VISIBLE);
                Log.d(TAG, "onCheckedChanged: " + language);
                // Todo here will be multilingual "Select" text
                commitLangButton.setText(language);
            }
        });

        commitLangButton.setOnClickListener((View view) -> {

            Log.d(TAG, "onCreateView: " + languageGroup.indexOfChild(
                    languageGroup.findViewById(languageGroup.getCheckedRadioButtonId())));

            if(language!=null) {
                Bundle bundle = new Bundle();
                bundle.putString("language", language);
                controller.navigate(R.id.toPreLogin, bundle);
            }
        });

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = Navigation.findNavController(view);
    }

}
