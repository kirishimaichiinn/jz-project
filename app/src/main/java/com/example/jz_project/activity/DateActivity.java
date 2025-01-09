package com.example.jz_project.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;

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
import com.example.jz_project.utils.SqlUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateActivity extends AppCompatActivity {
    private String selectDate = new SimpleDateFormat("yyyy-M-d", Locale.CHINA).format(new Date());

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_date);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActivityResultLauncher<Intent> activityResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    refresh();
                }
        );

        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(((view, year, month, dayOfMonth) -> {
            selectDate = year + "-" + (month + 1) + "-" + dayOfMonth;
            refresh();
        }));
        findViewById(R.id.date_back).setOnClickListener(v -> {
            finish();
        });
        findViewById(R.id.date_insert).setOnClickListener(v -> {
            Intent intent = new Intent(this, InsertActivity.class);
            intent.putExtra("insert_date",selectDate);
            activityResult.launch(intent);
        });



        RecyclerView recyclerView = findViewById(R.id.date_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        RecordAdapter recordAdapter = new RecordAdapter(this,activityResult, selectRecords(selectDate));
        recyclerView.setAdapter(recordAdapter);
        recyclerView.scrollToPosition(0);

    }

    public void refresh() {
        RecyclerView recyclerView = findViewById(R.id.date_recycler);
        RecordAdapter adapter = (RecordAdapter) recyclerView.getAdapter();
        adapter.setMessages(selectRecords(selectDate));
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(0);
    }

    public List<Record> selectRecords(String date){
        List<Record> messageList = DataUtil.messageList;
        List<Record> selectList = new ArrayList<>();
        for (Record record : messageList){
            if (record.time.equals(date)){
                selectList.add(record);
            }
        }
        return selectList;
    }
}