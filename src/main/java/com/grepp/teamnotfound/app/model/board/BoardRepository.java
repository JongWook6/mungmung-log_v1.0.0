package com.grepp.teamnotfound.app.model.board;

import com.grepp.teamnotfound.app.model.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

}
