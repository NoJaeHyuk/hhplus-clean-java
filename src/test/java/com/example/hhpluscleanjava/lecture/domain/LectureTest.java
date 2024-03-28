package com.example.hhpluscleanjava.lecture.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class LectureTest {

    @Nested
    @DisplayName("isLectureRegistrationOpen() 메서드 검증")
    class isLectureRegistrationOpen {
        @DisplayName("특강 신청 오픈 전 신청 시 TRUE 를 반환한다.")
        @Test
        void currentDate_less() {
            // given
            Lecture lecture = Lecture.builder()
                    .title("특강")
                    .openDate(ZonedDateTime.of(LocalDateTime.of(2024, 4, 1, 1, 0), ZoneId.of("Asia/Seoul")))
                    .build();

            // when
            boolean result = lecture.isLectureRegistrationOpen(
                    ZonedDateTime.of(LocalDateTime.of(2024, 3, 31, 1, 0), ZoneId.of("Asia/Seoul"))
            );

            // then
            assertThat(result).isTrue();
        }

        @DisplayName("오픈시간과 동일한 시간에 신청시 False 반환된다.")
        @Test
        void currentDate_equals() {
            // given
            Lecture lecture = Lecture.builder()
                    .title("특강")
                    .openDate(ZonedDateTime.of(LocalDateTime.of(2024, 4, 1, 1, 0), ZoneId.of("Asia/Seoul")))
                    .build();

            // when
            boolean result = lecture.isLectureRegistrationOpen(
                    ZonedDateTime.of(LocalDateTime.of(2024, 4, 1, 1, 0), ZoneId.of("Asia/Seoul"))
            );

            // then
            assertThat(result).isFalse();
        }

        @DisplayName("오픈시간 이 후에 신청시 False 반환된다.")
        @Test
        void currentDate_over() {
            // given
            Lecture lecture = Lecture.builder()
                    .title("특강")
                    .openDate(ZonedDateTime.of(LocalDateTime.of(2024, 4, 1, 1, 0), ZoneId.of("Asia/Seoul")))
                    .build();

            // when
            boolean result = lecture.isLectureRegistrationOpen(
                    ZonedDateTime.of(LocalDateTime.of(2024, 4, 2, 1, 0), ZoneId.of("Asia/Seoul"))
            );

            // then
            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("isCapacityExceeded 메서드 검증")
    class isCapacityExceeded {

        @DisplayName("최대 정원 수를 같거나 넘을 시 true를 반환한다.")
        @CsvSource({"30", "31"})
        @ParameterizedTest
        void over(int count) {
            // given
            Lecture lecture = Lecture.builder()
                    .title("특강")
                    .capacityCount(count)
                    .build();

            // when
            boolean result = lecture.isCapacityExceeded(30);

            // then
            assertThat(result).isTrue();
        }

        @DisplayName("최대 인원 수 보다 적으면 false를 반환한다.")
        @Test
        void less() {
            // given
            Lecture lecture = Lecture.builder()
                    .title("특강")
                    .capacityCount(29)
                    .build();

            // when
            boolean result = lecture.isCapacityExceeded(30);

            // then
            assertThat(result).isFalse();
        }


    }


}