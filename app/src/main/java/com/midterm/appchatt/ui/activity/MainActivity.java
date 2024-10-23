package com.midterm.appchatt.ui.activity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.midterm.appchatt.R;
import com.midterm.appchatt.model.ThemeType;

public class MainActivity extends AppliedThemeActivity {

    private TextView txtWelcome;
    private Button btnLogout;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Ánh xạ view
        txtWelcome = findViewById(R.id.txtWelcome);
        btnLogout = findViewById(R.id.btnLogout);

        // Kiểm tra user hiện tại
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Hiển thị email của user
            txtWelcome.setText("Welcome " + currentUser.getEmail());
        } else {
            // Nếu chưa login thì chuyển về LoginActivity
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

        // Xử lý sự kiện logout
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Kiểm tra lại user mỗi khi activity start
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
    }
}