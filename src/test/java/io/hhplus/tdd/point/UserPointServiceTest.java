package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

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
}