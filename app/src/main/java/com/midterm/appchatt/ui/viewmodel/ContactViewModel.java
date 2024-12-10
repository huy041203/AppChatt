package com.midterm.appchatt.ui.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.midterm.appchatt.data.repository.ContactRepository;
import com.midterm.appchatt.model.Contact;

import java.util.List;

public class ContactViewModel extends ViewModel {
    private final FirebaseFirestore db;
    private final MutableLiveData<List<Contact>> contacts = new MutableLiveData<>();
    private ContactRepository contactRepository;

    public ContactViewModel() {
        db = FirebaseFirestore.getInstance();
        contactRepository = new ContactRepository();
    }

    public void addContact(Contact contact) {
        contactRepository.addContact(contact);
    }

    public LiveData<List<Contact>> getContactsForUser(String userId) {
        return contactRepository.getContactsForUser(userId);
    }
    public LiveData<List<Contact>> getContacts() {
        return contactRepository.getContacts();
    }

    public void deleteContact(Contact contact) {
        contactRepository.deleteContact(contact, new ContactRepository.DeleteContactListener() {
            @Override
            public void onSuccess() {
                Log.d("ContactViewModel", "Contact deleted successfully");
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("ContactViewModel", "Error deleting contact", e);
            }
        });
    }
}
