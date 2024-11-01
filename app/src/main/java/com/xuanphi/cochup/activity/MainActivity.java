package com.xuanphi.cochup.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.xuanphi.cochup.R;
import com.xuanphi.cochup.dto.Category;
import com.xuanphi.cochup.dto.Difficulty;
import com.xuanphi.cochup.dto.Record;
import com.xuanphi.cochup.service.CocHupQuizApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView tvCurrentScore;
    private TextView tvTopicAndMode;
    private TextView tvUserScore;
    private Spinner spnCategory;
    private Spinner spnDifficulty;
    private Button btnStart;  
    private Button btnLogout;
    private Button btnRank;

    private List<Category> categoryList;
    private List<Difficulty> difficultyList;

    private int userId;
    private long highScore;
    private long currentScore;
    private String currentTopicAndMode;

    private static final int REQUEST_CODE_QUESTION = 1;

    public void bindingView() {
        tvCurrentScore = findViewById(R.id.tvCurrentScore);
        tvTopicAndMode = findViewById(R.id.tvTopicAndMode);
        tvUserScore = findViewById(R.id.tvUserScore);
        spnCategory = findViewById(R.id.spnCategory);
        spnDifficulty = findViewById(R.id.spnDifficulty);
        btnStart = findViewById(R.id.btnStart);
        btnLogout = findViewById(R.id.btnLogout);
        btnRank = findViewById(R.id.btnRank);
    }

    public void setUserId() {
        SharedPreferences preferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        userId = (int) preferences.getLong("USER_ID", 0);
    }

    private void setAdapter() {
        // Set category adapter
        CocHupQuizApiService.getICategoryApiEndpoints()
                .getAllCategory()
                .enqueue(new Callback<List<Category>>() {
                    @Override
                    public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                        List<String> categoryNameList = new ArrayList<>();
                        categoryList = response.body();
                        for (Category category : categoryList) {
                            categoryNameList.add(category.getCategoryName());
                        }

                        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(MainActivity.this, R.layout.spinner_item, categoryNameList);
                        categoryAdapter.setDropDownViewResource(R.layout.spinner_item);
                        spnCategory.setAdapter(categoryAdapter);
                    }

                    @Override
                    public void onFailure(Call<List<Category>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "No category available.\nError: " + t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Set difficulty adapter
        CocHupQuizApiService.getIDifficultyApiEndpoint()
                .getAllDifficulty()
                .enqueue(new Callback<List<Difficulty>>() {
                    @Override
                    public void onResponse(Call<List<Difficulty>> call, Response<List<Difficulty>> response) {
                        List<String> difficultyNameList = new ArrayList<>();
                        difficultyList = response.body();
                        for (Difficulty difficulty : difficultyList) {
                            difficultyNameList.add(difficulty.getDifficultyName());
                        }

                        ArrayAdapter<String> difficultyAdapter = new ArrayAdapter<>(MainActivity.this, R.layout.spinner_item, difficultyNameList);
                        difficultyAdapter.setDropDownViewResource(R.layout.spinner_item);
                        spnDifficulty.setAdapter(difficultyAdapter);
                    }

                    @Override
                    public void onFailure(Call<List<Difficulty>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "No difficulty available.\nError: " + t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void bindingAction() {
        btnStart.setOnClickListener(this::onBtnStartClick);
        btnLogout.setOnClickListener(this::onLogoutClick);
        btnRank.setOnClickListener(this::onBtnRankClick);
    }

    private void onBtnStartClick(View view) {
        // Get category
        String selectedCategory = spnCategory.getSelectedItem().toString();
        long categoryValue = 0;
        for (Category category : categoryList) {
            if (category.getCategoryName().equals(selectedCategory)) {
                categoryValue = category.getCategoryId();
            }
        }

        // Get difficulty
        String selectedDifficulty = spnDifficulty.getSelectedItem().toString();
        String difficultyValue = selectedDifficulty.toLowerCase();

        // Move to quiz activity
        Intent intent = new Intent(MainActivity.this, QuizActivity.class);
        intent.putExtra("selectedCategory", selectedCategory);
        intent.putExtra("selectedDifficulty", selectedDifficulty);

        intent.putExtra("categoryValue", categoryValue);
        intent.putExtra("difficultyValue", difficultyValue);

        startActivityForResult(intent, REQUEST_CODE_QUESTION);
    }

    private void onLogoutClick(View view) {
        // Clear saved user data
        SharedPreferences preferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        // Navigate back to login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void onBtnRankClick(View view) {
        Intent intent = new Intent(MainActivity.this, RankActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bindingView();
        setUserId();
        setAdapter();
        loadCurrentResult();
        bindingAction();
        displayWelcomeMessage();
    }

    // Get recent result and show it on screen
    @SuppressLint("SetTextI18n")
    private void loadCurrentResult() {
        CocHupQuizApiService.getIRecordApiEndpoints()
                .getRecordsByUserId(userId)
                .enqueue(new Callback<List<Record>>() {
                    @Override
                    public void onResponse(Call<List<Record>> call, Response<List<Record>> response) {
                        if (response.body() != null) {
                            List<Record> records = response.body();
                            Record currentRecords = records.get(0);
                            updateCurrentResult(currentRecords.getHighScore(), currentRecords.getCategory().getCategoryName(), currentRecords.getDifficulty().getDifficultyName());
                        } else {
                            updateCurrentResult(0, "", "");
                            Toast.makeText(MainActivity.this, "No record available.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Record>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Error: " + t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        SharedPreferences preferences = getSharedPreferences("share", MODE_PRIVATE);
        currentScore = preferences.getLong("currentScore", 0);
        tvCurrentScore.setText("" + currentScore);
        currentTopicAndMode = preferences.getString("topicAndMode", "");
        tvTopicAndMode.setText(currentTopicAndMode);
    }

    // Receive quiz test result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_QUESTION) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                int score = data.getIntExtra("score",0);
                String topic = data.getStringExtra("topic");
                String mode = data.getStringExtra("mode");
                updateCurrentResult(score, topic, mode);
            }
        }
    }

    // Set new current score
    @SuppressLint("SetTextI18n")
    private void updateCurrentResult(long score, String topic, String mode) {
        spnCategory.setSelection(0);
        spnDifficulty.setSelection(0);

        currentScore = score;
        tvCurrentScore.setText("" + currentScore);
        currentTopicAndMode = topic + " - " + mode;
        tvTopicAndMode.setText(currentTopicAndMode);

        SharedPreferences preferences = getSharedPreferences("share", MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("currentScore", currentScore);
        editor.putString("topicAndMode", currentTopicAndMode);
        editor.apply();
    }

    @SuppressLint("SetTextI18n")
    private void displayWelcomeMessage() {
        Intent intent = getIntent();
        String userName = intent.getStringExtra("USER_NAME");
        tvUserScore.setText(userName + "'s Recent Score");
    }
}