package com.example.ticTacToeBackend.exception;

public class NotPlayerTurnException  extends Exception{
    private  static final  String message = "Not players turn";
    public NotPlayerTurnException(){
        super(message);
    }
}

