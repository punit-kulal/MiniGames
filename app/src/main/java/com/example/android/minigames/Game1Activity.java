package com.example.android.minigames;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class Game1Activity extends AppCompatActivity {

    boolean marker = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game1);
    }

    public void addMarker(View view) {
        if(marker)
            ((ImageView)view).setImageResource(R.mipmap.x);
        else
            ((ImageView)view).setImageResource(R.mipmap.gameo);
        marker = !marker;
        view.setOnClickListener(null);
    }

    // private voidcheckIfOver()
}

