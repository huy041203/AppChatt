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
    private List<Chat> backupChatList;

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
            // If binding the last item, it will hide the separator for better
            //          performance.
            holder.toggleBottomSeparator(false);
        } else {
            // With other items, the separator is needed for sure.
            holder.toggleBottomSeparator(true);
        }
        if (position == 0) {
            holder.showMarginTopForFirstItem();
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    // The adapter will temporarily carry a new chat list and backup the old one.
    public void notifyThereIsAFilteringAction(List<Chat> tempChatList) {
        this.backupChatList = this.chatList;
        this.chatList = tempChatList;
    }

    public void finalizeFilteringAction() {
        if (this.backupChatList != null) {
            this.chatList = backupChatList;
        }
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

        public void toggleBottomSeparator(boolean foo) {
            this.binding.chatSeparator.setVisibility(foo ? View.VISIBLE : View.GONE);
        }

        // Show margin top for the first item instead of entire recycler view for
        //        avoiding the bug that some items unexpectedly hide (bad effect)
        //        into the void.
        public void showMarginTopForFirstItem() {
            this.binding.firstElementMargin.setVisibility(View.VISIBLE);
        }
    }
}
