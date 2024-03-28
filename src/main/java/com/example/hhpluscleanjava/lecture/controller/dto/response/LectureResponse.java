package com.example.hhpluscleanjava.lecture.controller.dto.response;

import com.example.hhpluscleanjava.lecture.domain.Applicant;
import com.example.hhpluscleanjava.lecture.domain.Lecture;
import com.example.hhpluscleanjava.lecture.domain.LectureStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LectureResponse {

    private Long id;
    private String title;
    private Long lectureId;
    private LectureStatus lectureStatus;

    @Builder
    private LectureResponse(Long id, String title, Long lectureId, LectureStatus lectureStatus) {
        this.id = id;
        this.title = title;
        this.lectureId = lectureId;
        this.lectureStatus = lectureStatus;
    }

    public static LectureResponse from(Applicant applicant) {
        return LectureResponse.builder()
                .id(applicant.getId())
                .title(applicant.getLecture().getTitle())
                .lectureId(applicant.getLecture().getId())
                .lectureStatus(applicant.getStatus())
                .build();
    }
}
