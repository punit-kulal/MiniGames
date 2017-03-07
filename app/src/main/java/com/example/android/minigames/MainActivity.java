package com.example.android.minigames;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void TicTacToe(View view) {
        Intent game1 = new Intent(getApplicationContext(),Game1Activity.class);
        startActivity(game1);
    }
}

