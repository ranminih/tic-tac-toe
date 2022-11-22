package com.javaproject.TicTacToe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "TicTacToe API", version = "2.0", description = "Tic Tac Toe API demo with AI component"))
public class TicTacToeApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicTacToeApplication.class, args);
	}

}
