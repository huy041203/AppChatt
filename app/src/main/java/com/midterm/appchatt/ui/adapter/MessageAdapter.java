package com.midterm.appchatt.ui.adapter;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.midterm.appchatt.R;
import com.midterm.appchatt.databinding.ItemMessageBinding;
import com.midterm.appchatt.model.Message;

import java.util.List;
import com.midterm.appchatt.model.ReactionType;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Message> messageList;

    private UserAdapter currentUser;
    // Them ChatAdapter sau.

    public MessageAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

    public UserAdapter getCurrentUser() {

        if (currentUser == null) {
            this.currentUser = UserAdapter.getCurrentUser();
        }
        return  this.currentUser;
    }

    @Override
    public int getItemCount() {
        return (this.messageList == null) ? 0 : this.messageList.size();
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new MessageViewHolder(ItemMessageBinding.inflate(layoutInflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);
        holder.bind(this, position, message, getCurrentUser());
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        private ItemMessageBinding binding;

        public MessageViewHolder(ItemMessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(MessageAdapter adapter, int position,
                Message message, UserAdapter currentUser) {
            binding.twMessage.setText(message.getContent());
            String reaction = message.getReaction(currentUser.getUser());

            if (reaction == null) {
                binding.reaction.setVisibility(View.GONE);
            } else {
                switch (reaction) {
                    case ReactionType.LIKE:
                        binding.reaction.setImageResource(R.drawable.reaction_like);
                        break;
                    case ReactionType.LOVE:
                        binding.reaction.setImageResource(R.drawable.reaction_love);
                        break;
                    case ReactionType.HAHA:
                        binding.reaction.setImageResource(R.drawable.reaction_haha);
                        break;
                    case ReactionType.WOW:
                        binding.reaction.setImageResource(R.drawable.reaction_wow);
                        break;
                    case ReactionType.SAD:
                        binding.reaction.setImageResource(R.drawable.reaction_sad);
                        break;
                    case ReactionType.ANGRY:
                        binding.reaction.setImageResource(R.drawable.reaction_angry);
                        break;
                }
            }


            // Loi o reaction

            // Them tinh nang reaction.
            this.binding.reaction.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    message.reactBy(adapter.getCurrentUser().getUser(), ReactionType.LOVE);
                    Toast.makeText(v.getContext(), "Reacted", Toast.LENGTH_SHORT).show();

                    adapter.notifyItemChanged(position);
                    return true;
                }
            });

            this.binding.reaction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    message.reactBy(adapter.getCurrentUser().getUser(), ReactionType.LOVE);
                    Toast.makeText(v.getContext(), "Reacted", Toast.LENGTH_SHORT).show();

                    adapter.notifyItemChanged(position);
                }
            });


            // Set for other user and current user later.
            //  this temporary is set for current user now.
            LinearLayout.LayoutParams params =
                    (LinearLayout.LayoutParams) this.binding.itemMessage.getLayoutParams();
            params.gravity = Gravity.END;
            this.binding.itemMessage.setLayoutParams(params);



        }
    }
}
