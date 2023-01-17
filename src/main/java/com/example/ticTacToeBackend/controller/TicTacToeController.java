package com.example.ticTacToeBackend.controller;

import com.example.ticTacToeBackend.exception.BoxAlreadyMarkedException;
import com.example.ticTacToeBackend.exception.NotPlayerTurnException;
import com.example.ticTacToeBackend.model.BoxInfo;
import com.example.ticTacToeBackend.model.GameStatus;
import com.example.ticTacToeBackend.model.MarkBoxRequest;
import com.example.ticTacToeBackend.service.TicTacToeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/game")
public class TicTacToeController {

    private static Logger LOGGER = LogManager.getLogger(TicTacToeController.class);

    @Autowired
    TicTacToeService service;

    @PostMapping("/create")
    public Integer createGame(){
        Integer gameSessionId = service.createGame();
        return gameSessionId;

    }

    @PostMapping("/")
    public GameStatus markBox(@RequestBody MarkBoxRequest request) throws NotPlayerTurnException, BoxAlreadyMarkedException {
        GameStatus gameStatus = service.markBox(request);
        return gameStatus;
    }

    @GetMapping("/gameStatus/{gameSessionId}/{player}")
    public GameStatus getGameStatus(@PathVariable("gameSessionId") Integer gameSessionId
            , @PathVariable("player") String player){
        GameStatus result = service.getGameStatus(gameSessionId, player);
        return result;
    }

    @GetMapping("/componentFilledBy/{gameSessionId}/{boxIndex}")
    public BoxInfo getComponentFilledBy(@PathVariable("gameSessionId") Integer gameSessionId
            , @PathVariable("boxIndex") Integer boxIndex){
        BoxInfo result = service.getComponentFilledBy(gameSessionId, boxIndex);
        return result;
    }
    @GetMapping("/getAllBoxState/{gameSessionId}")
    public List<BoxInfo> getAllBoxState(@PathVariable("gameSessionId") Integer gameSessionId){
        List<BoxInfo> result = service.getAllBoxState(gameSessionId);
        return result;
    }
}
