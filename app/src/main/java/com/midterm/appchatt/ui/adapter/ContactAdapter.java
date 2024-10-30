package com.midterm.appchatt.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.midterm.appchatt.databinding.ItemContactBinding;
import com.midterm.appchatt.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<Contact> contactList;
    private List<Contact> backupContactList;

    public ContactAdapter(List<Contact> chatList) {
        this.contactList = chatList != null ? chatList : new ArrayList<>();
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new ContactViewHolder(ItemContactBinding.inflate(layoutInflater, parent, false));
    }

    public void updateList(List<Contact> newList) {
        this.contactList = newList != null ? newList : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        Contact chat = contactList.get(position);
        holder.bind(chat);
        if (position + 1 == this.getItemCount()) {
            holder.toggleBottomSeparator(false);
        } else {
            holder.toggleBottomSeparator(true);
        }
        if (position == 0) {
            holder.showMarginTopForFirstItem();
        }
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public void notifyThereIsAFilteringAction(List<Contact> tempContactList) {
        this.backupContactList = this.contactList;
        this.contactList = tempContactList != null ? tempContactList : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void finalizeFilteringAction() {
        if (this.backupContactList != null) {
            this.contactList = backupContactList;
            notifyDataSetChanged();
        }
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        private final ItemContactBinding binding;

        public ContactViewHolder(ItemContactBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Contact contact) {
            if (contact != null) {
                String avatarUrl = contact.getAvatarUrl();
                if (avatarUrl != null && !avatarUrl.isEmpty()) {
                    // If there is an url of avatar...
                }

                String displayName = contact.getDisplayName();
                binding.messageDisplayName.setText(displayName != null ? displayName : "");
            }
        }

        public void toggleBottomSeparator(boolean foo) {
            if (binding != null && binding.chatSeparator != null) {
                binding.chatSeparator.setVisibility(foo ? View.VISIBLE : View.GONE);
            }
        }

        public void showMarginTopForFirstItem() {
            if (binding != null && binding.firstElementMargin != null) {
                binding.firstElementMargin.setVisibility(View.VISIBLE);
            }
        }
    }
}