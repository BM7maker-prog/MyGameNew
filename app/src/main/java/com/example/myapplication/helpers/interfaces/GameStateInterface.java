package com.example.myapplication.helpers.interfaces;

import android.graphics.Canvas;
import android.view.MotionEvent;

public interface GameStateInterface {
    void update(double delta);
    void render(Canvas c);
    void touchEvent(MotionEvent event);

}
