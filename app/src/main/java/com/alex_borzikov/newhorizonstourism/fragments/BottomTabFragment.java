package com.alex_borzikov.newhorizonstourism.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.alex_borzikov.newhorizonstourism.R;
import com.alex_borzikov.newhorizonstourism.adapters.TabAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

// Todo Set normal size !!!
public class BottomTabFragment extends BottomSheetDialogFragment {
    TabAdapter adapter;
    ViewPager pager;

    public static BottomTabFragment newInstance() {
        return new BottomTabFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.buttom_sheet_layout, container,false);

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog d = super.onCreateDialog(savedInstanceState);
        d.setCanceledOnTouchOutside(false);
        return d;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Log.w("Borlehandro", "onViewCreated: params: " + view.getLayoutParams() );

        adapter = new TabAdapter(getChildFragmentManager());
        pager = view.findViewById(R.id.viewPager);
        adapter.addFragment(new QuestListTabFragment(), "Quest List");
        pager.setAdapter(adapter);
    }
}
