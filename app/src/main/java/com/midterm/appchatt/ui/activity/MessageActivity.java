package com.midterm.appchatt.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.bumptech.glide.Glide;
import com.midterm.appchatt.model.Message;
import com.midterm.appchatt.model.User;
import com.midterm.appchatt.ui.adapter.MessageAdapter;
import com.midterm.appchatt.ui.viewmodel.MessageViewModel;
import com.midterm.appchatt.databinding.ActivityMessageBinding;

import java.util.List;

public class MessageActivity extends AppliedThemeActivity {
    private ActivityMessageBinding binding;
    private MessageViewModel viewModel;
    private MessageAdapter adapter;
    private User currentUser;
    private String chatId;
    private User otherUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMessageBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MessageViewModel.class);

        // Get data from intent
        currentUser = getIntent().getParcelableExtra("currentUser");
        otherUser = getIntent().getParcelableExtra("otherUser");
        chatId = getIntent().getStringExtra("chatId");

        setupToolbar();
        setupRecyclerView();
        setupMessageInput();
        observeData();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        binding.tvName.setText(otherUser.getName());
        Glide.with(this)
                .load(otherUser.getAvatarUrl())
                .into(binding.imgAvatar);
    }

    private void setupRecyclerView() {
        adapter = new MessageAdapter(currentUser.getUserId());
        binding.recyclerMessages.setAdapter(adapter);
        binding.recyclerMessages.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupMessageInput() {
        binding.btnSend.setOnClickListener(v -> {
            String content = binding.edtMessage.getText().toString().trim();
            if (!content.isEmpty()) {
                Message message = new Message(
                        currentUser.getUserId(),
                        content,
                        "text"
                );
                viewModel.sendMessage(chatId, message);
                binding.edtMessage.setText("");
            }
        });

        binding.btnImage.setOnClickListener(v -> openImagePicker());
    }

    private void observeData() {
        viewModel.getMessages().observe(this, messages -> {
            adapter.updateMessages((List<Message>) messages);
            binding.recyclerMessages.scrollToPosition(messages.size() - 1);
        });

        viewModel.getUserStatus().observe(this, isOnline -> {
            binding.tvStatus.setText(isOnline ? "Online" : "Offline");
        });

        viewModel.loadMessages(chatId);
        viewModel.observeUserStatus(otherUser.getUserId());
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), REQUEST_IMAGE_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICKER && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri uri = data.getData();
                viewModel.sendImage(chatId, uri, currentUser.getUserId());
            }
        }
    }

    private static final int REQUEST_IMAGE_PICKER = 100;
}
