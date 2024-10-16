package com.midterm.appchatt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ChatsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewChats;
    private ChatsAdapter chatsAdapter;
    private FloatingActionButton fabNewChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        recyclerViewChats = findViewById(R.id.recyclerViewChats);
        recyclerViewChats.setLayoutManager(new LinearLayoutManager(this));

        chatsAdapter = new ChatsAdapter();
        recyclerViewChats.setAdapter(chatsAdapter);

        fabNewChat = findViewById(R.id.fabNewChat);
        fabNewChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatsActivity.this, NewChatActivity.class);
                startActivity(intent);
            }
        });
    }
}
