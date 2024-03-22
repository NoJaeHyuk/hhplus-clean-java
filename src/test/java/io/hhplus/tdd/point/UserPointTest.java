package io.hhplus.tdd.point;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 객체에 대한 단위 테스트입니다.
 * 아직 객체지향적인 코드 작성에 익숙하지 않아
 * 샘플코드에 있는 레코드 클래스를 객체라 생각하고 작성하였습니다.
 */
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

    @Nested
    @DisplayName("usePoint()는 ")
    class usePoint {

        @DisplayName("사용자의 포인트가 사용한 포인트 만큼 감소한다.")
        @Test
        void will_success() {
            // given
            UserPoint userPoint = new UserPoint(1L, 500, System.currentTimeMillis());

            // when
            UserPoint result = userPoint.usePoint(100);

            // then
            assertThat(result.point()).isEqualTo(400);
        }

        @DisplayName("사용 포인트가 양수값이 아닌 경우 예외를 발생시킨다.")
        @Test
        void fail_양수값이_아닌경우() {
            // given
            UserPoint userPoint = new UserPoint(1L, 500, System.currentTimeMillis());

            // when then
            assertThatThrownBy(() -> userPoint.usePoint(0))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("포인트값은 양수여야 합니다.");
        }

        @DisplayName("사용 포인트가 기존 포인트보다 초과할 경우 예외를 발생시킨다.")
        @Test
        void fail_사용포인트_초과경우() {
            // given
            UserPoint userPoint = new UserPoint(1L, 500, System.currentTimeMillis());

            // when then
            assertThatThrownBy(() -> userPoint.usePoint(501))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("사용 포인트가 기존 포인트보다 클 수 없습니다.");
        }
    }


}