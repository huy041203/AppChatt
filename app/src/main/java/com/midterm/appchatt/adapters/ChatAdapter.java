package com.midterm.appchatt.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.midterm.appchatt.databinding.ChatItemBinding;
import com.midterm.appchatt.models.Chat;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<Chat> chatList;

    public ChatAdapter(List<Chat> chatList) {
        this.chatList = chatList;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new ChatViewHolder(ChatItemBinding.inflate(layoutInflater, parent, false));
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        Chat chat = chatList.get(position);
        holder.bind(chat);
        if (position + 1 == this.getItemCount()) {
            holder.hideSeparator();
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }


    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        private ChatItemBinding binding;

        public ChatViewHolder(ChatItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Chat chat) {
            if (!chat.getAvatarUrl().equals("")) {
                // If there is an url of avatar...
            }
            binding.lastMessageContent.setText(chat.getLastMessageContent());
            binding.messageDisplayName.setText(chat.getDisplayName());
            binding.timeView.setText(chat.getTime());
        }

        public void hideSeparator() {
            this.binding.chatSeparator.setVisibility(ViewGroup.GONE);
        }
    }
}
