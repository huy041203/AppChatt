package com.midterm.appchatt.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.midterm.appchatt.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactRepository {
    private final FirebaseFirestore db;
    private final DatabaseReference database;
    private final MutableLiveData<List<Contact>> contacts;

    public interface DeleteContactListener {
        void onSuccess();
        void onFailure(Exception e);
    }

    public ContactRepository() {
        db = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        contacts = new MutableLiveData<>();
    }

    public void deleteContact(Contact contact, DeleteContactListener listener) {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        
        // Trước tiên kiểm tra xem contact có thuộc về user hiện tại không
        db.collection("contacts")
            .whereEqualTo("userId", currentUserId)
            .whereEqualTo("contactId", contact.getContactId())
            .get()
            .addOnSuccessListener(querySnapshot -> {
                if (!querySnapshot.isEmpty()) {
                    // Lấy document ID của contact cần xóa
                    String documentId = querySnapshot.getDocuments().get(0).getId();
                    
                    // Xóa contact từ Firestore
                    db.collection("contacts")
                        .document(documentId)
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            // Sau khi xóa thành công trong Firestore, xóa trong Realtime Database
                            database.child("contacts")
                                .child(currentUserId)
                                .child(contact.getContactId())
                                .removeValue()
                                .addOnSuccessListener(aVoid2 -> {
                                    // Refresh danh sách contacts
                                    getContactsForUser(currentUserId);
                                    if (listener != null) {
                                        listener.onSuccess();
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    if (listener != null) {
                                        Log.e("ContactRepository", "Error deleting from Realtime DB", e);
                                        listener.onFailure(e);
                                    }
                                });
                        })
                        .addOnFailureListener(e -> {
                            Log.e("ContactRepository", "Error deleting from Firestore", e);
                            if (listener != null) {
                                listener.onFailure(e);
                            }
                        });
                } else {
                    if (listener != null) {
                        listener.onFailure(new Exception("Contact not found or permission denied"));
                    }
                }
            })
            .addOnFailureListener(e -> {
                if (listener != null) {
                    listener.onFailure(e);
                }
            });
    }

    public void addContact(Contact contact) {
        db.collection("contacts").document(contact.getContactId()).set(contact)
                .addOnSuccessListener(aVoid -> {
                    // Contact added successfully
                })
                .addOnFailureListener(e -> {
                    // Handle error
                });
    }

    public LiveData<List<Contact>> getContactsForUser(String userId) {
        MutableLiveData<List<Contact>> contactsLiveData = new MutableLiveData<>();
        
        db.collection("contacts")
            .whereEqualTo("userId", userId)
            .addSnapshotListener((value, error) -> {
                if (error != null) {
                    Log.e("ContactRepository", "Error getting contacts", error);
                    return;
                }

                List<Contact> contactsList = new ArrayList<>();
                if (value != null) {
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        Contact contact = doc.toObject(Contact.class);
                        if (contact != null) {
                            contact.setContactId(doc.getId());
                            contactsList.add(contact);
                        }
                    }
                }
                contactsLiveData.setValue(contactsList);
            });

        return contactsLiveData;
    }

    public LiveData<List<Contact>> getContacts() {
        return contacts;
    }
}