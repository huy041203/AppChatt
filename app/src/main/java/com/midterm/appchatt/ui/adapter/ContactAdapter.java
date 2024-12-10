package com.midterm.appchatt.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.midterm.appchatt.R;
import com.midterm.appchatt.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    public interface OnUserClickListener {
        public void onUserClick(Contact contact);
    }

    public interface OnContactDeleteListener {
        void onContactDelete(Contact contact);
    }

    private List<Contact> contacts;
    private OnUserClickListener clickListener;
    private OnContactDeleteListener deleteListener;

    public void setOnContactDeleteListener(OnContactDeleteListener listener) {
        this.deleteListener = listener;
    }

    public ContactAdapter(List<Contact> contacts, OnUserClickListener listener) {
        this.contacts = contacts;
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contacts.get(position);
        holder.bind(contact);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName;
        private final ImageView imgAvatar;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            imgAvatar = itemView.findViewById(R.id.img_avatar);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && clickListener != null) {
                    clickListener.onUserClick(contacts.get(position));
                }
            });

            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && deleteListener != null) {
                    deleteListener.onContactDelete(contacts.get(position));
                }
                return true;
            });
        }

        public void bind(Contact contact) {
            if (tvName != null) {
                tvName.setText(contact.getDisplayName());
            }
            
            if (imgAvatar != null && contact.getAvatarUrl() != null && !contact.getAvatarUrl().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(contact.getAvatarUrl())
                        .placeholder(R.drawable.default_avatar)
                        .into(imgAvatar);
            } else if (imgAvatar != null) {
                imgAvatar.setImageResource(R.drawable.default_avatar);
            }
        }
    }

    public void updateList(List<Contact> newContacts) {
        this.contacts = newContacts;
        notifyDataSetChanged();
    }
}