package com.example.ticTacToeBackend.respository;

import com.example.ticTacToeBackend.entity.GameDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameDetailsRepository extends JpaRepository<GameDetails, Integer> {
}
