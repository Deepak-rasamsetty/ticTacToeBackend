package com.example.ticTacToeBackend.respository;

import com.example.ticTacToeBackend.entity.PlayerTurn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerTurnRepository extends JpaRepository<PlayerTurn, Integer> {
}
