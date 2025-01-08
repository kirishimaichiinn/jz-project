package com.example.jz_project.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jz_project.R;
import com.example.jz_project.activity.InsertActivity;
import com.example.jz_project.activity.MainActivity;
import com.example.jz_project.activity.TypeManageActivity;
import com.example.jz_project.entity.Type;

import java.util.List;

public class TypeChooseAdapter extends RecyclerView.Adapter<TypeChooseAdapter.TypeViewHolder> {
    public List<Type> messages;
    private Context context;

    public TypeChooseAdapter(Context context, List<Type> messages) {
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public TypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.type_item, parent, false);
        return new TypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TypeViewHolder holder, int position) {
        if(position == messages.size()){
            holder.messageTextView.setText("类型管理");
            holder.messageTextView.setGravity(Gravity.CENTER);
            holder.messageTextView.setOnClickListener(v -> {
                Intent intent = new Intent(context, TypeManageActivity.class);
                context.startActivity(intent);
            });
        }else {
            Type type = messages.get(position);
            holder.messageTextView.setText(type.name);
            holder.messageTextView.setGravity(Gravity.LEFT);
            holder.messageTextView.setOnClickListener(v -> {
                TextView textView = (TextView) v;
                if(context instanceof InsertActivity){
                    TextView textViewType = ((InsertActivity) context).findViewById(R.id.textView_type_input);
                    textViewType.setText(textView.getText().toString());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return messages.size() + 1;
    }

    public static class TypeViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTextView;

        public TypeViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.textTypeItem);
        }
    }
}
