package com.midterm.appchatt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity {

    private ListView listViewContacts;
    private ArrayList<Contact> contactList;
    private ContactAdapter contactAdapter;
    private ArrayList<String> selectedContacts = new ArrayList<>();

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

        listViewContacts.setOnItemClickListener((parent, view, position, id) -> {
            Contact selectedContact = contactList.get(position);
            selectedContacts.add(selectedContact.getName());
        });

        findViewById(R.id.buttonConfirmSelection).setOnClickListener(v -> {
            String groupName = String.join(", ", selectedContacts);
            Intent resultIntent = new Intent();
            resultIntent.putExtra("groupName", groupName);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    public class ContactAdapter extends ArrayAdapter<Contact> {

        public ContactAdapter(AppCompatActivity context, ArrayList<Contact> contacts) {
            super(context, 0, contacts);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.contact_item, parent, false);
            }

            Contact contact = getItem(position);
            TextView nameTextView = convertView.findViewById(R.id.contactName);
            nameTextView.setText(contact.getName());

            return convertView;
        }
    }

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
    }
}
