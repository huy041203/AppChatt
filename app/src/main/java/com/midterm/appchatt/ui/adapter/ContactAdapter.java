package com.midterm.appchatt.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.midterm.appchatt.databinding.ItemContactBinding;
import com.midterm.appchatt.model.Contact;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<Contact> contactList;
    private List<Contact> backupContactList;

    public ContactAdapter(List<Contact> chatList) {
        this.contactList = chatList;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new ContactViewHolder(ItemContactBinding.inflate(layoutInflater, parent, false));
    }

    @NonNull
    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        Contact chat = contactList.get(position);
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
        return contactList.size();
    }

    // The adapter will temporarily carry a new chat list and backup the old one.
    public void notifyThereIsAFilteringAction(List<Contact> tempContactList) {
        this.backupContactList = this.contactList;
        this.contactList = tempContactList;
    }

    public void finalizeFilteringAction() {
        if (this.backupContactList != null) {
            this.contactList = backupContactList;
        }
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        private ItemContactBinding binding;

        public ContactViewHolder(ItemContactBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Contact contact) {
            if (!contact.getAvatarUrl().equals("")) {
                // If there is an url of avatar...
            }
            binding.messageDisplayName.setText(contact.getDisplayName());
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
