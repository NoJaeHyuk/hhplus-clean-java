package com.example.hhpluscleanjava.lecture.controller.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LectureRequest {

    private Long userId;
    private Long lectureId;

    @Builder
    private LectureRequest(Long userId, Long lectureId) {
        this.userId = userId;
        this.lectureId = lectureId;
    }
}
