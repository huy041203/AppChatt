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
    private List<Contact> backupContactList;

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

    // The adapter will temporarily carry a new contact list and backup the old one.
    public void notifyThereIsAFilteringAction(List<Contact> tempContactList) {
        this.backupContactList = this.contactList;
        this.contactList = tempContactList;
    }

    public void finalizeFilteringAction() {
        if (this.backupContactList != null) {
            this.contactList = backupContactList;
        }
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

        public void toggleBottomSeparator(boolean foo) {
            this.binding.chatSeparator.setVisibility(foo ? View.VISIBLE : View.GONE);
        }
    }
}
