package com.midterm.appchatt;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.midterm.appchatt.databinding.ActivityChatdetailBinding;
import com.midterm.appchatt.model.Message;
import com.midterm.appchatt.ui.adapter.MessageAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChatDetailActivity extends AppCompatActivity {

    private ActivityChatdetailBinding binding;
    MessageAdapter messageAdapter;
    List<Message> messageList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatdetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);
        binding.messageRecyclerView.setAdapter(messageAdapter);
        binding.messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        binding.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = binding.messageInput.getText().toString();
                messageList.add(new Message(messageAdapter.getCurrentUser()
                        .getUser().getUserId(), content, "text"));
                messageAdapter.notifyDataSetChanged();
                binding.messageInput.setText("");
            }
        });

    }
}
