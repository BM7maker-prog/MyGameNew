package com.example.myapplication.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

public enum GameCharacters {

    PLAYER(R.drawable.player_spritesheet),
    LION(R.drawable.lion_spritesheet);

    private Bitmap spriteSheet;
    private BitmapFactory.Options options = new BitmapFactory.Options();
    private Bitmap[][] sprites = new Bitmap[7][4];


    GameCharacters(int resId) {
        options.inScaled = false;
        spriteSheet = BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(),resId,options );
        for (int j = 0; j < sprites.length; j++){
            for (int i = 0; i < sprites[j].length; i++){
                sprites[j][i] =getScaledBitmap(Bitmap.createBitmap(spriteSheet, 16*i,16*j,16,16)) ;
            }
        }
    }

    public Bitmap getSpriteSheet() {
        return spriteSheet;
    }
    public Bitmap getSprite(int yPos, int xPos){
        return sprites[yPos][xPos];
    }
    private Bitmap getScaledBitmap(Bitmap bitmap){
        return Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() * 7, bitmap.getHeight() * 7, false);
    }

}
