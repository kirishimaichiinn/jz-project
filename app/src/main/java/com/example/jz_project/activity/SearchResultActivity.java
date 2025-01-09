package com.example.jz_project.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jz_project.R;
import com.example.jz_project.adapter.RecordAdapter;
import com.example.jz_project.entity.Record;
import com.example.jz_project.utils.DataUtil;

import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {
    private EditText editText;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_result);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editText = findViewById(R.id.editText_search);
        editText.requestFocus();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                refresh();
            }
        });

        ActivityResultLauncher<Intent> activityResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    refresh();
                }
        );

        findViewById(R.id.search_back).setOnClickListener(v -> {
            finish();
        });
        findViewById(R.id.view_backgroud).setOnClickListener(v -> {
            hideInputMethod(v);
            findViewById(R.id.view_backgroud).setVisibility(View.GONE);
        });
        editText.setOnClickListener(v -> {
            findViewById(R.id.view_backgroud).setVisibility(View.VISIBLE);
        });


        RecyclerView recyclerView = findViewById(R.id.search_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        RecordAdapter recordAdapter = new RecordAdapter(this,activityResult, new ArrayList<>());
        recyclerView.setAdapter(recordAdapter);
    }

    public void refresh() {
        RecyclerView recyclerView = findViewById(R.id.search_recycler);
        RecordAdapter adapter = (RecordAdapter) recyclerView.getAdapter();
        adapter.setMessages(selectRecords(editText.getText().toString()));
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(0);
    }

    public List<Record> selectRecords(String key){
        List<Record> messageList = DataUtil.messageList;
        List<Record> selectList = new ArrayList<>();
        if(key.isEmpty()) return selectList;
        for (Record record : messageList){
            if (record.type.contains(key) || record.note.contains(key)){
                selectList.add(record);
            }
        }
        return selectList;
    }


    private void hideInputMethod(View view){
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}