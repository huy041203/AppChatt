package com.midterm.appchatt;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ListView;
import java.util.ArrayList;

public class ContactsActivity extends Activity {

    private ListView listViewContacts;
    private ArrayList<Contact> contactList;
    private ContactAdapter contactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        listViewContacts = findViewById(R.id.listViewContacts);

        // Dữ liệu mẫu
        contactList = new ArrayList<>();
        contactList.add(new Contact("Ai Nhien", "ainhien@gmail.com", R.drawable.ic_contact));
        contactList.add(new Contact("Duy An", "tranduyan@example.com", R.drawable.ic_contact));
        contactList.add(new Contact("Gia Bao", "doanhuugiabao@example.com", R.drawable.ic_contact));
        contactList.add(new Contact("Hoang Duyen", "hoangthiduyen@example.com", R.drawable.ic_contact));
        contactList.add(new Contact("Nhat Nguyen", "nhatnguyen123@example.com", R.drawable.ic_contact));
        contactList.add(new Contact("Ngoc Huy", "ngochuy123@example.com", R.drawable.ic_contact));


        contactAdapter = new ContactAdapter(this, contactList);
        listViewContacts.setAdapter(contactAdapter);
    }

    // Lớp Contact dùng để lưu thông tin liên hệ
    public static class Contact {
        private String name;
        private String email;
        private int profileImage;

        public Contact(String name, String email, int profileImage) {
            this.name = name;
            this.email = email;
            this.profileImage = profileImage;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public int getProfileImage() {
            return profileImage;
        }
    }

    // Lớp ContactAdapter dùng để hiển thị danh sách liên hệ
    public class ContactAdapter extends ArrayAdapter<Contact> {

        public ContactAdapter(Activity context, ArrayList<Contact> contacts) {
            super(context, 0, contacts);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.contact_item, parent, false);
            }

            Contact contact = getItem(position);

            TextView nameTextView = convertView.findViewById(R.id.contactName);
            TextView emailTextView = convertView.findViewById(R.id.contactEmail);
            ImageView profileImageView = convertView.findViewById(R.id.contactProfileImage);

            nameTextView.setText(contact.getName());
            emailTextView.setText(contact.getEmail());
            profileImageView.setImageResource(contact.getProfileImage());

            return convertView;
        }
    }
}
