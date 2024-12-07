package com.midterm.appchatt.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.midterm.appchatt.databinding.UserDetailBinding;
import com.midterm.appchatt.model.User;

public class UserDetailActivity extends AppliedThemeActivity {

    private UserDetailBinding binding;
    private String nickname = "";

    private static boolean addingContactFlag = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = UserDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        User currentUser = getIntent().getParcelableExtra("currentUser");
        User user = getIntent().getParcelableExtra("user");

        try {
            binding.tvName.setText(user.getDisplayName());
            binding.tvEmail.setText(user.getEmail());
        } catch (NullPointerException e) {
            binding.tvName.setText("NO_NAME");
            binding.tvEmail.setText("");
        }

        if (addingContactFlag) {
            binding.addThisContact.setVisibility(View.VISIBLE);
            binding.addThisContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    binding.nicknameDialog.setVisibility(View.VISIBLE);
                    binding.btnNNCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            binding.nicknameDialog.setVisibility(View.GONE);
                        }
                    });

                    binding.btnNNGiveUp.setOnClickListener(view -> {
                        setNickName(user.getDisplayName());
                        finishAddingContact();
                    });

                    binding.btnNNConfirm.setOnClickListener(view -> {
                        setNickName(binding.tfNickname.getText().toString().trim());
                        finishAddingContact();
                    });

                }
            });
        } else {
            binding.addThisContact.setVisibility(View.GONE);
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

    public static void flagAddingContact() {
        addingContactFlag = true;
    }

    public static void flagNotAddingContact() {
        addingContactFlag = false;
    }

    private void setNickName(String nickName) {
        this.nickname = nickName;
    }

    private void finishAddingContact() {
        Intent intent = new Intent();
        intent.putExtra("nickname", nickname);
        setResult(RESULT_OK, intent);
        finish();
    }
}
