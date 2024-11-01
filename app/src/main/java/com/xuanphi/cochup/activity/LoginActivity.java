package com.xuanphi.cochup.activity;

import android.content.Intent;
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

public class LoginActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword;
    private TextView loginResultTextView, edtRegister;;
    private Button loginBtn;

    // Binding views
    private void bindingView() {
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        loginResultTextView = findViewById(R.id.loginResultTextView);
        loginBtn = findViewById(R.id.loginBtn);
        edtRegister = findViewById(R.id.edtRegister);
    }

    // Handling button click actions
    private void handlingAction() {
        loginBtn.setOnClickListener(this::onLoginButtonClick);
        edtRegister.setOnClickListener(this::onRegisterTextClick);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        bindingView();

        // Set button click listener
        handlingAction();
    }

    // Method triggered when login button is clicked
    private void onLoginButtonClick(View view) {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        // Check for empty fields
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Call login function
        loginUser(username, password);
    }

    private void onRegisterTextClick(View view) {
        // Start the Registration Activity
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    // Method to perform login
    private void loginUser(String username, String password) {
        IUserApiEndpoints iUserApiEndpoints = CocHupQuizApiService.getUserService();
        Call<User> call = iUserApiEndpoints.login(username, password);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Login successful, navigate to HomeActivity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("USER_NAME", response.body().getFullName()); // Modify as per your model
                    startActivity(intent);
                    finish();
                } else {
                    // Show error message if login fails
                    Toast.makeText(LoginActivity.this, "Login failed or user not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // Handle network failure
                Toast.makeText(LoginActivity.this, "API connection error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}