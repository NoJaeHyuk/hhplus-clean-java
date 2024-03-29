package com.example.hhpluscleanjava.lecture.controller;

import com.example.hhpluscleanjava.lecture.controller.dto.request.LectureRequest;
import com.example.hhpluscleanjava.lecture.controller.dto.response.LectureResponse;
import com.example.hhpluscleanjava.lecture.domain.Applicant;
import com.example.hhpluscleanjava.lecture.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;

@RestController
@RequiredArgsConstructor
public class LectureController {

    private final LectureService lectureService;

    /**
     * 특강신청 API
     * @return
     */
    @PostMapping("/lectures")
    public ResponseEntity<LectureResponse> applyLecture(
            @RequestBody LectureRequest lectureRequest
    ) {
        Applicant applicant = lectureService.applyLecture(lectureRequest, ZonedDateTime.now());
        return ResponseEntity.ok(LectureResponse.from(applicant));
    }

    /**
     * 특정 특강 신청 조회 API
     * @param userId
     * @return
     */
    @GetMapping("/lectures/{id}")
    public ResponseEntity<LectureResponse> getApplicant(
            @PathVariable long id,
            @RequestParam Long userId
    ) {
        Applicant applicant = lectureService.getApplicant(id, userId);
        return ResponseEntity.ok(LectureResponse.from(applicant));
    }
}
