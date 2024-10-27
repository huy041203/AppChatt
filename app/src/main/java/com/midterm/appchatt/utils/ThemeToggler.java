package com.midterm.appchatt.utils;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.midterm.appchatt.R;
import com.midterm.appchatt.model.ThemeType;

public class ThemeToggler {

    // Se duoc doc tu firebase.
    private static ThemeType theme = ThemeType.LIGHT;

    public static void lightTheme(AppCompatActivity activity) {
        theme = ThemeType.LIGHT;
        activity.recreate();
    }

    public static void darkTheme(AppCompatActivity activity) {
        theme = ThemeType.DARK;
        activity.recreate();
    }

    // Bat/tat sang toi.
    public static void toggleTheme(AppCompatActivity activity) {
        if (theme == ThemeType.LIGHT) {
            darkTheme(activity);
        } else {
            lightTheme(activity);
        }
    }

    public static void setThemeToOtherValue(ThemeType type,
                                            AppCompatActivity activity) {
        theme = type;
        activity.recreate();
    }

    public static void activeTheme(Context context) {
        if (theme == ThemeType.LIGHT) {
            context.setTheme(R.style.AppTheme);
        } else {
            context.setTheme(R.style.AppTheme_Dark);
        }
    }

    public static ThemeType getTheme() {
        return theme;
    }
}
