package com.midterm.appchatt.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.midterm.appchatt.R;
import com.midterm.appchatt.databinding.ContactItemBinding;
import com.midterm.appchatt.models.Contact;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ChatViewHolder> {

    private List<Contact> contactList;

    public ContactAdapter(List<Contact> contactList) {
        this.contactList = contactList;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new ChatViewHolder(ContactItemBinding.inflate(layoutInflater, parent, false));
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        Contact chat = contactList.get(position);
        holder.bind(chat);
        if (position + 1 == this.getItemCount()) {
            holder.hideSeparator();
        }
        if (position == 0) {
            holder.showMarginTopForFirstItem();
        }
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }


    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        private ContactItemBinding binding;

        public ChatViewHolder(ContactItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Contact contact) {
            if (!contact.getAvatarUrl().equals("")) {
                // If there is an url of avatar...
            }
            binding.messageDisplayName.setText(contact.getDisplayName());
        }

        // Show margin top for the first item instead of entire recycler view for
        //        avoiding the bug that some items unexpectedly hide (bad effect)
        //        into the void.
        public void showMarginTopForFirstItem() {
            this.binding.firstElementMargin.setVisibility(View.VISIBLE);
        }

        public void hideSeparator() {
            this.binding.chatSeparator.setVisibility(ViewGroup.GONE);
        }
    }
}
