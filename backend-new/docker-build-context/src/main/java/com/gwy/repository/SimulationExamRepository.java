package com.gwy.repository;

import com.gwy.model.ExamPaper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimulationExamRepository extends JpaRepository<ExamPaper, Long> {
    // 自定义查询方法可以在这里添加
}