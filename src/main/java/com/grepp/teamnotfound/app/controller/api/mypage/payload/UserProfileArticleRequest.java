package com.grepp.teamnotfound.app.controller.api.mypage.payload;

import com.grepp.teamnotfound.app.model.board.code.SortType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileArticleRequest {

    @Min(value = 1, message = "페이지는 1 이상이어야 합니다.")
    @Max(value = Integer.MAX_VALUE, message = "페이지 값이 너무 큽니다.")
    private int page = 1;

    @Min(value = 1, message = "사이즈는 1 이상이어야 합니다.")
    @Max(value = Integer.MAX_VALUE, message = "사이즈 값이 너무 큽니다.")
    private int size = 5;

    @NotNull(message = "정렬 타입은 필수입니다.")
    private SortType sortType = SortType.DATE;

}
