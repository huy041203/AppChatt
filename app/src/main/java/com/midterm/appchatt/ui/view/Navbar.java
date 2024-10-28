package com.midterm.appchatt.ui.view;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import com.midterm.appchatt.R;
import com.midterm.appchatt.databinding.MainContactBinding;
import com.midterm.appchatt.databinding.MainMessageBinding;
import com.midterm.appchatt.databinding.MainSettingBinding;
import com.midterm.appchatt.databinding.NavbarBinding;
import com.midterm.appchatt.ui.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;


public class Navbar {

    public static void setup(MainActivity activity) {
        List<NavbarBinding> bar = new ArrayList<>();

        MainMessageBinding messageBinding =
                activity.getBinding();
        MainContactBinding contactBinding =
                activity.getMainContactUI().getBinding();
        MainSettingBinding settingBinding =
                activity.getMainSettingUI().getBinding();


        bar.add(messageBinding.navbarView);
        bar.add(contactBinding.navbarView);
        bar.add(settingBinding.navbarView);

        for (int i = 0; i < bar.size(); ++i) {
            NavbarBinding binding = bar.get(i);
            binding.messageIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.setContentView(messageBinding.getRoot());
                    highlight(binding, OptionType.MESSAGE_LIST);
                }
            });

            binding.contactIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.setContentView(contactBinding.getRoot());
                    highlight(binding, OptionType.CONTACT_LIST);
                }
            });

            binding.settingsIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.setContentView(settingBinding.getRoot());
                    highlight(binding, OptionType.SETTING);
                }
            });

        }

    }

    private static void highlight(NavbarBinding binding, OptionType type) {
        binding.messageIcon.setImageResource(R.drawable.message);
        binding.contactIcon.setImageResource(R.drawable.contact_list);
        binding.settingsIcon.setImageResource(R.drawable.settings);
        if (type == OptionType.MESSAGE_LIST) {
            binding.messageIcon.setImageResource(R.drawable.message_highlight);
        } else if (type == OptionType.CONTACT_LIST) {
            binding.contactIcon.setImageResource(R.drawable.contact_list_highlight);
        } else if (type == OptionType.SETTING) {
            binding.settingsIcon.setImageResource(R.drawable.settings_highlights);
        }
    }

    public static enum OptionType {
        MESSAGE_LIST,
        CONTACT_LIST,
        SETTING,
    }
}
