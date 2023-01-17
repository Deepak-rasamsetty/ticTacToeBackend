package com.example.ticTacToeBackend.service;

import com.example.ticTacToeBackend.entity.GameDetails;
import com.example.ticTacToeBackend.entity.GameSession;
import com.example.ticTacToeBackend.entity.PlayerTurn;
import com.example.ticTacToeBackend.exception.BoxAlreadyMarkedException;
import com.example.ticTacToeBackend.exception.NotPlayerTurnException;
import com.example.ticTacToeBackend.model.BoxInfo;
import com.example.ticTacToeBackend.model.GameStatus;
import com.example.ticTacToeBackend.model.MarkBoxRequest;
import com.example.ticTacToeBackend.respository.GameDetailsRepository;
import com.example.ticTacToeBackend.respository.GameSessionRepository;
import com.example.ticTacToeBackend.respository.PlayerTurnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.*;
import java.util.function.Consumer;

@Service
public class TicTacToeService {

    @Autowired
    GameSessionRepository gameSessionRepository;

    @Autowired
    GameDetailsRepository gameDetailsRepository;

    @Autowired
    PlayerTurnRepository playerTurnRepository;
    public Integer createGame() {
        GameDetails game = new GameDetails();
        game.setCreatedOn(new Date().toString());
        game.setStatus("CREATED");
        GameDetails createdGame = gameDetailsRepository.save(game);

        for(int i=1;i<=9;i++){
            GameSession session = new GameSession();
            session.setGameSessionId(createdGame.getGameSessionId());
            session.setBoxIndex(i);
            gameSessionRepository.save(session);
        }

        PlayerTurn playerTurnRecord = new PlayerTurn();
        playerTurnRecord.setGameSessionId(createdGame.getGameSessionId());
        playerTurnRecord.setPlayer("P1");
        playerTurnRepository.save(playerTurnRecord);
        return createdGame.getGameSessionId();
    }

    public GameStatus markBox(MarkBoxRequest request) throws NotPlayerTurnException, BoxAlreadyMarkedException {

        if(!isPlayerTurn(request.getGameSessionId(), request.getPlayer())){
            throw new NotPlayerTurnException();
        }
        GameSession session= gameSessionRepository.findByGameSessionIdAndBoxIndex(request.getGameSessionId(),request.getBoxIndex());
        System.out.println(session);
        if(session.getPlayer()!=null){
            throw new BoxAlreadyMarkedException();
        }

        session.setPlayer(request.getPlayer());
        gameSessionRepository.save(session);


        PlayerTurn playerTurnRecord = playerTurnRepository.getReferenceById(request.getGameSessionId());
        if(request.getPlayer().equalsIgnoreCase("P1")){
            playerTurnRecord.setPlayer("P2");
        }else{
            playerTurnRecord.setPlayer("P1");
        }
        playerTurnRepository.save(playerTurnRecord);
        GameStatus result = checkGameStatus(request.getGameSessionId(), request.getPlayer());
        return result;
    }

    private GameStatus checkGameStatus(Integer gameSessionId, String player) {
        String allPlayerRecords = gameSessionRepository.findByGameSessionIdAndPlayer(gameSessionId, player);
        GameStatus response = new GameStatus();
        System.out.println(allPlayerRecords.toString());
        if((allPlayerRecords.contains("1") && allPlayerRecords.contains("2") && allPlayerRecords.contains("3"))||
            (allPlayerRecords.contains("4") && allPlayerRecords.contains("5") && allPlayerRecords.contains("6"))||
            (allPlayerRecords.contains("7") && allPlayerRecords.contains("8") && allPlayerRecords.contains("9"))||
            (allPlayerRecords.contains("1") && allPlayerRecords.contains("4") && allPlayerRecords.contains("7"))||
            (allPlayerRecords.contains("2") && allPlayerRecords.contains("5") && allPlayerRecords.contains("8"))||
            (allPlayerRecords.contains("3") && allPlayerRecords.contains("6") && allPlayerRecords.contains("9"))||
            (allPlayerRecords.contains("1") && allPlayerRecords.contains("5") && allPlayerRecords.contains("9"))||
            (allPlayerRecords.contains("3") && allPlayerRecords.contains("5") && allPlayerRecords.contains("7"))

        ){
            response.setStatus("COMPLETED");
            response.setMessage((player.equalsIgnoreCase("P1")?"Player1":"Player2") +" Won");
            GameDetails gameDetails = gameDetailsRepository.getReferenceById(gameSessionId);
            gameDetails.setStatus("COMPLETED");
            gameDetails.setMessage((player.equalsIgnoreCase("P1")?"Player1":"Player2")  +" Won");
            gameDetailsRepository.save(gameDetails);


        }else{
           List<GameSession> allFreeSessionRecords = gameSessionRepository.findByGameSessionIdAndPlayerIsNull(gameSessionId);
           if(allFreeSessionRecords.isEmpty()){
               response.setStatus("DRAWN");
               response.setMessage("Match drawn, no empty places left");
               GameDetails gameDetails = gameDetailsRepository.getReferenceById(gameSessionId);
               gameDetails.setStatus("COMPLETED");
               gameDetails.setMessage("Match drawn, no empty places left");
               gameDetailsRepository.save(gameDetails);
           }else{
               response.setStatus("INPROGRESS");
               response.setMessage("Match still in progress");
           }

        }
        return response;
    }


    public Boolean isPlayerTurn(Integer gameSessionId, String player) {
        PlayerTurn playerTurnRecord = playerTurnRepository.getReferenceById(gameSessionId);
        return playerTurnRecord.getPlayer().equals(player);

    }
    public GameStatus getGameStatus(Integer gameSessionId, String player) {
        GameStatus gameStatus = new GameStatus();
        GameDetails gameDetails = gameDetailsRepository.getReferenceById(gameSessionId);
        PlayerTurn playerTurnRecord = playerTurnRepository.getReferenceById(gameSessionId);
        gameStatus.setPlayerTurn(playerTurnRecord.getPlayer().equals(player));
        gameStatus.setStatus(gameDetails.getStatus());
        gameStatus.setMessage(gameDetails.getMessage());
        return gameStatus;

    }

    public BoxInfo getComponentFilledBy(Integer gameSessionId, Integer boxIndex) {
        GameSession session= gameSessionRepository.findByGameSessionIdAndBoxIndex(gameSessionId,boxIndex);
        BoxInfo result = new BoxInfo();

        if(session!=null){
            result.setPlayer(session.getPlayer());
        }else{
            result.setPlayer("0");
        }
        return result;


    }

    public List<BoxInfo> getAllBoxState(Integer gameSessionId) {
        List<GameSession> gameSessionList = gameSessionRepository.findByGameSessionId(gameSessionId);
        List<BoxInfo> boxInfoList = new ArrayList<>();
        gameSessionList.stream().forEach((gameSession)->{
                BoxInfo boxInfo = new BoxInfo();
                boxInfo.setBoxIndex(gameSession.getBoxIndex());
                boxInfo.setPlayer(gameSession.getPlayer());
                boxInfoList.add(boxInfo);
        });
        return boxInfoList;
    }
}
