package com.grepp.teamnotfound.app.model.board;

import com.grepp.teamnotfound.app.model.board.code.BoardType;
import com.grepp.teamnotfound.app.model.board.code.SearchType;
import com.grepp.teamnotfound.app.model.board.dto.ArticleListDto;
import com.grepp.teamnotfound.app.model.board.repository.ArticleRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ModelMapper modelMapper;

    public Page<ArticleListDto> findPaged(PageRequest pageable) {
//        List articleRepository.findPaged(pageable);
        return null;
    }

    public Page<ArticleListDto> searchArticles(BoardType boardType, SearchType searchType,
        String keyword, PageRequest pageable) {

        if (keyword == null || searchType == null) {
            return articleRepository.findByBoard(boardType, pageable);
        }



        return null;
    }
}
