package com.example.android.minigames;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Stack;

public class TicTacToe2P extends AppCompatActivity {
    ImageView[] list = new ImageView[10];
    boolean winner = false;
    int[] idArray;
    boolean marker = true;
    int id;
    Stack<Integer> undo = new Stack<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tic_tac_toe);
        idArray = new int[]{0, R.id.one, R.id.two, R.id.three, R.id.four, R.id.five, R.id.six, R.id.seven, R.id.eight, R.id.nine};
    }


    public void addMarker(View view) {
        if (marker) {
            ((ImageView) view).setImageResource(R.mipmap.x);
            view.setTag(R.mipmap.x);
            ((TextView) findViewById(R.id.Player)).setText(R.string.g1_t2_p2play);
        } else {
            ((ImageView) view).setImageResource(R.mipmap.gameo);
            view.setTag(R.mipmap.gameo);
            ((TextView) findViewById(R.id.Player)).setText(R.string.t2_p1play);
        }
        marker = !marker;
        view.setOnClickListener(null);
        id = view.getId();
        undo.push(id);
        if (checkIfOver(id)) {
            Button undo_button = (Button)findViewById(R.id.UNDO);
            undo_button.setClickable(false);
            undo_button.setTextColor(Color.GRAY);
            for (int i = 1; i <= 9; i++) {
                list[i].setOnClickListener(null);
            }
            if (marker)
                ((TextView) findViewById(R.id.Player)).setText(R.string.t2_p2won);
            else
                ((TextView) findViewById(R.id.Player)).setText(R.string.t2_p1won);
            return;
        }
        if (draw()) {
            ((TextView) findViewById(R.id.Player)).setText(R.string.draw);
        }
    }

    private boolean draw() {
        for (int i = 1; i <= 9; i++) {
            if (list[i].getTag() == null) {
                return false;
            }
        }
        return true;
    }

    private boolean checkIfOver(int id) {

        for (int i = 1; i < 10; i++) {
            list[i] = (ImageView) findViewById(idArray[i]);
        }
        //Check for 2nd 4th and 6th position
        for (int i = 2; i < 9; i += 2) {
            if (id == idArray[i]) {
                winner = checkHorizontal(id);
                winner = winner || checkVertical(id);
                return winner;
            }
        }

        for (int i = 1; i <= 9; i += 2) {
            if (id == idArray[i]) {
                winner = checkHorizontal(id);
                winner = winner || checkVertical(id);
                winner = winner || checkDiagonal(id);
                return winner;
            }
        }
        return winner;
    }

    private boolean checkVertical(int id) {
        boolean set;
        for (int i = 1; i <= 3; i++) {
            set = true;
            for (int j = i; j <= 9; j += 3) {
                if (list[j].getTag() == null)
                    set = false;
            }
            if (set && (id == idArray[i] || id == idArray[i + 3] || id == idArray[i + 6])) {
                if (list[i].getTag().equals(list[i + 3].getTag()) && list[i].getTag().
                        equals(list[i + 6].getTag())) {
                    return true;
                }
            }
        }
        return false;
    }


    private boolean checkHorizontal(int id) {
        boolean set;
        for (int i = 1; i <= 9; i += 3) {
            set = true;
            for (int j = i; j < i + 3; j++) {
                if (list[j].getTag() == null)
                    set = false;
            }
            if (set && (id == idArray[i] || id == idArray[i + 1] || id == idArray[i + 2])) {
                if (list[i].getTag().equals(list[i + 1].getTag()) && list[i].getTag().
                        equals(list[i + 2].getTag())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkDiagonal(int id) {
        boolean set = true;
        for (int i = 1; i <= 9; i += 4) {
            if (list[i].getTag() == null)
                set = false;
        }
        if (set && (id == idArray[1] || id == idArray[5] || id == idArray[9])) {
            if (list[1].getTag().equals(list[5].getTag()) && list[1].getTag().
                    equals(list[9].getTag())) {
                return true;
            }
        }
        set = true;
        for (int i = 3; i <= 9; i += 2) {
            if (list[i].getTag() == null)
                set = false;
        }
        if (set && (id == idArray[3] || id == idArray[5] || id == idArray[7])) {
            if (list[3].getTag().equals(list[5].getTag()) && list[3].getTag().
                    equals(list[7].getTag())) {
                return true;
            }
        }
        return false;
    }


    public void restart(View view) {
        undo.clear();
        for (int i = 1; i < 10; i++) {
            ImageView current = list[i];
            current.setTag(null);
            current.setImageResource(0);
            marker = !marker;
            current.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addMarker(v);
                }
            });
        }
        findViewById(R.id.UNDO).setClickable(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ((Button)findViewById(R.id.UNDO)).setTextColor(getResources().getColor(R.color.colorAccent,null));
        }
        else
            ((Button)findViewById(R.id.UNDO)).setTextColor(getResources().getColor(R.color.colorAccent));
        marker = true;
        winner = false;
        ((TextView)findViewById(R.id.Player)).setText(R.string.player_1_can_play);
    }

    public void undo(View view) {
        if (!undo.isEmpty()) {
            int undo_id = undo.pop();
            ImageView current = ((ImageView) findViewById(undo_id));
            current.setTag(null);
            current.setImageResource(0);
            marker = !marker;
            if (marker) {
                ((TextView) findViewById(R.id.Player)).setText(R.string.g1_t2_p2play);
            } else
                ((TextView) findViewById(R.id.Player)).setText(R.string.player_1_can_play);
            current.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addMarker(v);
                }
            });
        }
    }

    public void mainMenu(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}

