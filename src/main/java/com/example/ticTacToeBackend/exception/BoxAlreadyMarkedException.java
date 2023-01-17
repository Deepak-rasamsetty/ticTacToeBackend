package com.example.ticTacToeBackend.exception;

public class BoxAlreadyMarkedException  extends Exception{
    private  static final  String message = "Box already marked";
    public BoxAlreadyMarkedException(){
        super(message);
    }
}
