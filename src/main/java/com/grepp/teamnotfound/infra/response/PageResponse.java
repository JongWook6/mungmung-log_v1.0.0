package com.grepp.teamnotfound.infra.response;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

@RequiredArgsConstructor
public class PageResponse<T> {

    private final String url;
    private final Page<T> page;
    private final int pageButtonCnt;

    public String url() {
        return url;
    }

    // 현재 페이지 (Spring Data 의 Page 는 0부터 시작하므로 + 1)
    public int currentNumber() {
        return page.getNumber() + 1;
    }

    public int prevPage() {
        return Math.max(currentNumber() - 1, 1);
    }

    public int nextPage() {
        return Math.min(currentNumber() + 1, calcTotalPage());
    }

    // 페이지 버튼을 n개 생성할 때 첫번째 버튼의 숫자
    public int startNumber() {
        return Math.floorDiv(page.getNumber(), pageButtonCnt) * pageButtonCnt + 1;
    }

    // 페이지 버튼을 n개 생성할 때 n번째 버튼의 숫자
    public int endNumber() {
        return Math.min(startNumber() + pageButtonCnt - 1, calcTotalPage());
    }

    public List<T> content() {
        return page.getContent();
    }

    // 전체 페이지 개수 (데이터가 없을 때도 최소 1페이지로 처리)
    private int calcTotalPage() {
        int totalPage = page.getTotalPages();
        return totalPage == 0 ? 1 : totalPage;
    }


}
