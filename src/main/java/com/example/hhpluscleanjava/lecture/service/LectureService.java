package com.example.hhpluscleanjava.lecture.service;

import com.example.hhpluscleanjava.lecture.controller.dto.request.LectureRequest;
import com.example.hhpluscleanjava.lecture.domain.Applicant;
import com.example.hhpluscleanjava.lecture.domain.Lecture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class LectureService {

    private final LectureRepository lectureRepository;
    private final ApplicantRepository applicantRepository;

    @Transactional
    public Applicant applyLecture(LectureRequest lectureRequest, ZonedDateTime applyDate) {
        Lecture lecture = lectureRepository.findById(lectureRequest.getLectureId())
                .orElseThrow(() -> new IllegalArgumentException("해당 특강을 찾을 수 없습니다."));

        if (lecture.isLectureRegistrationOpen(applyDate)) {
            throw new IllegalArgumentException("특강신청 기간이 아닙니다.");
        }

        if (lecture.isCapacityExceeded(30)) {
            throw new IllegalArgumentException("인원이 초과되었습니다.");
        }

        if (applicantRepository.existsByLectureAndUserId(lecture, lectureRequest.getUserId())) {
            throw new IllegalArgumentException("이미 신청한 내역이 존재합니다.");
        }

        lecture.addCapacityCount();

        Applicant savedApplicant = applicantRepository.save(Applicant.create(lecture, lectureRequest.getUserId()));

        return savedApplicant;
    }

    @Transactional(readOnly = true)
    public Applicant getApplicant(long lectureId, Long userId) {
        return applicantRepository.findByLectureIdAndUserId(lectureId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 강의의 신청 정보를 찾을 수 없습니다."));
    }
}
