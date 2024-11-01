package com.xuanphi.cochup.activity;

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

    private EditText edtUsername, edtPassword, edtFullname, edtBirtyear;
    private TextView tvResult;
    private Button registerBtn;

    private void bindingView() {
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtFullname = findViewById(R.id.edtFullname);
        edtBirtyear = findViewById(R.id.edtBirtyear);
        tvResult = findViewById(R.id.tvResult);
        registerBtn = findViewById(R.id.registerBtn);
    }

    private void handlingAction() {
        registerBtn.setOnClickListener(this::onRegisterButtonClick);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bindingView();
        handlingAction();
    }

    private void onRegisterButtonClick(View view) {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String fullname = edtFullname.getText().toString().trim();
        String birthday = edtBirtyear.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty() || fullname.isEmpty() || birthday.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User(username, password, fullname, Long.parseLong(birthday));
        registerUser(user);
    }

    private void registerUser(User user) {
        IUserApiEndpoints iUserApiEndpoints = CocHupQuizApiService.getUserService();
        Call<User> call = iUserApiEndpoints.register(user);

        call.enqueue(new Callback<User>() {
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

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                tvResult.setText("API connection error: " + t.getMessage());
            }
        });
    }
}