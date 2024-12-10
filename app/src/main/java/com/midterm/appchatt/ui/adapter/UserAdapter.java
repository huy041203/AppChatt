package com.midterm.appchatt.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.midterm.appchatt.R;
import com.midterm.appchatt.databinding.ItemUserBinding;
import com.midterm.appchatt.model.User;
import com.midterm.appchatt.ui.activity.MainActivity;
import com.midterm.appchatt.ui.viewmodel.MessageViewModel;
import com.midterm.appchatt.utils.DateUtils;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends ListAdapter<User, UserAdapter.UserViewHolder> {
    private final OnUserClickListener listener;
    private final MessageViewModel messageViewModel;

    public interface OnUserClickListener {
        void onUserClick(User user);
    }

    public UserAdapter(OnUserClickListener listener) {
        super(new DiffUtil.ItemCallback<User>() {
            @Override
            public boolean areItemsTheSame(@NonNull User oldItem, @NonNull User newItem) {
                return oldItem.getUserId().equals(newItem.getUserId());
            }

            @Override
            public boolean areContentsTheSame(@NonNull User oldItem, @NonNull User newItem) {
                return oldItem.getEmail().equals(newItem.getEmail()) &&
                        oldItem.getDisplayName().equals(newItem.getDisplayName()) &&
                        oldItem.getStatus().equals(newItem.getStatus()) &&
                        oldItem.getLastActive() == newItem.getLastActive();
            }
        });
        this.listener = listener;
        this.messageViewModel = new MessageViewModel();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemUserBinding binding = ItemUserBinding.inflate(LayoutInflater
                .from(parent.getContext()), parent, false);
        return new UserViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = getItem(position);
        Log.d("UserAdapter", "Binding position " + position + " with user: " + 
              (user != null ? user.getDisplayName() : "null"));
        holder.bind(user);

        // Lấy chatId
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String chatId = MainActivity.generateChatId(currentUserId, user.getUserId());

        // Observe last message
        messageViewModel.getLastMessage(chatId).observe((LifecycleOwner) holder.itemView.getContext(), message -> {
            if (message != null) {
                // Hiển thị nội dung tin nhắn
                String content = message.getType().equals("text") ? 
                    message.getContent() : "[Hình ảnh]";
                holder.tvLastMessage.setText(content);

                // Sử dụng DateUtils mới
                String timeAgo = DateUtils.getTimeAgo(message.getTimestamp());
                holder.tvLastMessageTime.setText(timeAgo);

                holder.tvLastMessage.setVisibility(View.VISIBLE);
                holder.tvLastMessageTime.setVisibility(View.VISIBLE);
            } else {
                holder.tvLastMessage.setVisibility(View.GONE);
                holder.tvLastMessageTime.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void submitList(List<User> list) {
        Log.d("UserAdapter", "SubmitList called with size: " + (list != null ? list.size() : 0));
        super.submitList(list != null ? new ArrayList<>(list) : null);
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgAvatar;
        private final TextView tvName;
        private final TextView tvEmail;
        private final View statusIndicator;
        private final TextView tvLastActive;
        private final ItemUserBinding binding;
        private final TextView tvLastMessage;
        private final TextView tvLastMessageTime;

        public UserViewHolder(@NonNull ItemUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            imgAvatar = binding.imgAvatar;
            tvName = binding.tvName;
            tvEmail = binding.tvEmail;
            statusIndicator = binding.statusIndicator;
            tvLastActive = binding.tvLastActive;
            tvLastMessage = binding.tvLastMessage;
            tvLastMessageTime = binding.tvLastMessageTime;

            binding.container.setOnClickListener(v -> {
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onUserClick(getItem(position));
                }
            });
        }

        public void bind(User user) {
            // Set user display name or email if name is null
            String displayName = user.getDisplayName();
            if (displayName == null || displayName.isEmpty()) {
                displayName = user.getEmail().split("@")[0];
            }
            tvName.setText(displayName);
            tvEmail.setText(user.getEmail());

            // Load avatar
            if (user.getAvatarUrl() != null && !user.getAvatarUrl().isEmpty()) {
                Glide.with(imgAvatar.getContext())
                        .load(user.getAvatarUrl())
                        .placeholder(R.drawable.default_avatar)
                        .error(R.drawable.default_avatar)
                        .circleCrop()
                        .into(imgAvatar);
            } else {
                imgAvatar.setImageResource(R.drawable.default_avatar);
            }

            // Set online status indicator
            if ("online".equals(user.getStatus())) {
                statusIndicator.setBackgroundResource(R.drawable.status_online);
                tvLastActive.setVisibility(View.GONE);
            } else {
                statusIndicator.setBackgroundResource(R.drawable.status_offline);
                tvLastActive.setVisibility(View.VISIBLE);
                tvLastActive.setText(formatLastActive(user.getLastActive()));
            }
        }

        private String formatLastActive(long lastActive) {
            long currentTime = System.currentTimeMillis();
            long difference = currentTime - lastActive;

            // Convert to minutes
            long minutes = difference / (60 * 1000);

            if (minutes < 1) {
                return "Just now";
            } else if (minutes < 60) {
                return minutes + " minutes ago";
            } else if (minutes < 24 * 60) {
                long hours = minutes / 60;
                return hours + " hours ago";
            } else {
                long days = minutes / (24 * 60);
                return days + " days ago";
            }
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