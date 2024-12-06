package com.midterm.appchatt.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.midterm.appchatt.databinding.UserDetailBinding;
import com.midterm.appchatt.model.User;

public class UserDetailActivity extends AppliedThemeActivity {

    private UserDetailBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = UserDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        User currentUser = getIntent().getParcelableExtra("currentUser");
        User user = getIntent().getParcelableExtra("user");

        try {
            binding.tvName.setText(user.getDisplayName());
        } catch (NullPointerException e) {
            binding.tvName.setText("NO_NAME");
        }

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.sendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chatId = MainActivity.generateChatId(currentUser.getUserId(), user.getUserId());

                Intent intent = new Intent(UserDetailActivity.this, MessageActivity.class);
                intent.putExtra("currentUser", currentUser);
                intent.putExtra("otherUser", user);
                intent.putExtra("chatId", chatId);
                startActivity(intent);
            }
        });
    }
}
