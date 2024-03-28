package com.example.hhpluscleanjava.lecture.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.example.hhpluscleanjava.lecture.domain.LectureStatus.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Applicant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "applicant_id")
    private Long id;

    // 유저 테이블을 만들지 말라고 해서 id 값만 받는형태로 구성했습니다.
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    @Enumerated(value = EnumType.STRING)
    private LectureStatus status;

    @Builder
    private Applicant(Long id, Long userId, Lecture lecture, LectureStatus status) {
        this.id = id;
        this.userId = userId;
        this.lecture = lecture;
        this.status = status;
    }

    public static Applicant create(Lecture lecture, Long userId) {
        return Applicant.builder()
                .lecture(lecture)
                .userId(userId)
                .status(APPLY)
                .build();
    }
}
