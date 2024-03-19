package io.hhplus.tdd.point;

import io.hhplus.tdd.database.UserPointTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserPointServiceTest {

    @Autowired
    private UserPointTable userPointTable;

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
}