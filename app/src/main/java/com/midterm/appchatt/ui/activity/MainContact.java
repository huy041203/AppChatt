package com.midterm.appchatt.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.midterm.appchatt.R;
import com.midterm.appchatt.data.repository.AuthRepository;
import com.midterm.appchatt.model.User;
import com.midterm.appchatt.ui.adapter.ContactAdapter;
import com.midterm.appchatt.databinding.MainContactBinding;
import com.midterm.appchatt.model.Contact;
import com.midterm.appchatt.ui.viewmodel.AuthViewModel;
import com.midterm.appchatt.ui.viewmodel.UserViewModel;
import com.midterm.appchatt.utils.NavbarSupport;
import com.midterm.appchatt.ui.viewmodel.ContactViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainContact extends AppliedThemeActivity implements
        ContactAdapter.OnUserClickListener {

    private MainContactBinding binding;
    private List<Contact> contactList;
    private ContactAdapter adapter;
    private ContactViewModel contactViewModel;
    private UserViewModel userViewModel;

    private ActivityResultLauncher<Intent> launcher;
    private final NickNameAdapter nickNameAdapter = new NickNameAdapter();
    private ContactResultCallback contactResultCallback = null;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);


        initUserDetailIntentLauncher();


        binding = MainContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupViewModel();
        setupRecyclerView();
        setupAddContactButton();
        fetchContacts();
        configSearchBar();
        NavbarSupport.setup(this, binding.navbarView);
    }

    private void initUserDetailIntentLauncher() {
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            nickNameAdapter.setNickname(data.getStringExtra("nickname"));
                        }
                        if (contactResultCallback != null) {
                            contactResultCallback.onContactResult(nickNameAdapter);
                        }
                    }
                });
    }

    private void setupViewModel() {
        contactViewModel = new ViewModelProvider(this).get(ContactViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }
    private void setupAddContactButton() {
        binding.fabAddContact.setOnClickListener(v -> showAddContactDialog());
    }
    private void showAddContactDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_contact, null);

        EditText emailInput = dialogView.findViewById(R.id.editTextEmail);

        builder.setView(dialogView)
                .setTitle("Add New Contact")
                .setPositiveButton("Search", (dialog, id) -> {
                    String email = emailInput.getText().toString().trim();

                    if (!TextUtils.isEmpty(email)) {
                        findAndAddContact(email);
                    } else {
                        Toast.makeText(MainContact.this,
                                "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void findAndAddContact(String email) {
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        final User currentUser;
        if (fUser != null) {
            currentUser = new User(
                    fUser.getUid(),
                    fUser.getEmail(),
                    fUser.getDisplayName()
            );
        } else {
            Toast.makeText(this, "There is an error while loading database...", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        String currentUserEmail = currentUser.getEmail();
        if (email.equals(currentUserEmail)) {
            Toast.makeText(this, "Không thể thêm email của chính bạn", Toast.LENGTH_SHORT).show();
            return;
        }
        // Kiểm tra email đã tồn tại trong danh sách contact
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        contactViewModel.getContactsForUser(currentUserId).observe(this, contacts -> {

            ContactExistCallback contactExistCallback = new ContactExistCallback() {
                @Override
                public void onContactExist() {
                    Toast.makeText(MainContact.this, "Liên hệ này đã tồn tại", Toast.LENGTH_SHORT).show();
                    UserDetailActivity.flagNotAddingContact();
                }

                @Override
                public void onContactNotExist() {
                    UserDetailActivity.flagAddingContact();

                    // Tiếp tục tìm và thêm contact
                    userViewModel.findUserByEmail(email).observe(MainContact.this, user -> {
                        if (user != null) {
                            startIntentForAddingContact(email, currentUser, user, nnAdapter -> {
                                String nickname = nnAdapter.getNickname();
                                if ("".equals(nickname)) {
                                    nickname = user.getDisplayName();
                                }
                                Contact newContact = new Contact(
                                        currentUserId,
                                        user.getUserId(),
                                        nickname,
                                        user.getAvatarUrl()
                                );
                                contactViewModel.addContact(newContact);
                                Toast.makeText(MainContact.this, "Thêm liên hệ thành công", Toast.LENGTH_SHORT).show();
                                fetchContacts();
                            });
                        } else {
                            Toast.makeText(MainContact.this, "Không tìm thấy người dùng với email này",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            };

            if (contacts != null) {
                for (Contact contact : contacts) {
                    // Sử dụng UserViewModel để lấy email của contact
                    userViewModel.getUserById(contact.getContactId()).observe(this, user -> {
                        if (user != null && user.getEmail().equals(email)) {
                            contactExistCallback.onContactExist();
                        }
                    });
                }
            }

            // If onContactExist is called, this method will be end soon (and will not do anything).
            contactExistCallback.onContactNotExist();
        });
    }

    private void startIntentForAddingContact(
            String email, User currentUser, User user,
            ContactResultCallback callback) {
        this.contactResultCallback = callback;
        Intent intent = new Intent(MainContact.this, UserDetailActivity.class);

        intent.putExtra("currentUser", currentUser);
        intent.putExtra("user", user);

        launcher.launch(intent);

    }

    private void setupRecyclerView() {
        contactList = new ArrayList<>();
        adapter = new ContactAdapter(contactList, this);
        adapter.setOnContactDeleteListener(contact -> {
            new AlertDialog.Builder(this)
                .setTitle("Xóa liên hệ")
                .setMessage("Bạn có chắc chắn muốn xóa liên hệ này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    contactViewModel.deleteContact(contact);
                    String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    // Refresh lại danh sách sau khi xóa
                    fetchContacts();
                })
                .setNegativeButton("Hủy", null)
                .show();
        });
        binding.rvList.setAdapter(adapter);
        binding.rvList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void fetchContacts() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();



        contactViewModel.getContactsForUser(currentUserId).observe(this, contacts -> {


            if (contacts != null && !contacts.isEmpty()) {
                contactList.clear();
                contactList.addAll(contacts);
                adapter.notifyDataSetChanged();


                binding.rvList.setVisibility(View.VISIBLE);
            } else {

                binding.rvList.setVisibility(View.GONE);
            }
        });

    }

    private void configSearchBar() {
        binding.searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchText = charSequence.toString().toLowerCase().trim();

                if (searchText.isEmpty()) {
                    adapter.updateList(contactList);
                    return;
                }

                List<Contact> filteredList = new ArrayList<>();
                for (Contact contact : contactList) {
                    if (contact.getDisplayName().toLowerCase().contains(searchText)) {
                        filteredList.add(contact);
                    }
                }
                adapter.updateList(filteredList);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @Override
    public void onUserClick(Contact contact) {
        Intent intent = new Intent(this, UserDetailActivity.class);
        LiveData<User> liveData = userViewModel.getUserById(contact.getContactId());
        User user = liveData.getValue();
        if (user == null) {
            user = new User(contact.getContactId(), "", contact.getDisplayName());
        }
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        User currentUser = null;
        if (fUser != null) {
            currentUser = new User(
                    fUser.getUid(),
                    fUser.getEmail(),
                    fUser.getDisplayName()
            );
        }
        if (user == null || currentUser == null) {
            return;
        }

        intent.putExtra("currentUser", currentUser);
        intent.putExtra("user", user);

        // Chi hien thi thong tin lien lac, khong hien thi gi khac.
        UserDetailActivity.flagNotAddingContact();

        startActivity(intent);
    }

    public static class NickNameAdapter {
        private String nickname;

        public NickNameAdapter() {
            this.nickname = "";
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getNickname() {
            return this.nickname;
        }
    }

    public interface ContactExistCallback {
        void onContactExist();

        void onContactNotExist();
    }

    public interface ContactResultCallback {
        void onContactResult(NickNameAdapter nnAdapter);
    }
}