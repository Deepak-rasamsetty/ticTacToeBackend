package com.example.ticTacToeBackend.respository;

import com.example.ticTacToeBackend.entity.GameSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GameSessionRepository extends JpaRepository<GameSession, Integer> {

    GameSession findByGameSessionIdAndBoxIndex(Integer gameSessionId, Integer boxMarked);

    @Query(value="SELECT LISTAGG(BOX_INDEX,'') FROM GAME_SESSION WHERE GAME_SESSION_ID = ?1 AND PLAYER = ?2", nativeQuery = true)
    String findByGameSessionIdAndPlayer(Integer gameSessionId, String player);

    List<GameSession> findByGameSessionId(Integer gameSessionId);

    List<GameSession> findByGameSessionIdAndPlayerIsNull(Integer gameSessionId);
}
