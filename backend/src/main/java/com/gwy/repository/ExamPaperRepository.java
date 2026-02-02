package com.gwy.repository;

import com.gwy.model.ExamPaper;
import com.gwy.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamPaperRepository extends JpaRepository<ExamPaper, Long> {
    List<ExamPaper> findBySubjectAndIsActiveTrue(Subject subject);
    List<ExamPaper> findBySubject_IdAndIsActiveTrue(Long subjectId);
}