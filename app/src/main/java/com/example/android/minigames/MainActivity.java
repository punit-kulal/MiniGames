package com.example.android.minigames;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    int selected;
    Context mainContext;
    public void setSelected(int selected) {
        this.selected = selected;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Spinner spinner = (Spinner) findViewById(R.id.numPlayer);
        ArrayAdapter<CharSequence> spin = ArrayAdapter.createFromResource(this,R.array.number_player_,
                R.layout.spinner);
        spin.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(spin);
        spinner.setOnItemSelectedListener(this);
        mainContext = this;
    }

    public void TicTacToe(View view) {
        Intent game1 = null;
        System.out.println(selected);
        switch (selected){
            case 0 : game1 = new Intent(getApplicationContext(),TicTacToe1P.class);
                startActivity(game1);
                break;
            case 1: start2Pgame();
                break;
        }
    }

    private void start2Pgame() {
        {
            final AtomicBoolean b = new AtomicBoolean(Boolean.FALSE);
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setTitle("Play").setMessage("Do you want to play over Bluetooth.")
            .setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        mainContext.startActivity(new Intent(getApplicationContext(),TicTacToe2PB.class));
                        }
                    })
            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mainContext.startActivity(new Intent(getApplicationContext(),TicTacToe2P.class));
                    dialog.cancel();
                }
            });
            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
    }

    private void setUpBluetooth() {
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

    public void game2(View view) {
        Intent game2 = new Intent(getApplicationContext(),Reflex.class);
        startActivity(game2);
    }
}

