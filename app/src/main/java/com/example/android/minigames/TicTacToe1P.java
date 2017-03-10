package com.example.android.minigames;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Stack;

import static com.example.android.minigames.R.layout.tic_tac_toe;
import static com.example.android.minigames.TicTacToeAI.DEALLOCATE;
import static com.example.android.minigames.TicTacToeAI.SIZE;
import static com.example.android.minigames.TicTacToeAI.board;
import static com.example.android.minigames.TicTacToeAI.hasWon;

public class TicTacToe1P extends AppCompatActivity {
    int[] imageSelector = new int[2];
    ImageView[][] list = new ImageView[3][3];
    boolean winner = false;
    int[][] idArray;
    int id, compImage = 0, humanImage = 1;
    Stack<Integer> undo = new Stack<>();
    TicTacToeAI pc = new TicTacToeAI();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(tic_tac_toe);
        idArray = new int[][]{{R.id.one, R.id.two, R.id.three},
                {R.id.four, R.id.five, R.id.six},
                {R.id.seven, R.id.eight, R.id.nine}};
        imageSelector[0] = R.mipmap.x;
        imageSelector[1] = R.mipmap.gameo;
        for (int i = 0; i < 3; i++) {
            Arrays.fill(board[i], DEALLOCATE);
            for (int j = 0; j < 3; j++) {
                list[i][j] = (ImageView) findViewById(idArray[i][j]);
            }
        }
        //Initializing here because common layout.
        ((TextView) findViewById(R.id.Player)).setText(R.string.t1_inital);
    }

    public void addMarker(View view) {
        allotmove((ImageView) view, humanImage, TicTacToeAI.HUMAN);
        if (checkIfOver(R.string.t1_hwin)) {
            return;
        }
        //Computer plays;
        int[] temp;
        temp = pc.move();
        allotmove((ImageView) findViewById(idArray[temp[0]][temp[1]]), compImage, TicTacToeAI.COMPUTER);
        if (checkIfOver(R.string.t1_cwin))
            ;
    }

    private boolean checkIfOver(int stringID) {
        if (hasWon()) {
            afterGameOver(stringID);
            return true;
        }
        if (isDraw()) {
            afterGameOver(R.string.draw);
            return true;
        }
        return false;
    }

    void allotmove(ImageView view, int imageselect, int player) {
        int[] move;
        view.setImageResource(imageSelector[imageselect]);
        view.setTag(imageSelector[imageselect]);
        view.setOnClickListener(null);
        id = view.getId();
        undo.add(id);
        move = getRowCol(id);
        board[move[0]][move[1]] = player;
    }

    void afterGameOver(int stringID) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                list[i][j].setOnClickListener(null);
            }
        }
        ((TextView)findViewById(R.id.Player)).setText(stringID);
        Button undo_button = (Button)findViewById(R.id.UNDO);
        undo_button.setClickable(false);
        undo_button.setTextColor(Color.GRAY);
    }

    public void restart(View view) {
        //Clearing Board, clearing image view,tags and resetting thier listener
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = DEALLOCATE;
                list[i][j].setImageResource(0);
                list[i][j].setTag(null);
                list[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addMarker(v);
                    }
                });
            }
        }
        ((TextView) findViewById(R.id.Player)).setText(R.string.t1_inital);
        findViewById(R.id.UNDO).setClickable(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ((Button)findViewById(R.id.UNDO)).setTextColor(getResources().getColor(R.color.colorAccent,null));
        }
        else
            ((Button)findViewById(R.id.UNDO)).setTextColor(getResources().getColor(R.color.colorAccent));
    }

    public void mainMenu(View view) {
        this.finish();
    }

    private boolean isDraw() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == DEALLOCATE)
                    return false;
            }
        }
        return true;
    }

    //helper method to return row,id;
    private int[] getRowCol(int id) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (id == idArray[row][col])
                    return new int[]{row, col};
            }
        }
        //if not matcing throw null pointer exception
        return null;
    }


    public void undo(View view) {
        if (!undo.isEmpty()) {
            int currentID;
            ImageView currentImage;
            int[] move;
            for (int i = 0; i < 2; i++) {
                currentID = undo.pop();
                currentImage = (ImageView) findViewById(currentID);
                currentImage.setImageResource(0);
                currentImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addMarker(v);
                    }
                });
                move = getRowCol(currentID);
                board[move[0]][move[1]] = DEALLOCATE;
            }
        }
    }
}
