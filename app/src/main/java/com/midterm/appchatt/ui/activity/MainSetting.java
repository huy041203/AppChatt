package com.midterm.appchatt.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.midterm.appchatt.R;
import com.midterm.appchatt.databinding.MainSettingBinding;
import com.midterm.appchatt.model.ThemeType;
import com.midterm.appchatt.utils.ThemeToggler;

public class MainSetting extends AppliedThemeActivity {

    private MainSettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = MainSettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.navbarView.settingsIcon.setImageResource(R.drawable.settings_highlights);


        binding.settingAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add account settings intent running here.
            }
        });



        binding.settingTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toggle themes
                ThemeToggler.toggleTheme(MainSetting.this);
            }
        });


        binding.settingLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add logging out action here.
            }
        });
    }
}