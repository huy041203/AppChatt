package com.midterm.appchatt.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.midterm.appchatt.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactRepository {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference contactsCollection = db.collection("contacts");

    public void addContact(Contact contact) {
        contactsCollection.document(contact.getContactId()).set(contact)
                .addOnSuccessListener(aVoid -> {
                    // Contact added successfully
                })
                .addOnFailureListener(e -> {
                    // Handle error
                });
    }

    public LiveData<List<Contact>> getContactsForUser(String userId) {
        MutableLiveData<List<Contact>> contactsLiveData = new MutableLiveData<>();

        contactsCollection
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Contact> contacts = new ArrayList<>();
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        Contact contact = document.toObject(Contact.class);
                        contacts.add(contact);
                    }
                    contactsLiveData.postValue(contacts);
                })
                .addOnFailureListener(e -> {
                    // Handle error
                });

        return contactsLiveData;
    }
    public LiveData<List<Contact>> getContacts() {
        MutableLiveData<List<Contact>> contactsLiveData = new MutableLiveData<>();

        contactsCollection
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Contact> contacts = new ArrayList<>();
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        Contact contact = document.toObject(Contact.class);
                        contacts.add(contact);
                    }
                    contactsLiveData.postValue(contacts);
                })
                .addOnFailureListener(e -> {
                    // Handle error
                });

        return contactsLiveData;
    }
}