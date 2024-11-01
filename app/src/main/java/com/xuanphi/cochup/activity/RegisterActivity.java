package com.xuanphi.cochup.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.xuanphi.cochup.R;
import com.xuanphi.cochup.dto.User;
import com.xuanphi.cochup.service.CocHupQuizApiService;
import com.xuanphi.cochup.service.IUserApiEndpoints;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword, edtFullName, edtBirthYear;
    private TextView tvResult, tvLogin;
    private Button btnRegister;

    private void bindingView() {
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtFullName = findViewById(R.id.edtFullName);
        edtBirthYear = findViewById(R.id.edtBirthYear);
        tvResult = findViewById(R.id.tvResult);
        tvLogin = findViewById(R.id.tvLogin);
        btnRegister = findViewById(R.id.btnRegister);
    }

    private void handlingAction() {
        btnRegister.setOnClickListener(this::onBtnRegisterClick);
        tvLogin.setOnClickListener(this::onTvLoginClick);
    }

    private void onBtnRegisterClick(View view) {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String fullName = edtFullName.getText().toString().trim();
        String birthday = edtBirthYear.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty() || fullName.isEmpty() || birthday.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User(username, password, fullName, Long.parseLong(birthday));
        registerUser(user);
    }

    private void registerUser(User user) {
        CocHupQuizApiService.getUserService()
                .register(user)
                .enqueue(new Callback<User>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            User user = response.body();
                            Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                            finish(); // Optional: Close this activity
                        } else {
                            // Show error message if registration fails
                            tvResult.setText("Registration failed: " + (response.errorBody() != null ? response.message() : "Unknown error"));
                        }
                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        tvResult.setText("API connection error: " + t.getMessage());
                    }
                });
    }

    private void onTvLoginClick(View view) {
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bindingView();
        handlingAction();
    }
}