package com.example.jz_project.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jz_project.R;
import com.example.jz_project.adapter.RecordAdapter;
import com.example.jz_project.entity.Record;
import com.example.jz_project.utils.SqlUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private boolean menuOpen = false;
    private List<Record> messageList = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
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

        SqlUtil sqlUtil = new SqlUtil(this);

        ActivityResultLauncher<Intent> jumpToInsert = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    refresh();
                }
        );
        findViewById(R.id.buttonInsert).setOnClickListener(v -> {
            Intent intent = new Intent(this, InsertActivity.class);
            jumpToInsert.launch(intent);
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        RecordAdapter recordAdapter = new RecordAdapter(this,jumpToInsert,loadMessages());
        recyclerView.setAdapter(recordAdapter);
        recyclerView.scrollToPosition(0);


    }

    private List<Record> loadMessages() {
        messageList.clear();
        Cursor cursor = SqlUtil.getDb().rawQuery("select * from record order by time DESC, id DESC LIMIT 20", null);
        if (cursor.moveToFirst()) {
            do {
                Record record = new Record();
                record.setId(cursor.getInt(0));
                record.setMoney(cursor.getDouble(1));
                record.setType(cursor.getString(2));
                record.setTime(cursor.getString(3));
                record.setNote(cursor.getString(4));
                messageList.add(record);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return messageList;
    }

    private void refresh() {
        loadMessages();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scrollToPosition(0);
    }
}