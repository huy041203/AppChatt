package com.midterm.appchatt.utils;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.midterm.appchatt.R;
import com.midterm.appchatt.databinding.MainContactBinding;
import com.midterm.appchatt.databinding.NavbarBinding;
import com.midterm.appchatt.ui.activity.MainActivity;
import com.midterm.appchatt.ui.activity.MainContact;
import com.midterm.appchatt.ui.activity.MainSetting;

public class NavbarSupport {

    public static void setup(AppCompatActivity activity, NavbarBinding binding) {

        NavbarOption[] options = {
                new NavbarOption(MainActivity.class, binding.messageIcon,
                        R.drawable.message_highlight),
                new NavbarOption(MainContact.class, binding.contactIcon,
                        R.drawable.contact_list_highlight),
                new NavbarOption(MainSetting.class, binding.settingsIcon,
                        R.drawable.settings_highlights),
        };

        for (NavbarOption option : options) {

            if (!option.get_class().isInstance(activity)) {
                option.get_btn().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, option.get_class());

                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                        activity.startActivity(intent);
                        activity.finish();
                    }
                });
            } else {
                option.get_btn().setImageResource(option.get_highlight());
            }
        }
    }
}

class NavbarOption {
    private Class<?> _class;
    private ImageView _btn;
    private int _highlight;

    public NavbarOption(Class<?> _class, ImageView _btn, int _highlight) {
        this._highlight = _highlight;
        this._btn = _btn;
        this._class = _class;
    }

    public Class<?> get_class() {
        return _class;
    }

    public ImageView get_btn() {
        return _btn;
    }

    public int get_highlight() {
        return _highlight;
    }
}
