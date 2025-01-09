package com.example.jz_project.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jz_project.R;
import com.example.jz_project.activity.InsertActivity;
import com.example.jz_project.activity.MainActivity;
import com.example.jz_project.entity.Record;

import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {
    private List<Record> messages;
    private Context context;
    private ActivityResultLauncher<Intent> jumpToInsert;

    public RecordAdapter(Context context,ActivityResultLauncher<Intent> jumpToInsert, List<Record> messages) {
        this.messages = messages;
        this.context = context;
        this.jumpToInsert = jumpToInsert;
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_item, parent, false);
        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        Record record = messages.get(position);
        holder.record = record;
        holder.messageTextView.setText(record.toString());

        holder.messageTextView.setOnClickListener(v -> {
            Intent intent = new Intent(context, InsertActivity.class);
            intent.putExtra("insert_type","详情");
            intent.putExtra("record_id",String.valueOf(record.id));
            intent.putExtra("record_money",String.valueOf(record.money));
            intent.putExtra("record_type",record.type);
            intent.putExtra("record_time",record.time);
            intent.putExtra("record_note",record.note);
            jumpToInsert.launch(intent);
        });
    }

    @Override
    public int getItemCount() {
        return this.messages.size();
    }

    public void setMessages(List<Record> messages) {
        this.messages = messages;
    }

    public static class RecordViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTextView;
        public Record record;

        public RecordViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.textRecordItem);
        }
    }
}
