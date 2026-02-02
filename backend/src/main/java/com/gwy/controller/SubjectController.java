package com.gwy.controller;

import com.gwy.dto.ApiResponse;
import com.gwy.model.Subject;
import com.gwy.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@CrossOrigin(origins = "*")
public class SubjectController {

    @Autowired
    private SubjectRepository subjectRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Subject>>> getAllSubjects() {
        List<Subject> subjects = subjectRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success("获取科目列表成功", subjects));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Subject>> getSubject(@PathVariable Long id) {
        return subjectRepository.findById(id)
                .map(subject -> ResponseEntity.ok(ApiResponse.success("获取科目成功", subject)))
                .orElse(ResponseEntity.ok(ApiResponse.error("科目不存在")));
    }
}