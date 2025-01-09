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
import com.example.jz_project.utils.DataUtil;
import com.example.jz_project.utils.SqlUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

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

        DataUtil.init(this);

        ActivityResultLauncher<Intent> activityResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    refresh();
                }
        );
        findViewById(R.id.buttonInsert).setOnClickListener(v -> {
            Intent intent = new Intent(this, InsertActivity.class);
            activityResult.launch(intent);
        });
        findViewById(R.id.imageButton).setOnClickListener(v -> {
            Intent intent = new Intent(this, DateActivity.class);
            activityResult.launch(intent);
        });
        findViewById(R.id.viewToSearch).setOnClickListener(v -> {
            Intent intent = new Intent(this, SearchResultActivity.class);
            activityResult.launch(intent);
        });
        findViewById(R.id.imageButton2).setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingActivity.class);
            activityResult.launch(intent);
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        RecordAdapter recordAdapter = new RecordAdapter(this,activityResult,DataUtil.messageList);
        recyclerView.setAdapter(recordAdapter);
        recyclerView.scrollToPosition(0);

    }


    public void refresh() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scrollToPosition(0);
    }
}