package com.gwy.exam;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.gwy.exam.adapter.QuestionAdapter;
import com.gwy.exam.model.Question;
import com.gwy.exam.api.ApiClient;
import com.gwy.exam.api.ApiService;
import com.gwy.exam.api.QuestionListResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;

public class QuestionBankFragment extends Fragment implements QuestionAdapter.OnQuestionClickListener {

    private RecyclerView recyclerViewQuestions;
    private QuestionAdapter questionAdapter;
    private List<Question> questionList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_bank, container, false);
        
        initViews(view);
        setupRecyclerView();
        loadQuestionDataFromApi();
        
        return view;
    }

    private void initViews(View view) {
        recyclerViewQuestions = view.findViewById(R.id.recycler_view_questions);
    }

    private void setupRecyclerView() {
        questionList = new ArrayList<>();
        questionAdapter = new QuestionAdapter();
        questionAdapter.setOnQuestionClickListener(this);
        
        recyclerViewQuestions.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewQuestions.setAdapter(questionAdapter);
    }

    private void loadQuestionDataFromApi() {
        ApiService apiService = ApiClient.getApiService();
        Call<QuestionListResponse> call = apiService.getQuestions(0, 50); // Get first 50 questions
        
        call.enqueue(new Callback<QuestionListResponse>() {
            @Override
            public void onResponse(Call<QuestionListResponse> call, Response<QuestionListResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    QuestionListResponse questionListResponse = response.body();
                    questionList = questionListResponse.getData();
                    
                    if (questionList != null) {
                        questionAdapter.setQuestions(questionList);
                    } else {
                        Toast.makeText(getContext(), "暂无题目数据", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "获取题目失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<QuestionListResponse> call, Throwable t) {
                Toast.makeText(getContext(), "网络请求失败: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onQuestionClick(Question question) {
        // 处理题目点击事件
        // 可以跳转到题目详情页面
    }

    @Override
    public void onDoNowClick(Question question) {
        // 处理“立即练习”点击事件
        // 可以跳转到练习页面
    }
}