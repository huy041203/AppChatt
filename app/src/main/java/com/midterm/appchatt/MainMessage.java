package com.midterm.appchatt;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.midterm.appchatt.adapters.ChatAdapter;
import com.midterm.appchatt.databinding.MainMessageBinding;
import com.midterm.appchatt.models.Chat;

import java.util.ArrayList;
import java.util.List;

public class MainMessage extends AppCompatActivity {

    private MainMessageBinding binding;
    private List<Chat> chatList;
    private ChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = MainMessageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.navbarView.messageIcon.setImageResource(R.drawable.message_highlight);

        chatList = new ArrayList<>();
        adapter = new ChatAdapter(chatList);
        binding.rvList.setAdapter(adapter);
        binding.rvList.setLayoutManager(new LinearLayoutManager(this));

        // Add some templates.
        chatList.add(new Chat("", "Nhat Nguyen",
                "Ủa em?", "14:22"));
        chatList.add(new Chat("", "Duy An",
                "OK", "09:34"));
        chatList.add(new Chat("", "Ngoc Huy",
                "Đã xong deadline chưa em?", "Yesterday"));
        chatList.add(new Chat("", "Mommy",
                "Ngủ đi", "24/09/2023"));

    }
}