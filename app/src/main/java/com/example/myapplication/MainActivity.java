package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    private static Context gameContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameContext = this;  // Add this line to initialize the context
        EdgeToEdge.enable(this);
        setContentView(new GamePanel(this));
    }

    public static Context getGameContext() {
        return gameContext;
    }
}
