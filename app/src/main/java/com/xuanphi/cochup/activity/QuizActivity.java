package com.xuanphi.cochup.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.xuanphi.cochup.R;
import com.xuanphi.cochup.dto.Question;
import com.xuanphi.cochup.dto.Quiz;
import com.xuanphi.cochup.service.OpenTriviaDBApiService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizActivity extends AppCompatActivity {

    private TextView tvScore;
    private TextView tvNumberOfQuestion;
    private TextView tvCategory;
    private TextView tvDifficulty;
    private TextView tvCountDown;
    private TextView tvQuestion;

    private RadioGroup rgAnswers;
    private RadioButton rbAnswer1;
    private RadioButton rbAnswer2;
    private RadioButton rbAnswer3;
    private RadioButton rbAnswer4;

    private Button btnConfirmNext;

    private int score;
    private String topicAndMode;

    private static final int MAX_RETRIES = 100;
    private int retryCount = 0;

    private int quizSize;
    private int questionCounter;
    private int count = 0;
    private boolean isAnswered;
    private Question currentQuestion;

    private List<Question> quiz;
    private CountDownTimer globalCountDownTimer;

    public void bindingView() {
        tvScore = findViewById(R.id.tvScore);
        tvNumberOfQuestion = findViewById(R.id.tvNumberOfQuestion);
        tvCategory = findViewById(R.id.tvCategory);
        tvDifficulty = findViewById(R.id.tvDifficulty);
        tvCountDown = findViewById(R.id.tvCountDown);
        tvQuestion = findViewById(R.id.tvQuestion);

        rgAnswers = findViewById(R.id.rgAnswers);
        rbAnswer1 = findViewById(R.id.rbAnswer1);
        rbAnswer2 = findViewById(R.id.rbAnswer2);
        rbAnswer3 = findViewById(R.id.rbAnswer3);
        rbAnswer4 = findViewById(R.id.rbAnswer4);

        btnConfirmNext = findViewById(R.id.btnConfirmNext);

        quiz = new ArrayList<>();
    }

    public void bindingAction() {
        btnConfirmNext.setOnClickListener(this::btnConfirmNextClick);
    }

    private void btnConfirmNextClick(View view) {
        if (isAnswered) {
            showNextQuestion();
        } else {
            // Check required for answer
            if (rbAnswer1.isChecked() || rbAnswer2.isChecked() || rbAnswer3.isChecked() || rbAnswer4.isChecked()) {
                checkAnswer();
            } else {
                Toast.makeText(QuizActivity.this, "Please select your answer", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bindingView();
        receiveData();
        bindingAction();
    }

    // Receive option to get list of quiz
    @SuppressLint("SetTextI18n")
    public void receiveData() {
        // Receive option value
        Intent intent = getIntent();
        String selectedCategory = intent.getStringExtra("selectedCategory");
        int categoryValue = intent.getIntExtra("categoryValue", 0);
        String selectedDifficulty = intent.getStringExtra("selectedDifficulty");
        String difficultyValue = intent.getStringExtra("difficultyValue");

        // Show the receive data
        tvCategory.setText("Topic: " + selectedCategory);
        tvDifficulty.setText("Mode: " + selectedDifficulty);
        topicAndMode = selectedCategory + " - " + selectedDifficulty;

        // Get list of quiz
        fetchQuiz(100, categoryValue, difficultyValue);
    }

    // Fetch quiz until responseCode is 0
    private void fetchQuiz(int amount, int categoryValue, String difficultyValue) {
        OpenTriviaDBApiService.getIQuizApiEndpoints()
                .getAllQuiz(amount, categoryValue, difficultyValue, "multiple")
                .enqueue(new Callback<Quiz>() {
                    @Override
                    public void onResponse(Call<Quiz> call, Response<Quiz> response) {
                        if (response.body() != null && response.body().getResults() != null && response.body().getResponseCode() == 0) {
                            quiz = response.body().getResults();
                            Collections.shuffle(quiz);
                            quizSize = quiz.size();

                            questionCounter = 0;
                            showNextQuestion();
                            startGlobalCountdown(30000);
                            btnConfirmNext.setClickable(true);
                        } else {
                            if (retryCount < MAX_RETRIES) {
                                retryCount++;
                                fetchQuiz(amount - 5, categoryValue, difficultyValue);
                            } else {
                                Toast.makeText(QuizActivity.this, "No questions available", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Quiz> call, Throwable t) {
                        Toast.makeText(QuizActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    // Start count down
    private void startGlobalCountdown(long millisInFuture) {
        globalCountDownTimer = new CountDownTimer(millisInFuture, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvCountDown.setText(String.format(Locale.getDefault(), "%02d:%02d",
                        (int) (millisUntilFinished / 1000) / 60,
                        (int) (millisUntilFinished / 1000) % 60));

                if (millisUntilFinished < (millisInFuture/2)) {
                    tvCountDown.setTextColor(ContextCompat.getColor(QuizActivity.this, R.color.error));
                }
            }

            @Override
            public void onFinish() {
                finishQuestion();
            }
        }.start();
    }

    // Move to next question
    @SuppressLint("SetTextI18n")
    private void showNextQuestion() {
        // Set quiz setting to default
        rgAnswers.clearCheck();
        rbAnswer1.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
        rbAnswer2.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
        rbAnswer3.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
        rbAnswer4.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
        isAnswered = false;
        btnConfirmNext.setText("Confirm Answer");

        // Check quiz is end or not
        if (questionCounter < quizSize) {
            // Get the next question data
            currentQuestion = quiz.get(questionCounter);
            List<String> answers = new ArrayList<>();
            answers.add(currentQuestion.getCorrectAnswer());
            answers.addAll(currentQuestion.getIncorrectAnswers());
            Collections.shuffle(answers);

            // Show the next question on screen
            tvQuestion.setText(currentQuestion.getQuestion());
            rbAnswer1.setText(answers.get(0));
            rbAnswer2.setText(answers.get(1));
            rbAnswer3.setText(answers.get(2));
            rbAnswer4.setText(answers.get(3));

            // Set and show new number of question
            questionCounter++;
            tvNumberOfQuestion.setText("Number of Quiz: " + questionCounter);
        } else {
            finishQuestion();
        }
    }

    // Check correction of answer
    @SuppressLint("SetTextI18n")
    private void checkAnswer() {
        isAnswered = true;
        RadioButton rbSelected = findViewById(rgAnswers.getCheckedRadioButtonId());
        String answer = rbSelected.getText().toString();
        if (answer.equals(currentQuestion.getCorrectAnswer())) {
            score += 1;
            tvScore.setText("Score: " + score);
            rbSelected.setTextColor(ContextCompat.getColor(this, R.color.success));
        } else {
            rbSelected.setTextColor(ContextCompat.getColor(this, R.color.error));
        }

        if (questionCounter < quizSize) {
            btnConfirmNext.setText("Next Question");
        } else {
            btnConfirmNext.setText("Complete");
        }
    }

    // Finish the quiz and return the result
    private void finishQuestion() {
        globalCountDownTimer.cancel();
        Intent resultIntent = new Intent();
        resultIntent.putExtra("score", score);
        resultIntent.putExtra("topicAndMode", topicAndMode);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        count++;
        if (count >= 1) {
            finishQuestion();
        }
        count = 0;
    }
}