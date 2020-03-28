package com.alex_borzikov.newhorizonstourism.fragments.pre_login;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import com.alex_borzikov.newhorizonstourism.R;

import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class LanguageFragment extends Fragment {

    private static final String TAG = "Borlehandro";

    private NavController controller;

    private RadioGroup languageGroup;

    private ImageButton commitLangButton;

    private Locale myLocale;

    private SharedPreferences preferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getActivity().getSharedPreferences("User", MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_language, container, false);

        languageGroup = v.findViewById(R.id.changeLanguageGroup);
        commitLangButton = v.findViewById(R.id.commitLanguageButton);
        commitLangButton.setVisibility(View.INVISIBLE);

        languageGroup.setOnCheckedChangeListener((radioGroup, i) -> {

            Log.d(TAG, "onCheckedChanged: " + i );

            switch (i) {
                case R.id.changeEnglishButton:
                    setLocale("en");
                    break;

                case R.id.changeRussianButton:
                    setLocale("ru");
                    break;

                case R.id.changeChineseButton:
                    setLocale("zh");
                    break;
            }

            commitLangButton.setVisibility(View.VISIBLE);

        });

        commitLangButton.setOnClickListener((View view) -> {

            Log.d(TAG, "onCreateView: " + languageGroup.indexOfChild(
                    languageGroup.findViewById(languageGroup.getCheckedRadioButtonId())));

            if(preferences.contains("language")) {
                controller.navigate(R.id.toPreLogin);
            }
        });

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = Navigation.findNavController(view);
    }

    public void setLocale(String localeName) {

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("language", localeName);
            editor.apply();

            myLocale = new Locale(localeName);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
//            Intent refresh = new Intent(getContext(), getActivity().getClass());
//            startActivity(refresh);
    }
}
