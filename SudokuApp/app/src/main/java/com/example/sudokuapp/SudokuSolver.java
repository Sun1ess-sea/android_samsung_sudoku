package com.example.sudokuapp;

import java.util.Random;

public class SudokuSolver {

    private final int[][] board;
    private static final int SIZE = 9;

    public SudokuSolver() {
        this.board = new int[SIZE][SIZE];
    }

    public int[][] getBoard() {
        return board;
    }

    public boolean solve() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == 0) { // Пустая клетка
                    for (int num = 1; num <= SIZE; num++) {
                        if (isSafe(row, col, num)) {
                            board[row][col] = num;

                            if (solve()) {
                                return true;
                            }

                            board[row][col] = 0; // Отмена
                        }
                    }
                    return false;
                }
            }
        }
        return true; // Успешно решено
    }

    private boolean isSafe(int row, int col, int num) {
        // Проверяем строку
        for (int x = 0; x < SIZE; x++) {
            if (board[row][x] == num) {
                return false;
            }
        }

        // Проверяем колонку
        for (int x = 0; x < SIZE; x++) {
            if (board[x][col] == num) {
                return false;
            }
        }

        // Проверяем 3x3 квадрат
        int startRow = row - row % 3;
        int startCol = col - col % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[startRow + i][startCol + j] == num) {
                    return false;
                }
            }
        }

        return true;
    }

    // Метод для генерации доски решения
    public void generateSolutionBoard() {
        fillDiagonalBlocks(); // Заполняем диагональные блоки
        solve(); // Полностью заполняем доску
    }

    private void fillDiagonalBlocks() {
        for (int i = 0; i < SIZE; i += 3) {
            fillBlock(i, i);
        }
    }

    private void fillBlock(int startRow, int startCol) {
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int num;
                do {
                    num = random.nextInt(SIZE) + 1;
                } while (!isSafe(startRow + i, startCol + j, num));
                board[startRow + i][startCol + j] = num;
            }
        }
    }
}
