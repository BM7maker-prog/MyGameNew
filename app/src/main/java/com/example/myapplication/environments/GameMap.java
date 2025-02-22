package com.example.myapplication.environments;

import android.graphics.Canvas;

import com.example.myapplication.helpers.GameConstants;

public class GameMap {

    private int[][] spriteIds;

    public GameMap(int[][] spriteIds){
        this.spriteIds = spriteIds;

    }
    public void draw(Canvas c){
        for(int j = 0; j < spriteIds.length; j++){
            for(int i = 0; i < spriteIds[j].length; i++){
                c.drawBitmap(Floor.OUTSIDE.getSprite(spriteIds[j][i]),
                        i * GameConstants.Sprite.SIZE,
                        j * GameConstants.Sprite.SIZE,
                        null);
            }
        }
    }
}


