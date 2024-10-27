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
import com.midterm.appchatt.databinding.ItemUserBinding;
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
        ItemUserBinding binding = ItemUserBinding.inflate(LayoutInflater
                .from(parent.getContext()), parent, false);
        return new UserViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.bind(getItem(position));
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

    class UserViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgAvatar;
        private final TextView tvName;
        private final TextView tvEmail;
        private final View statusIndicator;
        private final TextView tvLastActive;
        private final ItemUserBinding binding;

        public UserViewHolder(@NonNull ItemUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            imgAvatar = binding.imgAvatar;
            tvName = binding.tvName;
            tvEmail = binding.tvEmail;
            statusIndicator = binding.statusIndicator;
            tvLastActive = binding.tvLastActive;

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