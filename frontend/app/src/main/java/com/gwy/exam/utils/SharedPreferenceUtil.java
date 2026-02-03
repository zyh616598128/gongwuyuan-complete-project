package com.gwy.exam.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtil {
    private static final String PREF_NAME = "gwy_exam_prefs";
    private static final String KEY_JWT_TOKEN = "jwt_token";
    
    private static SharedPreferenceUtil instance;
    private SharedPreferences sharedPreferences;
    
    private SharedPreferenceUtil(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
    
    public static synchronized void initializeInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferenceUtil(context.getApplicationContext());
        }
    }
    
    public static SharedPreferenceUtil getInstance() {
        if (instance == null) {
            throw new IllegalStateException("SharedPreferenceUtil not initialized. Call initializeInstance() first.");
        }
        return instance;
    }
    
    public void saveToken(String token) {
        sharedPreferences.edit().putString(KEY_JWT_TOKEN, token).apply();
    }
    
    public String getToken() {
        return sharedPreferences.getString(KEY_JWT_TOKEN, null);
    }
    
    public void clearToken() {
        sharedPreferences.edit().remove(KEY_JWT_TOKEN).apply();
    }
}