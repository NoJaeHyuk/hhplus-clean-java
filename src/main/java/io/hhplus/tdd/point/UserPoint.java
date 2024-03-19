package io.hhplus.tdd.point;

public record UserPoint(
        long id,
        long point,
        long updateMillis
) {
    private static final long MAX_POINT = 1000;

    public UserPoint addPoint(long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("올바르지 않은 충전 금액이 들어왔습니다.");
        }

        long updatedPoint = this.point + amount;
        if (updatedPoint > MAX_POINT) {
            throw new IllegalArgumentException("충전 한도를 넘어갔습니다.");
        }

        return new UserPoint(id, updatedPoint, System.currentTimeMillis());
    }

    public UserPoint usePoint(long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("포인트값은 양수여야 합니다.");
        }

        if (point < amount) {
            throw new IllegalArgumentException("사용 포인트가 기존 포인트보다 클 수 없습니다.");
        }

        return new UserPoint(id, point - amount, System.currentTimeMillis());
    }

    public static UserPoint empty(long id) {
        return new UserPoint(id, 0, System.currentTimeMillis());
    }
}
