package com.example.myapplication;

import static com.example.myapplication.MainActivity.GAME_HEIGHT;
import static com.example.myapplication.MainActivity.GAME_WIDTH;

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
import com.example.myapplication.environments.GameMap;
import com.example.myapplication.helpers.GameConstants;
import com.example.myapplication.inputs.TouchEvents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private Paint redPaint = new Paint();
    private SurfaceHolder holder;
    private float x,y;
    private boolean movePlayer;
    private PointF lastTouchDiff;
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
    private TouchEvents touchEvents;

    // Testing Map
    private GameMap testMap;

    public GamePanel(Context context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
        redPaint.setColor(Color.RED);
        touchEvents = new TouchEvents(this);
        gameLoop = new GameLoop(this);
        lionPos = new PointF(rand.nextInt(GAME_WIDTH), rand.nextInt(GAME_HEIGHT));


        int[][] testArrayWithIds = new int[10][10];


        int[][] spriteIds = {

        {454, 276, 275, 275, 190, 275, 275, 279, 275, 275, 275, 297, 110, 0, 1, 1, 1, 2, 110, 132},
        {454, 275, 169, 232, 238, 275, 275, 275, 276, 275, 275, 297, 110, 22, 89, 23, 23, 24, 110, 132},
        {454, 275, 190, 276, 275, 275, 279, 275, 275, 275, 279, 297, 110, 22, 23, 23, 23, 24, 110, 132},
        {454, 275, 190, 279, 275, 275, 169, 233, 275, 275, 275, 297, 110, 22, 23, 23, 23, 24, 110, 132},
        {454, 275, 190, 276, 277, 275, 190, 279, 279, 279, 275, 297, 110, 22, 23, 88, 23, 24, 110, 132},
        {454, 275, 235, 232, 232, 232, 260, 279, 276, 279, 275, 297, 110, 22, 23, 89, 23, 24, 110, 132},
        {454, 275, 275, 275, 275, 275, 190, 279, 279, 279, 275, 297, 110, 22, 23, 23, 23, 24, 110, 132},
        {454, 277, 275, 275, 279, 275, 257, 232, 232, 232, 238, 297, 110, 22, 88, 23, 23, 24, 110, 132},
        {454, 275, 275, 275, 275, 275, 190, 279, 275, 275, 275, 297, 110, 22, 23, 23, 88, 24, 110, 132},
        {454, 275, 275, 275, 275, 275, 190, 279, 279, 279, 279, 297, 110, 22, 23, 23, 23, 24, 110, 132},
        {454, 169, 232, 232, 232, 232, 239, 232, 232, 232, 172, 297, 110, 22, 23, 89, 23, 24, 110, 132},
        {454, 190, 279, 275, 275, 275, 275, 275, 275, 275, 190, 297, 110, 44, 45, 45, 45, 46, 110, 132}
        };
        testMap = new GameMap(spriteIds);

    }

    public void render() {

        Canvas c = holder.lockCanvas();
        if (c != null) {
            c.drawColor(Color.BLACK);
            testMap.draw(c);
            touchEvents.draw(c);
            c.drawBitmap(GameCharacters.PLAYER.getSprite(playerAniIndexY, palyerFaceDir), x, y, null);
            c.drawBitmap(GameCharacters.LION.getSprite(playerAniIndexY, lionDirection), lionPos.x, lionPos.y, null);
            holder.unlockCanvasAndPost(c);


        }
    }

    public void update(double delta){
        if(System.currentTimeMillis() - lastDirChange >= 3000){
            lionDirection = rand.nextInt(4);
            lastDirChange = System.currentTimeMillis();
        }
        switch (lionDirection){
            case GameConstants.Face_Dir.DOWN:
                lionPos.y += delta * 300;
                if (lionPos.y >= GAME_HEIGHT)
                    lionDirection = GameConstants.Face_Dir.UP;
                break;
            case GameConstants.Face_Dir.UP:
                lionPos.y -= delta * 300;
                if (lionPos.y <= 0)
                    lionDirection = GameConstants.Face_Dir.DOWN;
                break;
            case GameConstants.Face_Dir.RIGHT:
                lionPos.x += delta * 300;
                if (lionPos.x >= GAME_WIDTH)
                    lionDirection = GameConstants.Face_Dir.LEFT;
                break;
            case GameConstants.Face_Dir.LEFT:
                lionPos.x -= delta * 300;
                if (lionPos.x <= 0)
                    lionDirection = GameConstants.Face_Dir.RIGHT;
                break;

        }
        updatePlayerMove(delta);
        
        updateAnimation();
    }

    private void updatePlayerMove(double delta) {
        if (!movePlayer){
            return;
        }
        float baseSpeed = (float) delta * 300;
        float ratio = Math.abs(lastTouchDiff.y)/Math.abs(lastTouchDiff.x);
        double angle = Math.atan(ratio);

        float xSpeed = (float) Math.cos(angle);
        float ySpeed = (float)Math.sin(angle);
//        System.out.println("Angle" + Math.toDegrees(angle));
//        System.out.println("xSpeed" + xSpeed + " " + "ySpeed" + ySpeed);

        if (xSpeed > ySpeed){
            if (lastTouchDiff.x > 0){
                palyerFaceDir = GameConstants.Face_Dir.RIGHT;
            }else {
                palyerFaceDir = GameConstants.Face_Dir.LEFT;
            }
        }else {
            if (lastTouchDiff.y > 0){
                palyerFaceDir = GameConstants.Face_Dir.DOWN;

            }else {
                palyerFaceDir = GameConstants.Face_Dir.UP;
            }
        }

        if(lastTouchDiff.x < 0){
            xSpeed *= -1;
        }
        if(lastTouchDiff.y < 0){
            ySpeed *= -1;
        }
        x += xSpeed * baseSpeed;
        y += ySpeed * baseSpeed;
    }

    private void updateAnimation(){
        if (!movePlayer)
            return;
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
        return touchEvents.touchEvent(event);

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
    public void setPlayerMoveTrue(PointF lastTouchDiff){
        movePlayer = true;
        this.lastTouchDiff = lastTouchDiff;
    }
    public void setPlayerMoveFalse(){
        movePlayer = false;
        resetAnimation();

    }
    public void resetAnimation(){
        aniTick = 0;
        playerAniIndexY = 0;
    }

}
