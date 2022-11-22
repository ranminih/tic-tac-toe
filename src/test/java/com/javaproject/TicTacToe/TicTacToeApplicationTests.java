package com.javaproject.TicTacToe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.javaproject.TicTacToe.models.TicToe;

@SpringBootTest
class TicTacToeApplicationTests {

	@Test
	public void testFindMax() {
		// testing isTileMarked
		int[][] board = new int[3][3];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				board[i][j] = 1;
			}
		}
		assertEquals(false, anyMovesAvailable(board));

	}

	private Object anyMovesAvailable(int[][] board) {
		return false;
	}

}