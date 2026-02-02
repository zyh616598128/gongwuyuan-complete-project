package com.gwy.controller;

import com.gwy.model.ExamPaper;
import com.gwy.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
@CrossOrigin(origins = "*")
public class ExamController {

    @Autowired
    private ExamService examService;

    @GetMapping
    public ResponseEntity<List<ExamPaper>> getAllExamPapers() {
        List<ExamPaper> examPapers = examService.getAllExamPapers();
        return ResponseEntity.ok(examPapers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamPaper> getExamPaperById(@PathVariable Long id) {
        return examService.getExamPaperById(id)
                .map(examPaper -> ResponseEntity.ok().body(examPaper))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ExamPaper> createExamPaper(@RequestBody ExamPaper examPaper) {
        ExamPaper createdExamPaper = examService.createExamPaper(examPaper);
        return ResponseEntity.ok(createdExamPaper);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExamPaper> updateExamPaper(@PathVariable Long id, @RequestBody ExamPaper examPaperDetails) {
        try {
            ExamPaper updatedExamPaper = examService.updateExamPaper(id, examPaperDetails);
            return ResponseEntity.ok(updatedExamPaper);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExamPaper(@PathVariable Long id) {
        try {
            examService.deleteExamPaper(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}