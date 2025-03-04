package com.example.myapplication.entities;

import static com.example.myapplication.helpers.GameConstants.Animation.AMOUNT;

import android.graphics.PointF;

import com.example.myapplication.helpers.GameConstants;

public abstract class Character extends Entity{

    protected int aniTick, aniIndex;
    protected int faceDir = GameConstants.Face_Dir.DOWN;
    protected final GameCharacters gameCharType;



    public Character(PointF pos, GameCharacters gameCharType){

            super(pos, 1, 1);
            this.gameCharType = gameCharType;

    }
    protected void updateAnimation(){
        aniTick++;
        if(aniTick >= GameConstants.Animation.SPEED){
            aniTick = 0;
            aniIndex++;
            if(aniIndex >= AMOUNT){
                aniIndex = 0;
            }
        }
    }
    public void resetAnimation(){
        aniTick = 0;
        aniIndex = 0;
    }

    public int getFaceDir() {
        return faceDir;
    }

    public GameCharacters getGameCharType() {
        return gameCharType;
    }

    public int getAniIndex() {
        return aniIndex;
    }

    public void setFaceDir(int faceDir) {
        this.faceDir = faceDir;
    }
}
