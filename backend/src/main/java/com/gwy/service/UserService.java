package com.gwy.service;

import com.gwy.dto.RegisterRequest;
import com.gwy.model.ExamRecord;
import com.gwy.model.User;
import com.gwy.model.WrongQuestion;
import com.gwy.repository.ExamRecordRepository;
import com.gwy.repository.UserRepository;
import com.gwy.repository.WrongQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExamRecordRepository examRecordRepository;

    @Autowired
    private WrongQuestionRepository wrongQuestionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User register(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setNickname(request.getNickname());
        user.setPhone(request.getPhone());
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        return userRepository.save(user);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // Calculate user's study days based on their activity
    public int calculateStudyDays(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return 0;
        }

        // Count distinct days with exam records or wrong questions
        LocalDate startDate = user.getCreateTime().toLocalDate();
        LocalDate today = LocalDate.now();
        long totalDays = ChronoUnit.DAYS.between(startDate, today) + 1;

        // For simplicity, we'll return a basic calculation
        // In a real app, you'd want to count actual study days
        return Math.max(1, (int) totalDays);
    }

    // Get total questions answered by user (from exam records and wrong questions)
    public int getTotalQuestionsAnswered(Long userId) {
        // Count questions from exam records
        List<ExamRecord> examRecords = examRecordRepository.findByUserIdOrderByCreateTimeDesc(userId);
        int totalFromExams = 0;
        for (ExamRecord record : examRecords) {
            if (record.getAnswers() != null) {
                totalFromExams += record.getAnswers().size();
            }
        }

        // Count wrong questions
        int totalWrongQuestions = wrongQuestionRepository.countByUserId(userId);

        return totalFromExams + totalWrongQuestions;
    }

    // Get answered questions count
    public int getAnsweredQuestionsCount(Long userId) {
        return getTotalQuestionsAnswered(userId);
    }

    // Calculate accuracy rate
    public double getAccuracyRate(Long userId) {
        List<ExamRecord> examRecords = examRecordRepository.findByUserIdOrderByCreateTimeDesc(userId);
        
        if (examRecords.isEmpty()) {
            return 0.0;
        }

        int totalQuestions = 0;
        int correctAnswers = 0;

        for (ExamRecord record : examRecords) {
            if (record.getCorrectness() != null) {
                totalQuestions += record.getCorrectness().size();
                for (Boolean isCorrect : record.getCorrectness()) {
                    if (isCorrect != null && isCorrect) {
                        correctAnswers++;
                    }
                }
            }
        }

        if (totalQuestions == 0) {
            return 0.0;
        }

        return ((double) correctAnswers / totalQuestions) * 100;
    }
    
    public User update(User user) {
        user.setUpdateTime(java.time.LocalDateTime.now());
        return userRepository.save(user);
    }
}