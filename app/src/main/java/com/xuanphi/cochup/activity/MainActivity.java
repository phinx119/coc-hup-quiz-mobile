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

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.xuanphi.cochup.R;
import com.xuanphi.cochup.dto.Category;
import com.xuanphi.cochup.service.CocHupQuizApiService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView tvCurrentScore;
    private TextView tvTopicAndMode;
    private Spinner spnCategory;
    private Spinner spnDifficulty;
    private Button btnStart;

    private HashMap<String, Integer> categoryMap;

    private int highScore;
    private int currentScore;
    private String currentTopicAndMode;

    private static final int REQUEST_CODE_QUESTION = 1;

    public void bindingView() {
        tvCurrentScore = findViewById(R.id.tvCurrentScore);
        tvTopicAndMode = findViewById(R.id.tvTopicAndMode);
        spnCategory = findViewById(R.id.spnCategory);
        spnDifficulty = findViewById(R.id.spnDifficulty);
        btnStart = findViewById(R.id.btnStart);
    }

    private void setAdapter() {
        // Set category adapter
        String[] categories = {
                "Any Topic", "General Knowledge", "Entertainment: Books",
                "Entertainment: Film", "Entertainment: Music",
                "Entertainment: Musicals & Theatres", "Entertainment: Television",
                "Entertainment: Video Games", "Entertainment: Board Games",
                "Science & Nature", "Science: Computers", "Science: Mathematics",
                "Mythology", "Sports", "Geography", "History", "Politics",
                "Art", "Celebrities", "Animals", "Vehicles",
                "Entertainment: Comics", "Science: Gadgets",
                "Entertainment: Japanese Anime & Manga",
                "Entertainment: Cartoon & Animations"
        };

        categoryMap = new HashMap<>();
        categoryMap.put("Any Topic", 0);
        categoryMap.put("General Knowledge", 9);
        categoryMap.put("Entertainment: Books", 10);
        categoryMap.put("Entertainment: Film", 11);
        categoryMap.put("Entertainment: Music", 12);
        categoryMap.put("Entertainment: Musicals & Theatres", 13);
        categoryMap.put("Entertainment: Television", 14);
        categoryMap.put("Entertainment: Video Games", 15);
        categoryMap.put("Entertainment: Board Games", 16);
        categoryMap.put("Science & Nature", 17);
        categoryMap.put("Science: Computers", 18);
        categoryMap.put("Science: Mathematics", 19);
        categoryMap.put("Mythology", 20);
        categoryMap.put("Sports", 21);
        categoryMap.put("Geography", 22);
        categoryMap.put("History", 23);
        categoryMap.put("Politics", 24);
        categoryMap.put("Art", 25);
        categoryMap.put("Celebrities", 26);
        categoryMap.put("Animals", 27);
        categoryMap.put("Vehicles", 28);
        categoryMap.put("Entertainment: Comics", 29);
        categoryMap.put("Science: Gadgets", 30);
        categoryMap.put("Entertainment: Japanese Anime & Manga", 31);
        categoryMap.put("Entertainment: Cartoon & Animations", 32);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(MainActivity.this, R.layout.spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCategory.setAdapter(categoryAdapter);

        // Set difficulty adapter
        String[] difficulties = {
                //"Any Difficulty",
                "Easy", "Medium", "Hard"
        };

        ArrayAdapter<String> difficultyAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, difficulties);
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDifficulty.setAdapter(difficultyAdapter);
    }

    public void bindingAction() {
        btnStart.setOnClickListener(this::onBtnStartClick);
    }

    private void onBtnStartClick(View view) {
        // Get category
        String selectedCategory = spnCategory.getSelectedItem().toString();
        int categoryValue = categoryMap.get(selectedCategory);

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
        setAdapter();
        loadCurrentResult();
        bindingAction();
    }

    // Get recent result and show it on screen
    @SuppressLint("SetTextI18n")
    private void loadCurrentResult() {
        SharedPreferences preferences = getSharedPreferences("share", MODE_PRIVATE);
        currentScore = preferences.getInt("currentScore", 0);
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
                String topicAndMode = data.getStringExtra("topicAndMode");
                updateCurrentResult(score, topicAndMode);
            }
        }
    }

    // Set new current score
    @SuppressLint("SetTextI18n")
    private void updateCurrentResult(int score, String topicAndMode) {
        spnCategory.setSelection(0);
        spnDifficulty.setSelection(0);

        currentScore = score;
        tvCurrentScore.setText("" + currentScore);
        currentTopicAndMode = topicAndMode;
        tvTopicAndMode.setText(currentTopicAndMode);

        SharedPreferences preferences = getSharedPreferences("share", MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("currentScore", currentScore);
        editor.putString("topicAndMode", currentTopicAndMode);
        editor.apply();
    }
}