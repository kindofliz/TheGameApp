package com.example.thegameapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class FlyingFishView extends View {

    private Bitmap fish[] = new Bitmap[2];
    private int fishX = 10;
    private int fishY;
    private int fishSpeed;

    private int canvasWidth;
    private int canvasHeight;

    private int yellowX;
    private int yellowY;
    private int yellowSpeed = 10;
    private Paint yellowPaint = new Paint();

    private int greenX;
    private int greenY;
    private int greenSpeed = 12;
    private Paint greenPaint = new Paint();

    private int redX;
    private int redY;
    private int redSpeed = 14;
    private Paint redPaint = new Paint();

    private int score;
    private int lifeCounterOfFish;

    private boolean touch = false;

    private Bitmap backgroundImage;
    private Paint scorePaint = new Paint();
    private Bitmap life[] = new Bitmap[2];

    public FlyingFishView(Context context) {
        super(context);

        //connect the images to the code
        fish[0] = BitmapFactory.decodeResource(getResources(), R.drawable.fish1);
        fish[1] = BitmapFactory.decodeResource(getResources(), R.drawable.fish2);

        backgroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.background);

        yellowPaint.setColor(Color.YELLOW);
        yellowPaint.setAntiAlias(false);

        greenPaint.setColor(Color.GREEN);
        greenPaint.setAntiAlias(false);

        redPaint.setColor(Color.RED);
        redPaint.setAntiAlias(false);

        scorePaint.setColor(Color.WHITE);
        scorePaint.setTextSize(60);
        scorePaint.setTypeface(Typeface.DEFAULT_BOLD);
        scorePaint.setAntiAlias(true);

        life[0] = BitmapFactory.decodeResource(getResources(), R.drawable.hearts);
        life[1] = BitmapFactory.decodeResource(getResources(),R.drawable.heart_grey);

        fishY = 550;
        score = 0;
        lifeCounterOfFish = 3;

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //display the images on screen/canvas

        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();

        //DRAW BACKGROUND IMAGE
        canvas.drawBitmap(backgroundImage, 0, 0, null);

        //PUT THE FISH ON THE SCREEN AND MAKE IT FLY UP/DOWN
        int minFishY = fish[0].getHeight();
        int maxFishY = canvasHeight - fish[0].getHeight() * 3;
        fishY = fishY + fishSpeed;

        if (fishY < minFishY) {
            fishY = minFishY;
        }
        if (fishY > maxFishY) {
            fishY = maxFishY;
        }
        fishSpeed = fishSpeed + 2;

        if (touch) {
            canvas.drawBitmap(fish[1], fishX, fishY, null);
            touch = false;
        } else {
            canvas.drawBitmap(fish[0], fishX, fishY, null);
        }

        //YELLOW BALL
        yellowX = yellowX - yellowSpeed;

        if (hitBallChecker(yellowX, yellowY)) {
            score = score + 10;
            yellowX = -100;
        }

        if (yellowX < 0) {
            yellowX = canvasWidth + 21;
            yellowY = (int) Math.floor(Math.random() * (maxFishY - minFishY)) + minFishY;
        }
        canvas.drawCircle(yellowX, yellowY, 15, yellowPaint);

        //GREEN BALL
        greenX = greenX - greenSpeed;

        if (hitBallChecker(greenX, greenY)) {
            score = score + 20;
            greenX = -100;
        }

        if (greenX < 0) {
            greenX = canvasWidth + 21;
            greenY = (int) Math.floor(Math.random() * (maxFishY - minFishY)) + minFishY;
        }
        canvas.drawCircle(greenX, greenY, 15, greenPaint);

        //RED BALL
        redX = redX - redSpeed;

        if (hitBallChecker(redX, redY)) {
            redX = -100;
            lifeCounterOfFish--;

            if (lifeCounterOfFish == 0) {
                Toast.makeText(getContext(), "Game Over", Toast.LENGTH_SHORT).show();

                Intent gameOverIntent = new Intent(getContext(), GameOverActivity.class);
                gameOverIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                gameOverIntent.putExtra("SCORE", score);
                getContext().startActivity(gameOverIntent);
            }
        }

        if (redX < 0) {
            redX = canvasWidth + 21;
            redY = (int) Math.floor(Math.random() * (maxFishY - minFishY)) + minFishY;
        }
        canvas.drawCircle(redX, redY, 22, redPaint);

        //DISPLAY THE SCORE
        canvas.drawText("Score : " + score, 20, 100, scorePaint);

        //DISPLAY THE LIVES LEFT
        for (int i = 0; i < 3; i++) {
            int x = (int) (680 + life[0].getWidth() * 1.5 * i);
            int y = 30;

            //when all lives are still available
            if (i < lifeCounterOfFish) {
                canvas.drawBitmap(life[0], x, y, null);
            //if the fish eats red ball, a life turns grey
            } else {
                canvas.drawBitmap(life[1], x, y, null);
            }
        }

    }


    public boolean hitBallChecker(int x, int y) {
        if (fishX < x && x < (fishX + fish[0].getWidth()) && fishY < y && y < (fishY + fish[0].getHeight())) {
            return true;
        }
        return false;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
       if (event.getAction() == MotionEvent.ACTION_DOWN) {
           touch = true;
           fishSpeed = -25;
       }
       return true;
    }
}
