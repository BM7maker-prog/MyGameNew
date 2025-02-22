package com.example.myapplication.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.helpers.GameConstants;
import com.example.myapplication.helpers.interfaces.BitmapMethods;

public enum GameCharacters implements BitmapMethods {

    PLAYER(R.drawable.player_spritesheet),
    LION(R.drawable.lion_spritesheet);

    private Bitmap spriteSheet;

    private Bitmap[][] sprites = new Bitmap[7][4];


    GameCharacters(int resId) {
        options.inScaled = false;
        spriteSheet = BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(),resId,options );
        for (int j = 0; j < sprites.length; j++){
            for (int i = 0; i < sprites[j].length; i++){
                sprites[j][i] =getScaledBitmap(Bitmap.createBitmap(spriteSheet,
                        GameConstants.Sprite.DEFAULT_SIZE *i,
                        GameConstants.Sprite.DEFAULT_SIZE*j,
                        GameConstants.Sprite.DEFAULT_SIZE,
                        GameConstants.Sprite.DEFAULT_SIZE)) ;
            }
        }
    }

    public Bitmap getSpriteSheet() {
        return spriteSheet;
    }
    public Bitmap getSprite(int yPos, int xPos){
        return sprites[yPos][xPos];
    }


}
