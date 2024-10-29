package com.midterm.appchatt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class ChatsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewChats;
    private ChatsAdapter chatsAdapter;
    private FloatingActionButton fabNewChat;
    private List<Chat> chatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        recyclerViewChats = findViewById(R.id.recyclerViewChats);
        recyclerViewChats.setLayoutManager(new LinearLayoutManager(this));

        chatList = new ArrayList<>();
        chatsAdapter = new ChatsAdapter(chatList);
        recyclerViewChats.setAdapter(chatsAdapter);

        fabNewChat = findViewById(R.id.fabNewChat);
        fabNewChat.setOnClickListener(v -> {
            Intent intent = new Intent(ChatsActivity.this, ContactsActivity.class);
            startActivityForResult(intent, 1);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String groupName = data.getStringExtra("groupName");
            chatList.add(new Chat(groupName, "New group created"));
            chatsAdapter.notifyDataSetChanged();
        }
    }
}
