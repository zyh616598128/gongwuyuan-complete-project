package com.gwy.exam;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.gwy.exam.api.ApiClient;
import com.gwy.exam.api.ApiService;
import com.gwy.exam.api.QuestionListResponse;
import com.gwy.exam.model.Question;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;

public class PracticeActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TextView tvQuestionProgress;
    private TextView tvTimer;
    private TextView tvQuestionContent;
    private Button btnPrevious;
    private Button btnSubmit;
    private Button btnNext;

    private List<Question> questionList;
    private int currentQuestionIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

        initViews();
        setupToolbar();
        setupClickListeners();
        loadQuestionsFromApi();
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
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--;
                loadCurrentQuestion();
            }
        });

        btnSubmit.setOnClickListener(v -> {
            // 实现提交答案逻辑
            Toast.makeText(this, "提交答案", Toast.LENGTH_SHORT).show();
        });

        btnNext.setOnClickListener(v -> {
            if (currentQuestionIndex < questionList.size() - 1) {
                currentQuestionIndex++;
                loadCurrentQuestion();
            } else {
                Toast.makeText(this, "已经是最后一题", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadQuestionsFromApi() {
        ApiService apiService = ApiClient.getInstance().getApiService();
        Call<QuestionListResponse> call = apiService.getQuestions(0, 30); // Get first 30 questions
        
        call.enqueue(new Callback<QuestionListResponse>() {
            @Override
            public void onResponse(Call<QuestionListResponse> call, Response<QuestionListResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    QuestionListResponse questionListResponse = response.body();
                    questionList = questionListResponse.getData();
                    
                    if (questionList != null && !questionList.isEmpty()) {
                        loadCurrentQuestion();
                    } else {
                        Toast.makeText(PracticeActivity.this, "暂无题目数据", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PracticeActivity.this, "获取题目失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<QuestionListResponse> call, Throwable t) {
                Toast.makeText(PracticeActivity.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadCurrentQuestion() {
        if (questionList != null && currentQuestionIndex < questionList.size()) {
            Question currentQuestion = questionList.get(currentQuestionIndex);
            
            // Update progress text
            tvQuestionProgress.setText("第 " + (currentQuestionIndex + 1) + " 题，共 " + questionList.size() + " 题");
            
            // Update question content
            tvQuestionContent.setText(currentQuestion.getContent());
        }
    }
}