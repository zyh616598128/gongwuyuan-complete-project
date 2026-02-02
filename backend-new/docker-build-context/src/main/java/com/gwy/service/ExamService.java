package com.gwy.service;

import com.gwy.model.ExamPaper;
import com.gwy.model.ExamRecord;
import com.gwy.repository.SimulationExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExamService {

    @Autowired
    private SimulationExamRepository simulationExamRepository;

    public List<ExamPaper> getAllExamPapers() {
        return simulationExamRepository.findAll();
    }

    public Optional<ExamPaper> getExamPaperById(Long id) {
        return simulationExamRepository.findById(id);
    }

    public ExamPaper createExamPaper(ExamPaper examPaper) {
        return simulationExamRepository.save(examPaper);
    }

    public ExamPaper updateExamPaper(Long id, ExamPaper examPaperDetails) {
        ExamPaper examPaper = simulationExamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ExamPaper not found with id: " + id));

        examPaper.setTitle(examPaperDetails.getTitle());
        examPaper.setDescription(examPaperDetails.getDescription());
        examPaper.setExamType(examPaperDetails.getExamType());
        examPaper.setSubject(examPaperDetails.getSubject());
        examPaper.setDurationMinutes(examPaperDetails.getDurationMinutes());
        examPaper.setUpdatedAt(java.time.LocalDateTime.now());

        return simulationExamRepository.save(examPaper);
    }

    public void deleteExamPaper(Long id) {
        ExamPaper examPaper = simulationExamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ExamPaper not found with id: " + id));
        examPaper.setIsActive(false); // 软删除
        simulationExamRepository.save(examPaper);
    }

    // 其他考试相关的业务逻辑可以在这里添加
}