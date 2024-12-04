package com.midterm.appchatt.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.midterm.appchatt.R;
import com.midterm.appchatt.model.Message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private final String currentUserId;
    private List<Message> messages;
    private OnMessageLongClickListener longClickListener;

    public interface OnMessageLongClickListener {
        void onMessageLongClick(Message message);
    }

    public void setOnMessageLongClickListener(OnMessageLongClickListener listener) {
        this.longClickListener = listener;
    }

    public MessageAdapter(String currentUserId) {
        this.currentUserId = currentUserId != null ? currentUserId : "";
        this.messages = new ArrayList<>();
    }

    public void setMessages(List<Message> newMessages) {
        if (newMessages == null) {
            Log.e("MessageAdapter", "Attempted to set null message list");
            return;
        }
        Log.d("MessageAdapter", "Setting new messages: " + newMessages.size());
        for (Message msg : newMessages) {
            Log.d("MessageAdapter", "Message: " + msg.toString());
        }
        this.messages = newMessages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout layoutSender;
        private final LinearLayout layoutReceiver;
        private final TextView tvMessageSend;
        private final TextView tvMessageReceive;
        private final ImageView imgMessageSend;
        private final ImageView imgMessageReceive;
        private final TextView tvTimeSend;
        private final TextView tvTimeReceive;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutSender = itemView.findViewById(R.id.layoutSender);
            layoutReceiver = itemView.findViewById(R.id.layoutReceiver);
            tvMessageSend = itemView.findViewById(R.id.tvMessageSend);
            tvMessageReceive = itemView.findViewById(R.id.tvMessageReceive);
            imgMessageSend = itemView.findViewById(R.id.imgMessageSend);
            imgMessageReceive = itemView.findViewById(R.id.imgMessageReceive);
            tvTimeSend = itemView.findViewById(R.id.tvTimeSend);
            tvTimeReceive = itemView.findViewById(R.id.tvTimeReceive);
        }

        public void bind(Message message) {
            String senderId = message.getSenderId();
            if (senderId == null) {
                Log.e("MessageAdapter", "SenderId is null for message: " + message);
                return;
            }

            if (senderId.equals(currentUserId)) {
                layoutSender.setVisibility(View.VISIBLE);
                layoutReceiver.setVisibility(View.GONE);
                if (message.getType().equals("text")) {
                    imgMessageSend.setVisibility(View.GONE);
                    tvMessageSend.setVisibility(View.VISIBLE);
                    tvMessageSend.setText(message.getContent());
                    tvTimeSend.setText(formatTime(message.getTimestamp()));
                } else if (message.getType().equals("image")) {
                    imgMessageSend.setVisibility(View.VISIBLE);
                    tvMessageSend.setVisibility(View.GONE);
                    Glide.with(itemView.getContext())
                            .load(message.getContent())
                            .into(imgMessageSend);
                    tvTimeSend.setText(formatTime(message.getTimestamp()));
                }
                layoutSender.setOnLongClickListener(v -> {
                    if (longClickListener != null) {
                        longClickListener.onMessageLongClick(message);
                    }
                    return true;
                });
            } else {
                layoutSender.setVisibility(View.GONE);
                layoutReceiver.setVisibility(View.VISIBLE);
                if (message.getType().equals("text")) {
                    imgMessageReceive.setVisibility(View.GONE);
                    tvMessageReceive.setVisibility(View.VISIBLE);
                    tvMessageReceive.setText(message.getContent());
                    tvTimeReceive.setText(formatTime(message.getTimestamp()));
                } else if (message.getType().equals("image")) {
                    imgMessageReceive.setVisibility(View.VISIBLE);
                    tvMessageReceive.setVisibility(View.GONE);
                    Glide.with(itemView.getContext())
                            .load(message.getContent())
                            .into(imgMessageReceive);
                    tvTimeReceive.setText(formatTime(message.getTimestamp()));
                }
                layoutReceiver.setOnLongClickListener(v -> {
                    if (longClickListener != null) {
                        longClickListener.onMessageLongClick(message);
                    }
                    return true;
                });
            }
        }

        private String formatTime(Long timestamp) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            return sdf.format(new Date(timestamp));
        }
    }
}
