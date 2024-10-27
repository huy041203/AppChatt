package com.midterm.appchatt.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.midterm.appchatt.R;
import com.midterm.appchatt.model.User;

public class UserAdapter extends ListAdapter<User, UserAdapter.UserViewHolder> {
    private final OnUserClickListener listener;

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
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgAvatar;
        private final TextView tvName;
        private final TextView tvEmail;
        private final View statusIndicator;
        private final TextView tvLastActive;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            tvName = itemView.findViewById(R.id.tvName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            statusIndicator = itemView.findViewById(R.id.statusIndicator);
            tvLastActive = itemView.findViewById(R.id.tvLastActive);

            itemView.setOnClickListener(v -> {
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
    }
}