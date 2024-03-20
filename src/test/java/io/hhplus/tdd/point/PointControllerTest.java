package io.hhplus.tdd.point;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 컨트롤러 단위 테스트입니다.
 * 컨트롤러 테스트에서는 주로 정상 응답값 확인과 요청 파라미터의 Validation 체크를 진행합니다.
 * 이번 과제에서는 Validation 의존성이 없어 성공 요청에 대한 응답값만 체크하였습니다.
 */
@WebMvcTest(controllers = PointController.class)
class PointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserPointService userPointService;

    @DisplayName("특정 유저의 포인트를 조회한다.")
    @Test
    void point() throws Exception {
        // given
        long userId = 1L;
        long pointAmount = 100L;
        UserPoint userPoint = new UserPoint(userId, pointAmount, System.currentTimeMillis());

        given(userPointService.getUserPointById(userId)).willReturn(userPoint);

        // when then
        mockMvc.perform(
                        get("/point/" + userId)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.point").value(100));
    }

    @DisplayName("특정 유저의 포인트 충전/이용 내역을 조회한다.")
    @Test
    void history() throws Exception {
        // given
        long userId = 1L;
        List<PointHistory> pointHistories = Arrays.asList(
                new PointHistory(1L, userId, 100L, TransactionType.CHARGE, System.currentTimeMillis()),
                new PointHistory(2L, userId, 50L, TransactionType.USE, System.currentTimeMillis())
        );

        given(userPointService.getPointHistories(userId)).willReturn(pointHistories);

        // when than
        mockMvc.perform(
                        get("/point/" + userId + "/histories")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].userId").value(1L))
                .andExpect(jsonPath("$[0].type").value("CHARGE"))
                .andExpect(jsonPath("$[0].amount").value(100L))
                .andExpect(jsonPath("$[1].userId").value(1L))
                .andExpect(jsonPath("$[1].type").value("USE"))
                .andExpect(jsonPath("$[1].amount").value(50L))
        ;
    }

    @DisplayName("특정 유저에게 포인트를 충전한다.")
    @Test
    void charge() throws Exception {
        // given
        long userId = 1L;
        long amount = 100L;
        UserPoint userPoint = new UserPoint(userId, amount, System.currentTimeMillis());

        given(userPointService.chargePoint(userId, amount)).willReturn(userPoint);

        // when than
        mockMvc.perform(patch("/point/" + userId + "/charge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(amount)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.point").value(100L));
    }

    @DisplayName("특정 유저의 포인트를 사용한다")
    @Test
    void use() throws Exception {
        // given
        long userId = 1L;
        long amount = 50L;
        UserPoint userPoint = new UserPoint(userId, amount, System.currentTimeMillis());

        given(userPointService.usePoint(userId, amount)).willReturn(userPoint);

        // when
        mockMvc.perform(patch("/point/" + userId + "/use")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(amount)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.point").value(50L));
    }


}