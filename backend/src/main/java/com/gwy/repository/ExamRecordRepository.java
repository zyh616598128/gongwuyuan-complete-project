package com.gwy.repository;

import com.gwy.model.ExamRecord;
import com.gwy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRecordRepository extends JpaRepository<ExamRecord, Long> {
    List<ExamRecord> findByUserIdOrderByCreateTimeDesc(Long userId);
}