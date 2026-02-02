package com.gwy.service;

import com.gwy.model.ExamPaper;
import com.gwy.model.ExamRecord;
import com.gwy.model.Question;
import com.gwy.model.Subject;
import com.gwy.repository.ExamPaperRepository;
import com.gwy.repository.ExamRecordRepository;
import com.gwy.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ExamService {

    @Autowired
    private ExamPaperRepository examPaperRepository;

    @Autowired
    private ExamRecordRepository examRecordRepository;

    @Autowired
    private QuestionRepository questionRepository;

    public List<ExamPaper> getExamPapersBySubject(Subject subject) {
        return examPaperRepository.findBySubjectAndIsActiveTrue(subject);
    }

    public List<ExamPaper> getExamPapersBySubjectId(Long subjectId) {
        return examPaperRepository.findBySubject_IdAndIsActiveTrue(subjectId);
    }

    public Optional<ExamPaper> findExamPaperById(Long id) {
        return examPaperRepository.findById(id);
    }

    public ExamPaper saveExamPaper(ExamPaper examPaper) {
        if (examPaper.getId() == null) {
            examPaper.setCreateTime(LocalDateTime.now());
        }
        examPaper.setUpdateTime(LocalDateTime.now());
        return examPaperRepository.save(examPaper);
    }

    public List<Question> getQuestionsForExamPaper(ExamPaper examPaper) {
        return questionRepository.findAllById(
            examPaper.getQuestions().stream().map(Question::getId).toList()
        );
    }

    public ExamRecord saveExamRecord(ExamRecord examRecord) {
        if (examRecord.getId() == null) {
            examRecord.setCreateTime(LocalDateTime.now());
        }
        examRecord.setUpdateTime(LocalDateTime.now());
        return examRecordRepository.save(examRecord);
    }

    public List<ExamRecord> getExamRecordsByUserId(Long userId) {
        return examRecordRepository.findByUserIdOrderByCreateTimeDesc(userId);
    }
}