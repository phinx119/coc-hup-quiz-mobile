package com.xuanphi.cochup.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private TextView tvLoginResult, tvRegister;
    private Button btnLogin;

    // Binding views
    private void bindingView() {
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        tvLoginResult = findViewById(R.id.tvLoginResult);
        tvRegister = findViewById(R.id.tvRegister);
        btnLogin = findViewById(R.id.btnLogin);
    }

    // Handling button click actions
    private void handlingAction() {
        btnLogin.setOnClickListener(this::onBtnLoginClick);
        tvRegister.setOnClickListener(this::onTvRegisterClick);
    }

    // Method triggered when login button is clicked
    @SuppressLint("SetTextI18n")
    private void onBtnLoginClick(View view) {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        // Check for empty fields
        if (username.isEmpty() || password.isEmpty()) {
            tvLoginResult.setText("Please enter both username and password");
            Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Call login function
        loginUser(username, password);
    }

    // Method to perform login
    private void loginUser(String username, String password) {
        IUserApiEndpoints iUserApiEndpoints = CocHupQuizApiService.getUserService();
        Call<User> call = iUserApiEndpoints.login(username, password);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Store user data in SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putLong("USER_ID", response.body().getUserId());
                    editor.putString("USER_NAME", response.body().getFullName());
                    editor.putBoolean("IS_LOGGED_IN", true); // To track login state
                    editor.apply();

                    // Navigate to HomeActivity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("USER_NAME", response.body().getFullName());
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

    private void onTvRegisterClick(View view) {
        // Start the Registration Activity
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
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

        // Check if user is already logged in
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("IS_LOGGED_IN", false)) {
            // User is logged in, navigate directly to HomeActivity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("USER_NAME", sharedPreferences.getString("USER_NAME", ""));
            startActivity(intent);
            finish();
        } else {
            // Initialize views
            setContentView(R.layout.activity_login);
            EdgeToEdge.enable(this);
            bindingView();
            handlingAction();
        }
    }
}