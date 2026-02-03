package com.gwy.exam;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.card.MaterialCardView;
import com.gwy.exam.api.ApiClient;
import com.gwy.exam.api.ApiService;
import com.gwy.exam.api.UserProfileResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private TextView tvUsername;
    private TextView tvStudyDays;
    private TextView tvTotalQuestions;
    private TextView tvAnsweredQuestions;
    private TextView tvAccuracyRate;
    private MaterialCardView cardPractice;
    private MaterialCardView cardRandom;
    private MaterialCardView cardExam;
    private MaterialCardView cardWrongBook;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        initViews(view);
        setupClickListeners();
        loadUserDataFromApi();
        
        return view;
    }

    private void initViews(View view) {
        tvUsername = view.findViewById(R.id.tv_username);
        tvStudyDays = view.findViewById(R.id.tv_study_days);
        tvTotalQuestions = view.findViewById(R.id.tv_total_questions);
        tvAnsweredQuestions = view.findViewById(R.id.tv_answered_questions);
        tvAccuracyRate = view.findViewById(R.id.tv_accuracy_rate);
        
        // 快捷入口卡片
        cardPractice = view.findViewById(R.id.card_practice);
        cardRandom = view.findViewById(R.id.card_random);
        cardExam = view.findViewById(R.id.card_exam);
        cardWrongBook = view.findViewById(R.id.card_wrong_book);
    }

    private void setupClickListeners() {
        // 设置快捷入口点击事件
        cardPractice.setOnClickListener(v -> {
            // 跳转到顺序练习页面
            Intent intent = new Intent(getActivity(), PracticeActivity.class);
            startActivity(intent);
        });

        cardRandom.setOnClickListener(v -> {
            // 跳转到随机练习页面
            Intent intent = new Intent(getActivity(), PracticeActivity.class);
            startActivity(intent);
        });

        cardExam.setOnClickListener(v -> {
            // 跳转到模拟考试页面
            Intent intent = new Intent(getActivity(), MockExamActivity.class);
            startActivity(intent);
        });

        cardWrongBook.setOnClickListener(v -> {
            // 跳转到错题本页面
            Intent intent = new Intent(getActivity(), WrongBookActivity.class);
            startActivity(intent);
        });
    }

    private void loadUserDataFromApi() {
        ApiService apiService = ApiClient.getApiService();
        Call<UserProfileResponse> call = apiService.getUserProfile();
        
        call.enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    UserProfileResponse userProfileResponse = response.body();
                    updateUIWithUserData(userProfileResponse.getData());
                } else {
                    Toast.makeText(getContext(), "获取用户数据失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                Toast.makeText(getContext(), "网络请求失败: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateUIWithUserData(java.util.Map<String, Object> userData) {
        if (userData != null) {
            String username = (String) userData.get("username");
            if (username != null) {
                tvUsername.setText(username);
            }
            
            Number studyDays = (Number) userData.get("studyDays");
            if (studyDays != null) {
                tvStudyDays.setText("已学习 " + studyDays.intValue() + " 天");
            }
            
            Number totalQuestions = (Number) userData.get("totalQuestions");
            if (totalQuestions != null) {
                tvTotalQuestions.setText(String.format("%,d", totalQuestions.intValue()));
            }
            
            Number answeredQuestions = (Number) userData.get("answeredQuestions");
            if (answeredQuestions != null) {
                tvAnsweredQuestions.setText(String.format("%,d", answeredQuestions.intValue()));
            }
            
            Double accuracyRate = (Double) userData.get("accuracyRate");
            if (accuracyRate != null) {
                tvAccuracyRate.setText(String.format("%.0f%%", accuracyRate));
            }
        }
    }
}