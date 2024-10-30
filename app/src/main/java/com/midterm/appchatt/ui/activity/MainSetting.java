package com.midterm.appchatt.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.midterm.appchatt.R;
import com.midterm.appchatt.databinding.AccountSettingsBinding;
import com.midterm.appchatt.databinding.MainSettingBinding;
import com.midterm.appchatt.model.ThemeType;
import com.midterm.appchatt.utils.NavbarSupport;
import com.midterm.appchatt.utils.ThemeToggler;

public class MainSetting extends AppliedThemeActivity {

    private MainSettingBinding binding;
    private AccountSettingsBinding accountSettingsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = MainSettingBinding.inflate(getLayoutInflater());
        accountSettingsBinding = AccountSettingsBinding.inflate(getLayoutInflater());
        configAccountSettingsLayout();

        setContentView(binding.getRoot());

        binding.settingAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add account settings intent running here.
                setContentView(accountSettingsBinding.getRoot());
            }
        });



        binding.settingTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toggle themes
                ThemeToggler.toggleTheme(MainSetting.this);
            }
        });


        NavbarSupport.setup(this, binding.navbarView);
    }

    private void configAccountSettingsLayout() {
        accountSettingsBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(binding.getRoot());
            }
        });
    }
}