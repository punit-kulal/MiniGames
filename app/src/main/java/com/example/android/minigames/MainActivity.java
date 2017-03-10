package com.example.android.minigames;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    int selected;

    public void setSelected(int selected) {
        this.selected = selected;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Spinner spinner = (Spinner) findViewById(R.id.numPlayer);
        ArrayAdapter<CharSequence> spin = ArrayAdapter.createFromResource(this,R.array.number_player_,android.R.layout.simple_spinner_item);
        spin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spin);
        spinner.setOnItemSelectedListener(this);
    }

    public void TicTacToe(View view) {
        Intent game1 = null;
        System.out.println(selected);
        switch (selected){
            case 0 : game1 = new Intent(getApplicationContext(),TicTacToe1P.class);
                break;
            case 1: game1 = new Intent(getApplicationContext(),TicTacToe2P.class);
                break;
        }
        startActivity(game1);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        findViewById(R.id.game1).setClickable(true);
        setSelected(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        findViewById(R.id.game1).setClickable(false);
        ((Button)findViewById(R.id.game1)).setTextColor(Color.GRAY);
    }
}

