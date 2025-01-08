package com.example.jz_project.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jz_project.R;
import com.example.jz_project.activity.InsertActivity;
import com.example.jz_project.activity.TypeManageActivity;
import com.example.jz_project.entity.Type;
import com.example.jz_project.utils.DataUtil;
import com.example.jz_project.utils.SqlUtil;

import java.util.List;

public class TypeManageAdapter extends RecyclerView.Adapter<TypeManageAdapter.TypeViewHolder> {
    public List<Type> messages;
    private Context context;

    public TypeManageAdapter(Context context, List<Type> messages) {
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public TypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.type_manage_item, parent, false);
        return new TypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TypeViewHolder holder, int position) {
        Type type = messages.get(position);
        holder.nameTextView.setText(type.name);
        holder.changeTextView.setOnClickListener(v -> {
            EditText editText = new EditText(context);
            editText.setText(type.name);
            AlertDialog alertDialog = new AlertDialog.Builder(context)
                    .setView(editText)
                    .setPositiveButton("确定", ((dialog, which) -> {
                        if (!editText.getText().toString().equals(type.name)) {
                            updateType(type.id, editText.getText().toString());
                            DataUtil.loadType();
                            ((TypeManageActivity) context).refresh();
                        }
                    }))
                    .setNegativeButton("取消", ((dialog, which) -> {
                        dialog.dismiss();
                    }))
                    .show();
        });
        holder.delTextView.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("删除此条类型")
                    .setMessage("你确定要删除此条类型吗？")
                    .setPositiveButton("确定", (dialog, which) -> {
                        deleteType(type.id);
                        DataUtil.loadType();
                        ((TypeManageActivity) context).refresh();
                    })
                    .setNegativeButton("取消", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    private void deleteType(int id) {
        SqlUtil.getDb().execSQL("DELETE FROM type WHERE id = ?", new Object[]{id});
    }

    private void updateType(int id, String newType) {
        SqlUtil.getDb().execSQL("UPDATE type SET name = ? WHERE id = ?", new Object[]{newType, id});
    }

    public static class TypeViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView changeTextView;
        public TextView delTextView;

        public TypeViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.type_manage_name);
            changeTextView = itemView.findViewById(R.id.type_manage_change);
            delTextView = itemView.findViewById(R.id.type_manage_delete);
        }
    }
}
