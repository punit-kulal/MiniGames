package com.example.android.minigames;

import java.util.ArrayList;
import java.util.List;

public class TicTacToeAI {
    Integer[][] board;
    static final int COMPUTER = 1;
    static final int HUMAN = -1;
    static final int DEALLOCATE = 0;
    final int SIZE = 3;
    
    /**
     * Constructor with the given game board
     */
    public TicTacToeAI(Integer[][] board) {
        this.board = board;
    }

    /**
     * Get next best move for COMPUTER. Return int[2] of {row, col}
     */
    int[] move() {
        int[] result = minimax(4, COMPUTER); // depth, max turn
        return new int[]{result[1], result[2]};   // row, col
    }

    /**
     * Recursive minimax at level of depth for either maximizing or minimizing player.
     * Return int[3] of {score, row, col}
     */
    private int[] minimax(int depth, int player) {
        // Generate possible next moves in a List of int[2] of {row, col}.
        List<int[]> nextMoves = generateMoves();

        // COMPUTER is maximizing; while HUMAN is minimizing
        int bestScore = (player == COMPUTER) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int currentScore;
        int bestRow = -1;
        int bestCol = -1;

        if (nextMoves.isEmpty() || depth == 0) {
            // Gameover or depth reached, evaluate score
            bestScore = evaluate();
        } else {
            for (int[] move : nextMoves) {
                // Try this move for the current "player"
                board[move[0]][move[1]] = player;
                if (player == COMPUTER) {  // COMPUTER (COMPUTER) is maximizing player
                    currentScore = minimax(depth - 1, HUMAN)[0];
                    if (currentScore > bestScore) {
                        bestScore = currentScore;
                        bestRow = move[0];
                        bestCol = move[1];
                    }
                } else {  // HUMAN is minimizing player
                    currentScore = minimax(depth - 1, COMPUTER)[0];
                    if (currentScore < bestScore) {
                        bestScore = currentScore;
                        bestRow = move[0];
                        bestCol = move[1];
                    }
                }
                // Undo move
                board[move[0]][move[1]] = DEALLOCATE;
            }
        }
        return new int[]{bestScore, bestRow, bestCol};
    }

    /**
     * Find all valid next moves.
     * Return List of moves in int[2] of {row, col} or empty list if gameover
     */
    private List<int[]> generateMoves() {
        List<int[]> nextMoves = new ArrayList<int[]>(); // allocate List

        // If gameover, i.e., no next move
        if (hasWon()) {
            return nextMoves;   // return empty list
        }

        // Search for empty board and add to the List
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 3; ++col) {
                if (board[row][col] == DEALLOCATE) {
                    nextMoves.add(new int[]{row, col});
                }
            }
        }
        return nextMoves;
    }

    /**
     * The heuristic evaluation function for the current board
     *
     * @Return +100, +10, +1 for EACH 3-, 2-, 1-in-a-line for COMPUTER.
     * -100, -10, -1 for EACH 3-, 2-, 1-in-a-line for opponent.
     * 0 otherwise
     */
    private int evaluate() {
        int score = 0;
        // Evaluate score for each of the 8 lines (3 rows, 3 columns, 2 diagonals)
        score += evaluateLine(0, 0, 0, 1, 0, 2);  // row 0
        score += evaluateLine(1, 0, 1, 1, 1, 2);  // row 1
        score += evaluateLine(2, 0, 2, 1, 2, 2);  // row 2
        score += evaluateLine(0, 0, 1, 0, 2, 0);  // col 0
        score += evaluateLine(0, 1, 1, 1, 2, 1);  // col 1
        score += evaluateLine(0, 2, 1, 2, 2, 2);  // col 2
        score += evaluateLine(0, 0, 1, 1, 2, 2);  // diagonal
        score += evaluateLine(0, 2, 1, 1, 2, 0);  // alternate diagonal
        return score;
    }

    /**
     * The heuristic evaluation function for the given line of 3 board
     *
     * @Return +100, +10, +1 for 3-, 2-, 1-in-a-line for COMPUTER.
     * -100, -10, -1 for 3-, 2-, 1-in-a-line for opponent.
     * 0 otherwise
     */
    private int evaluateLine(int row1, int col1, int row2, int col2, int row3, int col3) {
        int score = 0;

        // First cell
        if (board[row1][col1] == COMPUTER) {
            score = 1;
        } else if (board[row1][col1] == HUMAN) {
            score = -1;
        }

        // Second cell
        if (board[row2][col2] == COMPUTER) {
            if (score == 1) {   // cell1 is COMPUTER
                score = 10;
            } else if (score == -1) {  // cell1 is HUMAN
                return 0;
            } else {  // cell1 is empty
                score = 1;
            }
        } else if (board[row2][col2] == HUMAN) {
            if (score == -1) { // cell1 is HUMAN
                score = -10;
            } else if (score == 1) { // cell1 is COMPUTER
                return 0;
            } else {  // cell1 is empty
                score = -1;
            }
        }

        // Third cell
        if (board[row3][col3] == COMPUTER) {
            if (score > 0) {  // cell1 and/or cell2 is COMPUTER
                score *= 10;
            } else if (score < 0) {  // cell1 and/or cell2 is HUMAN
                return 0;
            } else {  // cell1 and cell2 are empty
                score = 1;
            }
        } else if (board[row3][col3] == HUMAN) {
            if (score < 0) {  // cell1 and/or cell2 is HUMAN
                score *= 10;
            } else if (score > 1) {  // cell1 and/or cell2 is COMPUTER
                return 0;
            } else {  // cell1 and cell2 are empty
                score = -1;
            }
        }
        return score;
    }

    private boolean hasWon(){
        for (int i = 0; i < SIZE; i++) {
            if(board[SIZE][0]== board[SIZE][1]&& board[SIZE][1]== board[SIZE][2])
                return true;
            if (board[0][SIZE]== board[1][SIZE] && board[1][SIZE]== board[2][SIZE])
                return true;
        }
        if(board[0][0]==board[1][1]&&board[1][1]==board[2][2])
            return true;
        if(board[0][2]==board[1][1]&& board[1][1]==board[2][0])
            return true;
        return false;
    }
}
