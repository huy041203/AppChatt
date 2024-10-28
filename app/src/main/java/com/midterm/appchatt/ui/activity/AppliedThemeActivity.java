package com.midterm.appchatt.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Contacts;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.midterm.appchatt.model.ThemeType;
import com.midterm.appchatt.utils.ThemeToggler;

public class AppliedThemeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ThemeToggler.activeTheme(this);

        super.onCreate(savedInstanceState);
    }
}
