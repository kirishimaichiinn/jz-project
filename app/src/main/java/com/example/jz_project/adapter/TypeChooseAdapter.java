package com.example.jz_project.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jz_project.R;

import java.util.List;

public class TypeChooseAdapter extends RecyclerView.Adapter<TypeChooseAdapter.TypeViewHolder> {
    private List<String> messages;
    private Context context;

    public TypeChooseAdapter(Context context, List<String> messages) {
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public TypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull TypeViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class TypeViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTextView;

        public TypeViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.textRecordItem);
        }
    }
}
