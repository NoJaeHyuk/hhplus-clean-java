package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

/**
 * Service 로직에 대한 테스트는 통합 테스트 관점에서 진행하였습니다.
 * 그래서 스프링컨테이너를 띄우기 위해 @SpringBootTest를 사용하였습니다.
 * 왜 통합테스트라서 명칭했냐면 한가지 이상의 레이어를 진행하기 때문입니다.
 * Repository 부분을 mock 처리하기도 하는데 로직 자체가 간단하여 @SpringBootTest를 이용해도 큰 문제 없다고 생각했습니다.
 */
@SpringBootTest
class UserPointServiceTest {

    @Autowired
    private UserPointTable userPointTable;

    @Autowired
    private PointHistoryTable pointHistoryTable;

    @Autowired
    private UserPointService userPointService;

    /**
     * 유저 포인트 충전에 대해 Service 로직 검증
     */
    @DisplayName("유저에 대한 포인트를 충전한다.")
    @Test
    void chargePoint() {
        // given
        UserPoint userPoint = new UserPoint(1L, 100, System.currentTimeMillis());
        UserPoint savedPoint = userPointTable.insertOrUpdate(userPoint.id(), userPoint.point());

        // when
        UserPoint chargedPoint = userPointService.chargePoint(savedPoint.id(), 200);

        // then
        assertThat(chargedPoint.point()).isEqualTo(300);
    }

    @DisplayName("유저의 식별자 값으로 UserPoint 정보를 조회한다.")
    @Test
    void getUserPointById() {
        // given
        UserPoint userPoint = new UserPoint(1L, 100, System.currentTimeMillis());
        userPointTable.insertOrUpdate(userPoint.id(), userPoint.point());

        // when
        UserPoint result = userPointService.getUserPointById(1L);

        // then
        assertThat(result.id()).isNotNull();
        assertThat(result).extracting("id", "point")
                .containsExactlyInAnyOrder(1L, 100L);
    }

    @DisplayName("유저에 대한 포인트를 사용한다.")
    @Test
    void usePoint() {
        // given
        UserPoint userPoint = new UserPoint(1L, 100, System.currentTimeMillis());
        userPointTable.insertOrUpdate(userPoint.id(), userPoint.point());

        // when
        UserPoint result = userPointService.usePoint(userPoint.id(), 50);

        // then
        assertThat(result.point()).isEqualTo(50);
    }

    @DisplayName("유저의 포인트 충전/사용 내역을 조회한다.")
    @Test
    void getPointHistories() {
        // given
        pointHistoryTable.insert(1L, 200, TransactionType.CHARGE, System.currentTimeMillis());
        pointHistoryTable.insert(1L, 100, TransactionType.USE, System.currentTimeMillis());

        // when
        List<PointHistory> pointHistories = userPointService.getPointHistories(1L);

        // then
        assertThat(pointHistories).hasSize(2)
                .extracting("id", "amount", "type")
                .containsExactly(
                        tuple(1L, 200L, TransactionType.CHARGE),
                        tuple(2L, 100L, TransactionType.USE)
                );
    }

    @Nested
    @DisplayName("포인트 충전, 사용에 대한 동시성 테스트")
    class 동시성테스트 {
        @DisplayName("포인트 충전에 대한 동시성 테스트")
        @Test
        void 충전_대한_동시성() throws InterruptedException {
            // given
            ExecutorService executorService = Executors.newFixedThreadPool(30);
            CountDownLatch latch = new CountDownLatch(100);

            AtomicInteger successCount = new AtomicInteger();
            AtomicInteger failedCount = new AtomicInteger();

            final long userId = 1; // 특정 사용자 ID
            final long amount = 100; // 충전 및 사용할 포인트 양

            // when
            for (int i = 0; i < 100; i++) {
                executorService.submit(() -> {
                    try {
                        userPointService.chargePoint(userId, amount);
                        successCount.incrementAndGet();
                    } catch (Exception e) {
                        System.err.println("Error occurred for User " + userId + " during usage: " + e.getMessage());
                        failedCount.incrementAndGet();
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await();

            // then
            System.out.println("success count : " + successCount.get());
            System.out.println("failed count : " + failedCount.get());

            List<PointHistory> pointHistories = userPointService.getPointHistories(1L);
            assertThat(pointHistories).hasSize(100);
        }

        @DisplayName("포인트 사용에 대한 동시성 테스트")
        @Test
        void 사용_대한_동시성() throws InterruptedException {
            // given
            ExecutorService executorService = Executors.newFixedThreadPool(30);
            CountDownLatch latch = new CountDownLatch(100);

            AtomicInteger successCount = new AtomicInteger();
            AtomicInteger failedCount = new AtomicInteger();

            final long userId = 1; // 특정 사용자 ID
            final long amount = 100; // 충전 및 사용할 포인트 양

            userPointTable.insertOrUpdate(userId, 10000);

            // when
            for (int i = 0; i < 100; i++) {
                executorService.submit(() -> {
                    try {
                        userPointService.usePoint(userId, amount);
                        successCount.incrementAndGet();
                    } catch (Exception e) {
                        System.err.println("Error occurred for User " + userId + " during usage: " + e.getMessage());
                        failedCount.incrementAndGet();
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await();

            // then
            System.out.println("success count : " + successCount.get());
            System.out.println("failed count : " + failedCount.get());

            List<PointHistory> pointHistories = userPointService.getPointHistories(1L);
            assertThat(pointHistories).hasSize(100);
        }
    }
}