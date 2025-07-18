package com.grepp.teamnotfound.app.model.board.repository;

import com.grepp.teamnotfound.app.model.board.entity.Board;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Optional<Board> findByName(String name);
}
