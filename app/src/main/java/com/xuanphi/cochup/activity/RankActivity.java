package com.xuanphi.cochup.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.xuanphi.cochup.R;
import com.xuanphi.cochup.dto.Category;
import com.xuanphi.cochup.dto.Difficulty;
import com.xuanphi.cochup.fragment.GlobalFragment;
import com.xuanphi.cochup.fragment.UserFragment;
import com.xuanphi.cochup.service.CocHupQuizApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RankActivity extends AppCompatActivity {

    private Button btnHome;

    private Spinner spnCategory;
    private Spinner spnDifficulty;
    private Button btnUser;
    private Button btnGlobal;

    private List<Category> categoryList;
    private List<Difficulty> difficultyList;

    private UserFragment userFragment;
    private GlobalFragment globalFragment;

    public void bindingView() {
        btnHome = findViewById(R.id.btnHome);

        spnCategory = findViewById(R.id.spnCategory);
        spnDifficulty = findViewById(R.id.spnDifficulty);
        btnUser = findViewById(R.id.btnUser);
        btnGlobal = findViewById(R.id.btnGlobal);

        if (userFragment == null) {
            userFragment = new UserFragment();
        }
        if (globalFragment == null) {
            globalFragment = new GlobalFragment();
        }
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

                        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(RankActivity.this, R.layout.spinner_item, categoryNameList);
                        categoryAdapter.setDropDownViewResource(R.layout.spinner_item);
                        spnCategory.setAdapter(categoryAdapter);
                    }

                    @Override
                    public void onFailure(Call<List<Category>> call, Throwable t) {
                        Toast.makeText(RankActivity.this, "No category available.\nError: " + t.toString(), Toast.LENGTH_SHORT).show();
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

                        ArrayAdapter<String> difficultyAdapter = new ArrayAdapter<>(RankActivity.this, R.layout.spinner_item, difficultyNameList);
                        difficultyAdapter.setDropDownViewResource(R.layout.spinner_item);
                        spnDifficulty.setAdapter(difficultyAdapter);
                    }

                    @Override
                    public void onFailure(Call<List<Difficulty>> call, Throwable t) {
                        Toast.makeText(RankActivity.this, "No difficulty available.\nError: " + t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void bindingAction() {
        btnHome.setOnClickListener(this::onBtnHomeClick);

        spnCategory.setOnItemSelectedListener(new CategorySelectedListener());
        spnDifficulty.setOnItemSelectedListener(new DifficultySelectedListener());

        btnUser.setOnClickListener(this::onBtnUserClick);
        btnGlobal.setOnClickListener(this::onBtnGlobalClick);
    }

    private class CategorySelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            setRecords();
            Toast.makeText(RankActivity.this, parent.getItemAtPosition(position).toString() + " selected", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    }

    private class DifficultySelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            setRecords();
            Toast.makeText(RankActivity.this, parent.getItemAtPosition(position).toString() + " selected", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    }

    private void onBtnUserClick(View view) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, userFragment)
                .addToBackStack(null)
                .commit();
        setMyRecords();
    }

    private void onBtnGlobalClick(View view) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, globalFragment)
                .addToBackStack(null)
                .commit();
        setGlobalRecords();
    }

    private void onBtnHomeClick(View view) {
        finish();
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof UserFragment) {
            userFragment = (UserFragment) fragment;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_rank);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bindingView();
        setAdapter();
        bindingAction();
    }

    // Get and set records for fragment
    public void setRecords() {
        // Get category
        String selectedCategory = spnCategory.getSelectedItem() != null ? spnCategory.getSelectedItem().toString() : "Any Topic";

        // Get difficulty
        String selectedDifficulty = spnDifficulty.getSelectedItem() != null ? spnDifficulty.getSelectedItem().toString() : "Easy";

        // Set rank list
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        if (currentFragment instanceof UserFragment && userFragment != null) {
            userFragment.setRecord(selectedCategory, selectedDifficulty);
            Toast.makeText(RankActivity.this, "User fragment.", Toast.LENGTH_SHORT).show();
        } else if (currentFragment instanceof GlobalFragment && globalFragment != null) {
            globalFragment.setRecords(selectedCategory, selectedDifficulty);
            Toast.makeText(RankActivity.this, "Global fragment.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(RankActivity.this, "No fragment available.", Toast.LENGTH_SHORT).show();
        }
    }

    // Get and set records for user fragment
    public void setMyRecords() {
        if (userFragment != null) {
            userFragment.setRecords();
        }
    }

    // Get and set records for global fragment
    public void setGlobalRecords() {
        // Get category
        String selectedCategory = spnCategory.getSelectedItem() != null ? spnCategory.getSelectedItem().toString() : "Any Topic";

        // Get difficulty
        String selectedDifficulty = spnDifficulty.getSelectedItem() != null ? spnDifficulty.getSelectedItem().toString() : "Easy";

        // Set rank list
        if (globalFragment != null) {
            globalFragment.setRecords(selectedCategory, selectedDifficulty);
        }
    }
}