package com.example.hhpluscleanjava.lecture.infrastructure;

import com.example.hhpluscleanjava.lecture.domain.Applicant;
import com.example.hhpluscleanjava.lecture.domain.Lecture;
import com.example.hhpluscleanjava.lecture.service.ApplicantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ApplicantRepositoryImpl implements ApplicantRepository {

    private final ApplicantJpaRepository applicantJpaRepository;

    @Override
    public boolean existsByLectureAndUserId(Lecture lecture, Long userId) {
        return applicantJpaRepository.existsByLectureAndUserId(lecture, userId);
    }

    @Override
    public Applicant save(Applicant applicant) {
        return applicantJpaRepository.save(applicant);
    }

    @Override
    public Optional<Applicant> findByLectureIdAndUserId(Long lectureId, Long userId) {
        return applicantJpaRepository.findByLectureIdAndUserId(lectureId, userId);
    }
}
