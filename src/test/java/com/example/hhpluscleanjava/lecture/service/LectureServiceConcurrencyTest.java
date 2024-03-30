package com.example.hhpluscleanjava.lecture.service;

import com.example.hhpluscleanjava.lecture.controller.dto.request.LectureRequest;
import com.example.hhpluscleanjava.lecture.domain.Lecture;
import com.example.hhpluscleanjava.lecture.infrastructure.LectureJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 동시성 테스트는 통합테스트 기준하에 작성하였습니다.
 */
@SpringBootTest
public class LectureServiceConcurrencyTest {

    @Autowired
    private LectureService lectureService;

    @Autowired
    private LectureJpaRepository lectureRepository;

    @DisplayName("수강 신청에 대한 동시성 테스트를 진행한다.")
    @Test
    void applyLecture_currency() throws InterruptedException {
        // given
        Lecture lecture = lectureRepository.save(
                Lecture.builder().title("강의").capacityCount(0).openDate(ZonedDateTime.of(LocalDateTime.of(2024, 4, 1, 1, 0), ZoneId.of("Asia/Seoul"))).build());

        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        Long userId = 0L;

        // when
        for (int i = 0; i < threadCount; i++) {
            final Long currentUserId = userId + i; // 각 스레드마다 고유한 userId를 생성
            executorService.submit(() -> {
                try {
                    LectureRequest request = LectureRequest.builder()
                            .userId(currentUserId)
                            .lectureId(lecture.getId())
                            .build();

                    lectureService.applyLecture(request, ZonedDateTime.of(LocalDateTime.of(2024, 4, 2, 1, 0), ZoneId.of("Asia/Seoul")));
                } catch (Exception e) {
                    System.err.println("Error occurred for User " + userId + " during usage: " + e.getMessage());
                }finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        Lecture result = lectureRepository.findById(lecture.getId()).get();

        // then
        assertThat(result.getCapacityCount()).isEqualTo(30);
    }
}
