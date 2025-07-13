package com.grepp.teamnotfound.app.model.board.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.grepp.teamnotfound.app.model.board.entity.Board;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    void name() {
        List<Board> all = boardRepository.findAll();
        System.out.println(all);
        
    }
}