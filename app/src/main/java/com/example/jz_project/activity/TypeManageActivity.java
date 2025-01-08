package com.example.jz_project.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jz_project.R;
import com.example.jz_project.adapter.RecordAdapter;
import com.example.jz_project.adapter.TypeManageAdapter;
import com.example.jz_project.utils.DataUtil;
import com.example.jz_project.utils.SqlUtil;

public class TypeManageActivity extends AppCompatActivity {

    @SuppressLint({"ClickableViewAccessibility", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_type_manage);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.type_manage_back).setOnClickListener(v -> {

            finish();
        });
        findViewById(R.id.text_addType2).setOnFocusChangeListener((v, focus) -> {
            if (focus) {
                findViewById(R.id.view_greyBackground).setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.view_greyBackground).setVisibility(View.GONE);
            }
        });
        findViewById(R.id.view_greyBackground).setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                View currentFocus = getCurrentFocus();
                if (currentFocus != null) {
                    hideInputMethod(v);
                    currentFocus.clearFocus();
                }
                findViewById(R.id.view_greyBackground).setVisibility(View.GONE);
            }
            return true;
        });
        findViewById(R.id.button_addType2).setOnClickListener(v -> {
            TextView textView = findViewById(R.id.text_addType2);
            if (!textView.getText().toString().isEmpty()){
                SqlUtil.getDb().execSQL("INSERT INTO type VALUES (null,?)",new Object[]{textView.getText().toString()});
                hideInputMethod(v);
                findViewById(R.id.view_greyBackground).setVisibility(View.GONE);
                DataUtil.loadType();
                RecyclerView typeRecycler = findViewById(R.id.typeManage_recycler);
                typeRecycler.getAdapter().notifyDataSetChanged();
                typeRecycler.scrollToPosition(DataUtil.typeList.size());
                textView.setText("");
            }
        });


        RecyclerView typeManageRecycler = findViewById(R.id.typeManage_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        typeManageRecycler.setLayoutManager(layoutManager);
        TypeManageAdapter typeManageAdapter = new TypeManageAdapter(this, DataUtil.typeList);
        typeManageRecycler.setAdapter(typeManageAdapter);
    }

    public void refresh() {
        RecyclerView recyclerView = findViewById(R.id.typeManage_recycler);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    private void hideInputMethod(View view){
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}