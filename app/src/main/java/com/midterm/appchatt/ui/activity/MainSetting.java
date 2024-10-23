package com.midterm.appchatt.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.midterm.appchatt.R;
import com.midterm.appchatt.databinding.MainSettingBinding;
import com.midterm.appchatt.model.ThemeType;

public class MainSetting extends AppCompatActivity {

    private MainSettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set theme phai dat truoc  super.onCreate()
        setAppTheme(MainActivity.currentAppTheme);

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
                if (MainActivity.currentAppTheme == ThemeType.LIGHT) {
                    MainActivity.currentAppTheme = ThemeType.DARK;
                } else {
                    MainActivity.currentAppTheme = ThemeType.LIGHT;
                }
                recreate();
            }
        });


        binding.settingLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add logging out action here.
            }
        });
    }

    public void setAppTheme(ThemeType type) {
        if (type == ThemeType.LIGHT) {
            setTheme(R.style.AppTheme);
        } else {
            setTheme(R.style.AppTheme_Dark);
        }
    }
}