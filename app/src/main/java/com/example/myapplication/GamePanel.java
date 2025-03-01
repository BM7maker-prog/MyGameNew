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
import com.example.myapplication.environments.MapManager;
import com.example.myapplication.helpers.GameConstants;
import com.example.myapplication.inputs.TouchEvents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private Paint redPaint = new Paint();
    private SurfaceHolder holder;
    private float playerX = GAME_WIDTH / 2, playerY = GAME_HEIGHT / 2;
    private float cameraX, cameraY;
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

    private MapManager mapManager;

    public GamePanel(Context context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
        redPaint.setColor(Color.RED);
        touchEvents = new TouchEvents(this);
        gameLoop = new GameLoop(this);
        lionPos = new PointF(rand.nextInt(GAME_WIDTH), rand.nextInt(GAME_HEIGHT));
        mapManager = new MapManager();

        int[][] testArrayWithIds = new int[10][10];


    }

    public void render() {

        Canvas c = holder.lockCanvas();
        if (c != null) {
            c.drawColor(Color.BLACK);
            mapManager.draw(c);
            touchEvents.draw(c);
            c.drawBitmap(GameCharacters.PLAYER.getSprite(playerAniIndexY, palyerFaceDir), playerX, playerY, null);
            c.drawBitmap(GameCharacters.LION.getSprite(playerAniIndexY, lionDirection), lionPos.x + cameraX, lionPos.y + cameraY, null);
            holder.unlockCanvasAndPost(c);


        }
    }

    public void update(double delta){

        updatePlayerMove(delta);
        mapManager.setCameraValues(cameraX, cameraY);
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

        int pWidth = GameConstants.Sprite.SIZE;
        int pHeight = GameConstants.Sprite.SIZE;
        if (xSpeed <= 0)
            pWidth = 0;
        if (ySpeed <= 0)
            pHeight = 0;




        float deltaX = xSpeed * baseSpeed * -1;
        float deltaY =  ySpeed * baseSpeed * -1;

        if (mapManager.canMoveHere(playerX + cameraX * -1 + deltaX * -1 + pWidth, playerY + cameraY * -1 + deltaY * -1 + pHeight)){
            cameraX += deltaX;
            cameraY += deltaY;
        }

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
