package com.example.myapplication.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.myapplication.Details_setting_activity.Contact_Activity;
import com.example.myapplication.Details_setting_activity.Favourite_Activity;
import com.example.myapplication.Details_setting_activity.Feedback_Activity;
import com.example.myapplication.Details_setting_activity.History_Activity;
import com.example.myapplication.Details_setting_activity.Info_Account_Activity;
import com.example.myapplication.Details_setting_activity.Introduce_Activity;
import com.example.myapplication.Details_setting_activity.Policy_Activity;
import com.example.myapplication.Details_setting_activity.Terms_Activity;
import com.example.myapplication.R;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        Toolbar toolbar = view.findViewById(R.id.tool_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Setting");

        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_ios_24);
        toolbar.setNavigationOnClickListener(view1 -> {
//            Intent i = new Intent(getContext(), HomeActivity.class);
//            startActivity(i);
//                getActivity().finish();
            getActivity().onBackPressed();
        });

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy id của LinearLayout được click
                int id = v.getId();
                Intent intent;
                // Chuyển sang activity khác tương ứng với LinearLayout được click
                switch (id) {
                    case R.id.Set_header_layout:
                        intent = new Intent(getActivity(), Info_Account_Activity.class);
                        startActivity(intent);
                        break;
                    case R.id.Set_info_account:
                        intent = new Intent(getActivity(), Info_Account_Activity.class);
                        startActivity(intent);
                        break;
                    case R.id.Set_watching:
                        intent = new Intent(getActivity(), History_Activity.class);
                        startActivity(intent);
                        break;
                    case R.id.Set_Favourite:
                        intent = new Intent(getActivity(), Favourite_Activity.class);
                        startActivity(intent);
                        break;
                    case R.id.Set_Terms_of_use:
                        intent = new Intent(getActivity(), Terms_Activity.class);
                        startActivity(intent);
                        break;
                    case R.id.Policy:
                        intent = new Intent(getActivity(), Policy_Activity.class);
                        startActivity(intent);
                        break;
                    case R.id.Contact:
                        intent = new Intent(getActivity(), Contact_Activity.class);
                        startActivity(intent);
                        break;
                    case R.id.Feedback:
                        intent = new Intent(getActivity(), Feedback_Activity.class);
                        startActivity(intent);
                        break;
                    case R.id.Introduce:
                        intent = new Intent(getActivity(), Introduce_Activity.class);
                        startActivity(intent);
                        break;
                }
            }
        };

        // Gán cùng một onClickListener cho tất cả các LinearLayout
        LinearLayout linearLayout = view.findViewById(R.id.Set_header_layout);
        linearLayout.setOnClickListener(onClickListener);

        LinearLayout setInfoAccountLayout = view.findViewById(R.id.Set_info_account);
        setInfoAccountLayout.setOnClickListener(onClickListener);

        LinearLayout setHistoryLayout = view.findViewById(R.id.Set_watching);
        setHistoryLayout.setOnClickListener(onClickListener);

        LinearLayout setFavourite = view.findViewById(R.id.Set_Favourite);
        setFavourite.setOnClickListener(onClickListener);

        LinearLayout setTerm = view.findViewById(R.id.Set_Terms_of_use);
        setTerm.setOnClickListener(onClickListener);

        LinearLayout setPolicy = view.findViewById(R.id.Policy);
        setPolicy.setOnClickListener(onClickListener);

        LinearLayout setContact = view.findViewById(R.id.Contact);
        setContact.setOnClickListener(onClickListener);

        LinearLayout setFeedback = view.findViewById(R.id.Feedback);
        setFeedback.setOnClickListener(onClickListener);

        LinearLayout setIntroduce = view.findViewById(R.id.Introduce);
        setIntroduce.setOnClickListener(onClickListener);

        SwitchMaterial theme_switch = view.findViewById(R.id.switch_theme);
        theme_switch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
            else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}