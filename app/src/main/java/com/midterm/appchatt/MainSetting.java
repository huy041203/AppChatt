package com.midterm.appchatt;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.midterm.appchatt.databinding.MainSettingBinding;

public class MainSetting extends AppCompatActivity {

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
                // Add changing theme event here.
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