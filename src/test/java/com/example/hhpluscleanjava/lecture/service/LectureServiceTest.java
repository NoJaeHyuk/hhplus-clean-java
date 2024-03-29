package com.example.hhpluscleanjava.lecture.service;

import com.example.hhpluscleanjava.lecture.controller.dto.request.LectureRequest;
import com.example.hhpluscleanjava.lecture.domain.Applicant;
import com.example.hhpluscleanjava.lecture.domain.Lecture;
import com.example.hhpluscleanjava.lecture.domain.LectureStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LectureServiceTest {

    @InjectMocks
    private LectureService lectureService;

    @Mock
    private LectureRepository lectureRepository;

    @Mock
    private ApplicantRepository applicantRepository;

    @Nested
    @DisplayName("applyLecture 메서드")
    class applyLecture {
        @DisplayName("특정 유저가 특강을 신청 시 정상적으로 신청이 된다.")
        @Test
        void applyLecture_success() {
            // given
            LectureRequest request = LectureRequest.builder()
                    .userId(1L)
                    .lectureId(1L)
                    .build();

            when(lectureRepository.findById(any())).thenReturn(Optional.of(Lecture.builder()
                    .id(1L)
                    .title("강의")
                    .capacityCount(29)
                    .openDate(ZonedDateTime.of(LocalDateTime.of(2024, 4, 1, 1, 0), ZoneId.of("Asia/Seoul")))
                    .build()));
            when(applicantRepository.existsByLectureAndUserId(any(), anyLong())).thenReturn(false);
            when(applicantRepository.save(any(Applicant.class))).thenReturn(
                    Applicant.builder()
                            .status(LectureStatus.APPLY)
                            .build());

            // when
            Applicant applicant = lectureService.applyLecture(request,
                    ZonedDateTime.of(LocalDateTime.of(2024, 4, 2, 1, 0), ZoneId.of("Asia/Seoul")));

            // then
            assertThat(applicant.getStatus()).isEqualTo(LectureStatus.APPLY);
        }

        @DisplayName("특강을 오픈된 날짜 이전에 신청할 시 예외를 발생시킨다.")
        @Test
        void applyLecture_fail_date() {
            // given
            LectureRequest request = LectureRequest.builder()
                    .userId(1L)
                    .lectureId(1L)
                    .build();

            when(lectureRepository.findById(any())).thenReturn(Optional.of(Lecture.builder()
                    .id(1L)
                    .title("강의")
                    .capacityCount(29)
                    .openDate(ZonedDateTime.of(LocalDateTime.of(2024, 4, 1, 1, 0), ZoneId.of("Asia/Seoul")))
                    .build()));

            // when than
            assertThatThrownBy(() -> lectureService.applyLecture(request,
                    ZonedDateTime.of(LocalDateTime.of(2024, 3, 31, 1, 0), ZoneId.of("Asia/Seoul"))))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("특강신청 기간이 아닙니다.");
        }

        @DisplayName("특강 신청 시 정원수 이상이면 예외를 발생시킨다.")
        @Test
        void applyLecture_fail_overCount() {
            // given
            LectureRequest request = LectureRequest.builder()
                    .userId(1L)
                    .lectureId(1L)
                    .build();

            when(lectureRepository.findById(any())).thenReturn(Optional.of(Lecture.builder()
                    .id(1L)
                    .title("강의")
                    .capacityCount(30)
                    .openDate(ZonedDateTime.of(LocalDateTime.of(2024, 4, 1, 1, 0), ZoneId.of("Asia/Seoul")))
                    .build()));

            // when then
            assertThatThrownBy(() -> lectureService.applyLecture(request,
                    ZonedDateTime.of(LocalDateTime.of(2024, 4, 1, 1, 0), ZoneId.of("Asia/Seoul"))))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("인원이 초과되었습니다.");
        }

        @DisplayName("특강 신청 시 이미 신청된 내역이 존재하면 예외를 발생시킨다.")
        @Test
        void applyLecture_fail_duplication() {
            // given
            LectureRequest request = LectureRequest.builder()
                    .userId(1L)
                    .lectureId(1L)
                    .build();

            when(lectureRepository.findById(any())).thenReturn(Optional.of(Lecture.builder()
                    .id(1L)
                    .title("강의")
                    .capacityCount(29)
                    .openDate(ZonedDateTime.of(LocalDateTime.of(2024, 4, 1, 1, 0), ZoneId.of("Asia/Seoul")))
                    .build()));
            when(applicantRepository.existsByLectureAndUserId(any(), anyLong())).thenReturn(true);

            // when then
            assertThatThrownBy(() -> lectureService.applyLecture(request,
                    ZonedDateTime.of(LocalDateTime.of(2024, 4, 1, 1, 0), ZoneId.of("Asia/Seoul"))))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("이미 신청한 내역이 존재합니다.");
        }
    }


    @DisplayName("특정 유저가 신청한 특정 특강의 정보를 조회한다.")
    @Test
    void getApplicant() {
        // given
        Long userId = 1L;
        Long lectureId = 1L;

        when(applicantRepository.findByLectureIdAndUserId(lectureId, userId)).thenReturn(Optional.of(Applicant.builder().build()));

        // when
        Applicant applicant = lectureService.getApplicant(lectureId, userId);

        // then
        assertThat(applicant).isNotNull();
    }


}