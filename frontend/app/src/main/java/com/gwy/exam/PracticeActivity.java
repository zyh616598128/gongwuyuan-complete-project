package com.gwy.exam;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;

public class PracticeActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TextView tvQuestionProgress;
    private TextView tvTimer;
    private TextView tvQuestionContent;
    private Button btnPrevious;
    private Button btnSubmit;
    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

        initViews();
        setupToolbar();
        setupClickListeners();
        loadQuestionData();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tvQuestionProgress = findViewById(R.id.tv_question_progress);
        tvTimer = findViewById(R.id.tv_timer);
        tvQuestionContent = findViewById(R.id.tv_question_content);
        btnPrevious = findViewById(R.id.btn_previous);
        btnSubmit = findViewById(R.id.btn_submit);
        btnNext = findViewById(R.id.btn_next);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("题目练习");
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setupClickListeners() {
        btnPrevious.setOnClickListener(v -> {
            // 实现上一题逻辑
        });

        btnSubmit.setOnClickListener(v -> {
            // 实现提交答案逻辑
        });

        btnNext.setOnClickListener(v -> {
            // 实现下一题逻辑
        });
    }

    private void loadQuestionData() {
        // 模拟加载题目数据
        tvQuestionProgress.setText("第 1 题，共 30 题");
        tvQuestionContent.setText("下列选项中，哪一项不属于我国《宪法》规定的基本权利？");
    }
}