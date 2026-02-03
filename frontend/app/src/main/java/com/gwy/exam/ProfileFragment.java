package com.gwy.exam;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.gwy.exam.api.ApiClient;
import com.gwy.exam.api.ApiService;
import com.gwy.exam.api.UserProfileResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private TextView tvUsername;
    private TextView tvMemberLevel;
    private TextView tvStudyDays;
    private TextView tvTotalQuestions;
    private TextView tvAnsweredQuestions;
    private TextView tvAccuracyRate;
    
    private LinearLayout layoutFavorites;
    private LinearLayout layoutStudyPlan;
    private LinearLayout layoutSettings;
    private LinearLayout layoutHelpFeedback;
    private LinearLayout layoutAbout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        
        initViews(view);
        setupClickListeners();
        loadUserDataFromApi();
        
        return view;
    }

    private void initViews(View view) {
        tvUsername = view.findViewById(R.id.tv_username);
        tvMemberLevel = view.findViewById(R.id.tv_member_level);
        tvStudyDays = view.findViewById(R.id.tv_study_days);
        tvTotalQuestions = view.findViewById(R.id.tv_total_questions);
        tvAnsweredQuestions = view.findViewById(R.id.tv_answered_questions);
        tvAccuracyRate = view.findViewById(R.id.tv_accuracy_rate);
        
        // 功能列表
        layoutFavorites = view.findViewById(R.id.layout_favorites);
        layoutStudyPlan = view.findViewById(R.id.layout_study_plan);
        layoutSettings = view.findViewById(R.id.layout_settings);
        layoutHelpFeedback = view.findViewById(R.id.layout_help_feedback);
        layoutAbout = view.findViewById(R.id.layout_about);
    }

    private void setupClickListeners() {
        // 我的收藏
        layoutFavorites.setOnClickListener(v -> {
            // 跳转到收藏页面
            Intent intent = new Intent(getActivity(), FavoritesActivity.class);
            startActivity(intent);
        });

        // 学习计划
        layoutStudyPlan.setOnClickListener(v -> {
            // 跳转到学习计划页面
            Intent intent = new Intent(getActivity(), StudyPlanActivity.class);
            startActivity(intent);
        });

        // 设置
        layoutSettings.setOnClickListener(v -> {
            // 跳转到设置页面
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
        });

        // 帮助与反馈
        layoutHelpFeedback.setOnClickListener(v -> {
            // 跳转到帮助反馈页面
            Intent intent = new Intent(getActivity(), HelpFeedbackActivity.class);
            startActivity(intent);
        });

        // 关于我们
        layoutAbout.setOnClickListener(v -> {
            // 跳转到关于页面
            Intent intent = new Intent(getActivity(), AboutActivity.class);
            startActivity(intent);
        });
    }

    private void loadUserDataFromApi() {
        ApiService apiService = ApiClient.getInstance().getApiService();
        Call<UserProfileResponse> call = apiService.getUserProfile();
        
        call.enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    UserProfileResponse userProfileResponse = response.body();
                    updateUIWithUserData(userProfileResponse.getData());
                } else {
                    Toast.makeText(getContext(), "获取用户资料失败", Toast.LENGTH_SHORT).show();
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
            
            // For member level, we'll use a default value or fetch from server if available
            tvMemberLevel.setText("普通会员"); // Default, can be updated based on server response if available
            
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