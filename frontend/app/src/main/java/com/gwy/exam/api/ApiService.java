package com.gwy.exam.api;

import com.gwy.exam.model.Question;
import com.gwy.exam.model.Subject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    // 获取题目列表
    @GET("api/questions")
    Call<QuestionListResponse> getQuestions(
        @Query("page") int page,
        @Query("size") int size
    );

    // 根据科目获取题目
    @GET("api/questions/subject/{subjectId}")
    Call<QuestionListResponse> getQuestionsBySubject(
        @Path("subjectId") Long subjectId,
        @Query("page") int page,
        @Query("size") int size
    );

    // 根据分类获取题目
    @GET("api/questions/category/{categoryId}")
    Call<QuestionListResponse> getQuestionsByCategory(
        @Path("categoryId") Long categoryId,
        @Query("page") int page,
        @Query("size") int size
    );

    // 获取单个题目
    @GET("api/questions/{id}")
    Call<Question> getQuestion(@Path("id") Long id);

    // 获取科目列表
    @GET("api/subjects")
    Call<SubjectListResponse> getSubjects();

    // 登录 - 使用POST方法
    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    // 注册 - 使用POST方法
    @POST("api/auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest registerRequest);
}