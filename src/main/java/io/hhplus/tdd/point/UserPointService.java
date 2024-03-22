package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserPointService {

    private final UserPointTable userPointTable;
    private final PointHistoryTable pointHistoryTable;

    public synchronized UserPoint chargePoint(long id, long amount) {
        pointHistoryTable.insert(id, amount, TransactionType.CHARGE, System.currentTimeMillis());

        UserPoint userPoint = userPointTable.selectById(id);
        UserPoint updatedPoint = userPoint.addPoint(amount);

        return userPointTable.insertOrUpdate(userPoint.id(), updatedPoint.point());
    }

    public UserPoint getUserPointById(long id) {
        return userPointTable.selectById(id);
    }

    public synchronized UserPoint usePoint(long id, long amount) {
        pointHistoryTable.insert(id, amount, TransactionType.USE, System.currentTimeMillis());

        UserPoint userPoint = userPointTable.selectById(id);
        UserPoint updatedPoint = userPoint.usePoint(amount);

        return userPointTable.insertOrUpdate(userPoint.id(), updatedPoint.point());
    }

    public List<PointHistory> getPointHistories(long id) {
        return pointHistoryTable.selectAllByUserId(id);
    }
}
