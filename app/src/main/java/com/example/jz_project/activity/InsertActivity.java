package com.example.jz_project.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.TimeZone;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.jz_project.R;
import com.example.jz_project.entity.Record;
import com.example.jz_project.utils.SqlUtil;

public class InsertActivity extends AppCompatActivity {
    private String insertType = "添加";
    private Calendar calendar;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_insert);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));

        findViewById(R.id.main).setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                View currentFocus = getCurrentFocus();
                if (currentFocus != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);

                    if (!isViewInLayout(currentFocus,findViewById(R.id.layout_type_choose))){
                        findViewById(R.id.layout_type_choose).setVisibility(View.INVISIBLE);
                    }

                    currentFocus.clearFocus();
                }
            }
            return false;
        });
        findViewById(R.id.insert_back).setOnClickListener(v -> finish());
        findViewById(R.id.insert_enter).setOnClickListener(v -> {
            switch (insertType) {
                case "添加" -> insertData();
                case "详情" -> updateData();
            }
            finish();
        });
        findViewById(R.id.insert_delete).setOnClickListener(v -> {
            if (insertType.equals("详情")) {
                new AlertDialog.Builder(InsertActivity.this)
                        .setTitle("删除记录")
                        .setMessage("你确定要删除这条记录吗？")
                        .setPositiveButton("确定", (dialog, which) -> {
                            deleteData();
                            finish();
                        })
                        .setNegativeButton("取消", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();
            }
        });
        findViewById(R.id.textView_time_input).setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(InsertActivity.this, (view, selectedYear, selectedMonth, selectedDay) -> {
                String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                TextView timeView = findViewById(R.id.textView_time_input);
                timeView.setText(selectedDate);
            }, year, month, day);
            datePickerDialog.show();
        });
        findViewById(R.id.textView_type_input).setOnClickListener(v -> {
            findViewById(R.id.layout_type_choose).setVisibility(View.VISIBLE);
        });


        setTitle(getIntent());
    }

    private void setTitle(Intent intent) {
        TextView textView = findViewById(R.id.insert_title);
        String type = intent.getStringExtra("insert_type");
        if (type != null && !type.isEmpty()) {
            insertType = type;
            textView.setText(type);

            if (type.equals("详情")) {
                fillPage();
                findViewById(R.id.insert_delete).setVisibility(View.VISIBLE);
            }
        } else {
            TextView timeView = findViewById(R.id.textView_time_input);
            String date = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
            timeView.setText(date);
        }
    }

    private Record getRecord() {
        double money = 0.0;
        String type = "";
        String time = "";
        String note = "";

        EditText moneyView = findViewById(R.id.textView_money_input);
        TextView typeView = findViewById(R.id.textView_type_input);
        TextView timeView = findViewById(R.id.textView_time_input);
        EditText noteView = findViewById(R.id.textView_note_input);
        try {
            money = Double.parseDouble(moneyView.getText().toString());
            type = typeView.getText().toString();
            time = timeView.getText().toString();
            note = noteView.getText().toString();
        } catch (Exception e) {
            Toast.makeText(this, "数据有误", Toast.LENGTH_LONG).show();
        }

        return new Record(null, money, type, time, note);
    }

    private void insertData() {
        Record record = getRecord();

        SqlUtil.getDb().execSQL("INSERT INTO record values (null,?,?,?,?)", new Object[]{record.money, record.type, record.time, record.note});
    }

    private void updateData() {
        Record record = getRecord();
        record.id = Integer.valueOf(getIntent().getStringExtra("record_id"));
        SqlUtil.getDb().execSQL("UPDATE record SET money = ?, type = ?, time = ?, note = ? WHERE id = ?", new Object[]{record.money, record.type, record.time, record.note, record.id});
    }

    private void deleteData() {
        SqlUtil.getDb().execSQL("DELETE FROM record WHERE id = ?", new Object[]{Integer.valueOf(getIntent().getStringExtra("record_id"))});
    }

    private void fillPage() {
        EditText moneyView = findViewById(R.id.textView_money_input);
        TextView typeView = findViewById(R.id.textView_type_input);
        TextView timeView = findViewById(R.id.textView_time_input);
        EditText noteView = findViewById(R.id.textView_note_input);

        moneyView.setText(getIntent().getStringExtra("record_money"));
        typeView.setText(getIntent().getStringExtra("record_type"));
        timeView.setText(getIntent().getStringExtra("record_time"));
        noteView.setText(getIntent().getStringExtra("record_note"));
    }

    private boolean isViewInLayout(View view, ViewGroup layout) {
        if (view == null || layout == null) {
            return false;
        }
        if (view == layout) {
            return true;
        }
        ViewParent parent = view.getParent();
        while (parent != null && parent instanceof ViewGroup) {
            if (parent == layout) {
                return true;
            }
            parent = parent.getParent();
        }
        return false;
    }
}