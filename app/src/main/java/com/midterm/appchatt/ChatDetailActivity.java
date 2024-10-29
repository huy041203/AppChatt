package com.midterm.appchatt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ChatDetailActivity extends AppCompatActivity {

    private TextView chatTitle;
    private EditText editGroupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatdetail);

        chatTitle = findViewById(R.id.chatTitle);
        editGroupName = findViewById(R.id.editGroupName);

        String contactName = getIntent().getStringExtra("contactName");
        if (contactName != null) {
            chatTitle.setText(contactName);
            editGroupName.setText(contactName);
        }

        findViewById(R.id.buttonSaveGroupName).setOnClickListener(v -> {
            String newGroupName = editGroupName.getText().toString();
            chatTitle.setText(newGroupName);
        });
    }
}
