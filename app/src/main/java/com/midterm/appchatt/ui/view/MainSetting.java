package com.midterm.appchatt.ui.view;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.midterm.appchatt.databinding.AccountSettingsBinding;
import com.midterm.appchatt.databinding.MainSettingBinding;
import com.midterm.appchatt.ui.activity.MainActivity;
import com.midterm.appchatt.utils.ThemeToggler;

public class MainSetting {
    private MainSettingBinding binding;
    private AccountSettingsBinding accountSettingsBinding;
    private static MainSetting _instance;
    private AppCompatActivity activity;

    private MainSetting(AppCompatActivity activity) {
        this.activity = activity;
        binding = MainSettingBinding.inflate(activity.getLayoutInflater());
        setupAccountSettingLayout(activity);

        binding.settingAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.setContentView(accountSettingsBinding.getRoot());
            }
        });



        binding.settingTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toggle themes
                ThemeToggler.toggleTheme(activity);
            }
        });
    }

    private void setupAccountSettingLayout(AppCompatActivity activity) {
        accountSettingsBinding =
                AccountSettingsBinding.inflate(activity.getLayoutInflater());
        accountSettingsBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setContentView(binding.getRoot());
            }
        });
    }

    public void bindLogoutEvent(SettingLogoutEventListener listener) {
        binding.settingLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.logout();
            }
        });
    }

    public static MainSetting get_instance(AppCompatActivity activity) {
        if (_instance == null) {
            _instance = new MainSetting(activity);
        }
        return _instance;
    }

    public MainSettingBinding getBinding() {
        return binding;
    }

    public static interface SettingLogoutEventListener {
        public void logout();
    }
}
