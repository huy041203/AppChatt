package com.midterm.appchatt.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.midterm.appchatt.R;
import com.midterm.appchatt.ui.adapter.ChatAdapter;
import com.midterm.appchatt.databinding.MainMessageBinding;
import com.midterm.appchatt.model.Chat;

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



        //Search bar configuration.
        binding.searchBar.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    adapter.finalizeFilteringAction();
                }

                List<Chat> filteredList = new ArrayList<>();
                adapter.notifyThereIsAFilteringAction(filteredList);
                for (Chat contact : chatList) {
                    if (contact.getDisplayName().toLowerCase()
                            .contains(charSequence.toString().toLowerCase())) {
                        filteredList.add(contact);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }
}