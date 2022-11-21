package com.javaproject.TicTacToe.controller.dto;

import com.javaproject.TicTacToe.models.Player;
import lombok.Data;

@Data
public class ConnectRequest {
    private Player player;
    private String gameId;
}
