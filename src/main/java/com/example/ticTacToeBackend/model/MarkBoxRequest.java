package com.example.ticTacToeBackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarkBoxRequest {
    private Integer gameSessionId;
    private String player;
    private Integer boxIndex;
}
