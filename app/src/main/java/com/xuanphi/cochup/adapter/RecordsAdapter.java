package com.xuanphi.cochup.adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xuanphi.cochup.R;
import com.xuanphi.cochup.dto.Record;

import java.util.HashMap;
import java.util.List;

public class RecordsAdapter extends RecyclerView.Adapter<RecordsAdapter.ViewHolder> {

    private List<Record> records;

    public RecordsAdapter(List<Record> records) {
        this.records = records;
    }

    @NonNull
    @Override
    public RecordsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.record_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordsAdapter.ViewHolder holder, int position) {
        Record record = records.get(position);
        holder.setData(record);
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvRank;
        private TextView tvName;
        private TextView tvCategory;
        private TextView tvDifficulty;

        private void bindingView() {
            tvRank = itemView.findViewById(R.id.tvRank);
            tvName = itemView.findViewById(R.id.tvName);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvDifficulty = itemView.findViewById(R.id.tvDifficulty);
        }

        private void bindingAction() {
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bindingView();
            bindingAction();
        }

        public void setData(Record record) {
            tvRank.setText("" + record.getHighScore());
            tvName.setText(record.getUser().getFullName());
            tvCategory.setText(record.getCategory().getCategoryName());
            tvDifficulty.setText(record.getDifficulty().getDifficultyName());
        }
    }
}
