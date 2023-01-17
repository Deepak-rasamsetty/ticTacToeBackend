package com.example.ticTacToeBackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoxInfo {
    String player;
    Integer boxIndex;
}
