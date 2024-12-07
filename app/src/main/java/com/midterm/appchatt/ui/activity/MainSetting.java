package com.midterm.appchatt.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.midterm.appchatt.R;
import com.midterm.appchatt.data.firebase.FirebaseAuthHelper;
import com.midterm.appchatt.databinding.AccountSettingsBinding;
import com.midterm.appchatt.databinding.MainSettingBinding;
import com.midterm.appchatt.model.ThemeType;
import com.midterm.appchatt.ui.viewmodel.AuthViewModel;
import com.midterm.appchatt.utils.NavbarSupport;
import com.midterm.appchatt.utils.NoUserFoundException;
import com.midterm.appchatt.utils.RetrivingDataException;
import com.midterm.appchatt.utils.ThemeToggler;

import com.google.firebase.auth.FirebaseAuth;
import com.midterm.appchatt.utils.UpdatingEmailException;

public class MainSetting extends AppliedThemeActivity {

    private MainSettingBinding binding;
    private AccountSettingsBinding accountSettingsBinding;
    private AuthViewModel authViewModel;

    private String _current_email;
    private String _current_display_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        binding = MainSettingBinding.inflate(getLayoutInflater());
        accountSettingsBinding = AccountSettingsBinding.inflate(getLayoutInflater());
        configAccountSettingsLayout();

        setContentView(binding.getRoot());

        binding.settingAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                 Add account settings intent running here.
                try {
                    authViewModel.getUserData(new FirebaseAuthHelper.UserDataCallback() {
                        @Override
                        public void onUserDataRetrived(String displayName, String email) {
                            _current_email = email;
                            _current_display_name = displayName;
                        }

                        @Override
                        public void onError(Exception exception) {
                            _current_email = null;
                            _current_display_name = null;
                        }
                    });

                    if (_current_email == null
                        || _current_display_name == null) {

                        throw new RetrivingDataException();
                    }

                    accountSettingsBinding.edtEmail.setText(_current_email);
                    accountSettingsBinding.edtUsername.setText(_current_display_name);
                }
                catch (RetrivingDataException e2) {
                    Toast.makeText(MainSetting.this, e2.toString(),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                setContentView(accountSettingsBinding.getRoot());
            }
        });



        binding.settingTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toggle themes
                ThemeToggler.toggleTheme(MainSetting.this);
            }
        });

        binding.settingLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainSetting.this);
                builder.setTitle("Are you sure?");
                builder.setMessage("Do you want to logout?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        logout();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        NavbarSupport.setup(this, binding.navbarView);
    }

    private void configAccountSettingsLayout() {
        accountSettingsBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(binding.getRoot());
            }
        });

        accountSettingsBinding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String modified_email = accountSettingsBinding.edtEmail
                        .getText().toString();
                String modified_display_name = accountSettingsBinding.edtUsername
                        .getText().toString();
                String modified_password = accountSettingsBinding.edtPassword
                        .getText().toString();
                String retype_password = accountSettingsBinding.edtRetypePassword
                        .getText().toString();

                if (!validateDisplayName(modified_display_name)) {
                    Toast.makeText(MainSetting.this,
                            "Email is invalid!!",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else if (!modified_display_name.equals(_current_display_name)) {
                    authViewModel.modifyUserDisplayName(modified_display_name);
                }

                if (!validateEmail(modified_email)) {
                    Toast.makeText(MainSetting.this,
                            "Email is invalid!!",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else if (!modified_email.equals(_current_email)) {
                    authViewModel.modifyUserEmail(modified_email, new FirebaseAuthHelper.OnUpdateEmailListener() {
                        @Override
                        public void onSuccess() {
                            PASSWORD_CASE _case = validatePassword(modified_password, retype_password);
                            if (_case == PASSWORD_CASE.PASSWORD_EMPTY) {
                                // Do nothing because this will not change the password
                                //    if the user leaves it blank.
                            } else if (_case == PASSWORD_CASE.PASSWORD_UNMATCH) {
                                Toast.makeText(MainSetting.this,
                                        "Password is unmatch!!",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            } else if (_case == PASSWORD_CASE.PASSWORD_MATCH) {
                                authViewModel.modifyUserPassword(modified_password);
                            }

                            setContentView(binding.getRoot());
                        }

                        @Override
                        public void onFailure(Exception exception) {
                            Toast.makeText(MainSetting.this,
                                    exception.toString(),
                                    Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(MainSetting.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private boolean validateEmail(String email) {
        if (email.contains("@") && email.contains(".")) {
            return true;
        } else {
            return false;
        }
    }

    private PASSWORD_CASE validatePassword(String password, String retype_password) {
        if ("".equals(password)) {
            return PASSWORD_CASE.PASSWORD_EMPTY;
        }
        if (password.equals(retype_password)) {
            return PASSWORD_CASE.PASSWORD_MATCH;
        }
        return PASSWORD_CASE.PASSWORD_UNMATCH;
    }

    private boolean validateDisplayName(String displayName) {
        if ("".equals(displayName)) {
            return false;
        }
        return true;
    }

    private static enum PASSWORD_CASE {
        PASSWORD_MATCH,
        PASSWORD_EMPTY,
        PASSWORD_UNMATCH,
    }
}