package com.example.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    private EditText usernameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private Button signUpButton;
    private TextView signInLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        // Ánh xạ các view từ XML
        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        signUpButton = findViewById(R.id.signUpButton);
        signInLink = findViewById(R.id.signInLink);

        // Xử lý sự kiện khi người dùng nhấn nút Đăng ký
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy dữ liệu từ các trường
                String username = usernameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();

                // Kiểm tra tính hợp lệ
                if (TextUtils.isEmpty(username)) {
                    usernameEditText.setError("Vui lòng nhập tên đăng nhập");
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    emailEditText.setError("Vui lòng nhập email");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    passwordEditText.setError("Vui lòng nhập mật khẩu");
                    return;
                }

                if (TextUtils.isEmpty(confirmPassword)) {
                    confirmPasswordEditText.setError("Vui lòng xác nhận mật khẩu");
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    confirmPasswordEditText.setError("Mật khẩu không khớp");
                    return;
                }

                // Đăng ký thành công (chuyển sang màn hình đăng nhập hoặc màn hình chính)
                Toast.makeText(SignUpActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Xử lý sự kiện khi nhấn vào "Sign In"
        signInLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang màn hình đăng nhập
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
    }
}
