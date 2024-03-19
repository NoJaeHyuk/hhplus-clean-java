package io.hhplus.tdd.point;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("UserPoint 테스트")
class UserPointTest {

    @Nested
    @DisplayName("addPoint()는 ")
    class addPoint {
        @DisplayName("유저의 기존 포인트에 충전한 포인트를 추가한다.")
        @Test
        void userPoint_add_point() {
            // given
            UserPoint userPoint = UserPoint.empty(1L);

            // when
            UserPoint addedPoint = userPoint.addPoint(100);

            // then
            assertThat(addedPoint.point()).isEqualTo(100);
        }

        @DisplayName("충전한 포인트가 양수값이 아닌 경우 예외를 발생한다.")
        @Test
        void addPoint_양수_아닐경우() {
            // given
            UserPoint userPoint = UserPoint.empty(1L);

            // when then
            assertThatThrownBy(() -> userPoint.addPoint(0))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("올바르지 않은 충전 금액이 들어왔습니다.");
        }

        @DisplayName("충전 시 합산 포인트가 최대 한도를 넘어갔을경우 예외를 발생한다.")
        @Test
        void addPoint_최대_한도_넘어갔을경우() {
            // given
            UserPoint userPoint = UserPoint.empty(1L);

            // when then
            assertThatThrownBy(() -> userPoint.addPoint(1001))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("충전 한도를 넘어갔습니다.");
        }
    }


}