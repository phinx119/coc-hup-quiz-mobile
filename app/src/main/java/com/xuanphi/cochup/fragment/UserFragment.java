package com.xuanphi.cochup.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.xuanphi.cochup.R;
import com.xuanphi.cochup.adapter.RecordsAdapter;
import com.xuanphi.cochup.dto.Record;
import com.xuanphi.cochup.service.CocHupQuizApiService;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFragment extends Fragment {

    private RecyclerView rcvRecords;

    public void bindingView() {
        rcvRecords = getView().findViewById(R.id.rcvUserRecords);
    }

    public void bindingAction() {

    }

    public void setRecords() {
        CocHupQuizApiService.getIRecordApiEndpoints()
                .getRecordsByUserId(1)
                .enqueue(new Callback<List<Record>>() {
                    @Override
                    public void onResponse(Call<List<Record>> call, Response<List<Record>> response) {
                        if (response.body() != null) {
                            List<Record> records = response.body();
                            bindDataToRecyclerView(records);
                        } else {
                            Toast.makeText(getContext(), "No record available.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Record>> call, Throwable t) {
                        Toast.makeText(getContext(), "Error: " + t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void setRecord(String selectedCategory, String selectedDifficulty) {
        CocHupQuizApiService.getIRecordApiEndpoints()
                .getRecordByUserId(1, selectedCategory, selectedDifficulty)
                .enqueue(new Callback<Record>() {
                    @Override
                    public void onResponse(Call<Record> call, Response<Record> response) {
                        if (response.body() != null) {
                            List<Record> records = List.of(response.body());
                            bindDataToRecyclerView(records);
                        } else {
                            Toast.makeText(getContext(), "No record available.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Record> call, Throwable t) {
                        Toast.makeText(getContext(), "Error: " + t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindingView();
        bindingAction();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    private void bindDataToRecyclerView(List<Record> records) {
        if (rcvRecords != null && getContext() != null) {
            RecordsAdapter adapter = new RecordsAdapter(records);
            rcvRecords.setAdapter(adapter);
            rcvRecords.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }
}