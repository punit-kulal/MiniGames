package com.example.android.minigames;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Stack;

import static com.example.android.minigames.TicTacToeAI.DEALLOCATE;
import static com.example.android.minigames.TicTacToeAI.SIZE;
import static com.example.android.minigames.TicTacToeAI.board;
import static com.example.android.minigames.TicTacToeAI.hasWon;

public class TicTacToe1P extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    int[] imageSelector = new int[2];
    ImageView[][] list = new ImageView[3][3];
    boolean winner = false;
    int[][] idArray;
    View.OnClickListener setter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            addMarker(v);
        }
    };
    int id, compImage = 1, humanImage = 0;
    Stack<Integer> undo = new Stack<>();
    TicTacToeAI pc = new TicTacToeAI();
    Spinner difficultySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tic_tac_toe1p);
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
        difficultySpinner = (Spinner) findViewById(R.id.difficulty);
        ArrayAdapter<CharSequence> difficultyAdapter = ArrayAdapter.createFromResource(this, R.array.difficulty_level,
                R.layout.spinner);
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultySpinner.setAdapter(difficultyAdapter);
        difficultySpinner.setOnItemSelectedListener(this);
    }

    public void addMarker(View view) {
        allotMove((ImageView) view, humanImage, TicTacToeAI.HUMAN);
        if (checkIfOver(R.string.t1_hwin)) {
            return;
        }
        findViewById(R.id.t1Restart).setEnabled(false);
        findViewById(R.id.UNDO).setEnabled(false);
        //Computer plays;
        ((TextView) findViewById(R.id.Status)).setText(R.string.wait);
        int[] temp;
        temp = pc.move();
        allotMove((ImageView) findViewById(idArray[temp[0]][temp[1]]), compImage, TicTacToeAI.COMPUTER);
//        if (checkIfOver(R.string.t1_cwin))
//            ;
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

    void allotMove(ImageView view, int imageselect, int player) {
        int[] move;
        if (imageselect == humanImage)
            view.setImageResource(imageSelector[imageselect]);
        else {
            MyHandler handler = new MyHandler();
            MyRunnable task = new MyRunnable(this, view, imageSelector[compImage]);
            if(difficultySpinner.getSelectedItemPosition()==1)
            handler.postDelayed(task, 1500);
            else
                handler.postDelayed(task,500);
        }
        view.setTag(imageSelector[imageselect]);
        view.setOnClickListener(null);
        id = view.getId();
        undo.add(id);
        move = getRowCol(id);
        board[move[0]][move[1]] = player;
        //shut down all listeners temporarily
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                list[i][j].setEnabled(false);
            }
        }
        difficultySpinner.setEnabled(false);
    }

    void afterGameOver(int stringID) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                list[i][j].setOnClickListener(null);
            }
        }
        ((TextView) findViewById(R.id.Status)).setText(stringID);
        Button undo_button = (Button) findViewById(R.id.UNDO);
        undo_button.setEnabled(false);
        difficultySpinner.setEnabled(true);
    }

    public void restart(View view) {
        //Clearing Board, clearing image view,tags and resetting thier listener
        pc.reset();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = DEALLOCATE;
                list[i][j].setImageResource(0);
                list[i][j].setTag(null);
                list[i][j].setOnClickListener(setter);
                list[i][j].setEnabled(true);
            }
        }
        ((TextView) findViewById(R.id.Status)).setText(R.string.t1_inital);
        findViewById(R.id.UNDO).setEnabled(true);
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
                currentImage.setOnClickListener(setter);
                move = getRowCol(currentID);
                board[move[0]][move[1]] = DEALLOCATE;
            }
        }
        if (undo.isEmpty())
            difficultySpinner.setEnabled(true);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        pc.setDifficulty(position + 1);
        restart(view);
    }
        @Override
        public void onNothingSelected (AdapterView < ? > parent){

        }

        private static class MyHandler extends Handler {

        }

        class MyRunnable implements Runnable {
            private final WeakReference<Activity> mActivity;
            private final ImageView image;
            private final int id;

            MyRunnable(Activity activity, ImageView image, int id) {
                mActivity = new WeakReference<>(activity);
                this.image = image;
                this.id = id;
            }

            @Override
            public void run() {
                Activity activity = mActivity.get();
                if (activity != null) {
                    ((TextView) activity.findViewById(R.id.Status)).setText(R.string.t1_inital);
                    image.setImageResource(id);
                    //reset disabled onclicklistener.
                    for (int i = 0; i < SIZE; i++) {
                        for (int j = 0; j < SIZE; j++) {
                            if (board[i][j] == DEALLOCATE) {
                                ((TicTacToe1P) activity).list[i][j].setEnabled(true);
                            }
                        }
                    }
                    findViewById(R.id.t1Restart).setEnabled(true);
                    findViewById(R.id.UNDO).setEnabled(true);
                    checkIfOver(R.string.t1_cwin);
                }
            }
        }
    }
