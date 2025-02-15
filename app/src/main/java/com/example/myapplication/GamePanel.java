package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.example.myapplication.entities.GameCharacters;
import com.example.myapplication.helpers.GameConstants;

import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private Paint redPaint = new Paint();
    private SurfaceHolder holder;
    private float x,y;
    private Random rand = new Random();
    private ArrayList<PointF> lions = new ArrayList<>();
    private int playerAniIndexY, palyerFaceDir = GameConstants.Face_Dir.RIGHT;
    private int aniTick;
    private int aniSpeed = 10;
    private PointF lionPos;
    private Random random = new Random();
    private GameLoop gameLoop;
    private int lionDirection = GameConstants.Face_Dir.DOWN;
    private long lastDirChange = System.currentTimeMillis();

    public GamePanel(Context context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
        redPaint.setColor(Color.RED);
        gameLoop = new GameLoop(this);
        lionPos = new PointF(rand.nextInt(1080), rand.nextInt(1920));
//
//        for (int i = 0;i < 50;i++){
//            lions.add(new PointF(rand.nextInt(1080), rand.nextInt(1920)));
//
//        }
    }

    public void render(){
        Canvas c = holder.lockCanvas();
        c.drawColor(Color.BLACK);
//        c.drawBitmap(GameCharacters.PLAYER.getSpriteSheet(),500,500,null);
        c.drawBitmap(GameCharacters.PLAYER.getSprite(playerAniIndexY,palyerFaceDir),x, y,null);
        c.drawBitmap(GameCharacters.LION.getSprite(playerAniIndexY,lionDirection),lionPos.x,lionPos.y,null);

        holder.unlockCanvasAndPost(c);
//        for (PointF pos : lions)
//            c.drawBitmap(GameCharacters.LION.getSprite(0,0), pos.x, pos.y, null);
    }
    public void update(double delta){
        if(System.currentTimeMillis() - lastDirChange >= 3000){
            lionDirection = rand.nextInt(4);
            lastDirChange = System.currentTimeMillis();
        }
        switch (lionDirection){
            case GameConstants.Face_Dir.DOWN:
                lionPos.y += delta * 300;
                if (lionPos.y >= 1980)
                    lionDirection = GameConstants.Face_Dir.UP;
                break;
            case GameConstants.Face_Dir.UP:
                lionPos.y -= delta * 300;
                if (lionPos.y <= 0)
                    lionDirection = GameConstants.Face_Dir.DOWN;
                break;
            case GameConstants.Face_Dir.RIGHT:
                lionPos.x += delta * 300;
                if (lionPos.x >= 1080)
                    lionDirection = GameConstants.Face_Dir.LEFT;
                break;
            case GameConstants.Face_Dir.LEFT:
                lionPos.x -= delta * 300;
                if (lionPos.x <= 0)
                    lionDirection = GameConstants.Face_Dir.RIGHT;
                break;

        }

        updateAnimation();
    }

    private void updateAnimation(){
        aniTick++;
        if(aniTick >= aniSpeed){
            aniTick = 0;
            playerAniIndexY++;
            if(playerAniIndexY >= 4){
                playerAniIndexY = 0;
            }
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){

            float newX = getX();
            float newY = getY();
            float xDif = Math.abs(newX - x);
            float yDif = Math.abs(newY - y);
            x = event.getX();
            y = event.getY();
            if(xDif > yDif ){
                if (newX > x){
                    palyerFaceDir = GameConstants.Face_Dir.RIGHT;

                }else {
                    palyerFaceDir = GameConstants.Face_Dir.LEFT;
                }
            }else {
                if (newY > y){
                    palyerFaceDir = GameConstants.Face_Dir.DOWN;
                }else{
                    palyerFaceDir = GameConstants.Face_Dir.UP;
                }
            }
        }
        return true;
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        gameLoop.startGameLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }



}
