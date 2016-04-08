package com.example.seize.circledots;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Seize on 4/7/2016.
 */
public class CircleTimer implements ObjectCoordinates {
    private int x, y, radius;
    private float sweepAngle, startAngle;
    private Paint mPaint;


    //default constructor
    public CircleTimer(){

    }

    public CircleTimer(int x, int y, int radius, float startAngle, float sweepAngle){

    }

    public CircleTimer(int x, int y, int radius, float startAngle, float sweepAngle, Paint mPaint){
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.startAngle = startAngle;
        this.sweepAngle = sweepAngle;
        this.mPaint = mPaint;
    }

    public void Draw(Canvas canvas){
        //api 21. Worry about this later!
        canvas.drawArc(this.x - this.radius, this.y - this.radius, this.x + radius, this.y + radius, this.startAngle, this.sweepAngle, false, this.mPaint);
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

     @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public float getSweepAngle() {
        return sweepAngle;
    }

    public void setSweepAngle(float sweepAngle) {
        this.sweepAngle = sweepAngle;
    }

    public float getStartAngle() {
        return startAngle;
    }

    public void setStartAngle(float startAngle) {
        this.startAngle = startAngle;
    }

    public void startTimer(){
        Runnable r = new Runnable() {
            @Override
            public void run() {

            }
        };
        new Thread(r).start();
    }




}
