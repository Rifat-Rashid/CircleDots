package com.example.seize.circledots;

/**
 * Created by Seize on 4/7/2016.
 */
public class CircleTimer implements ObjectCoordinates {
    private int x, y, radius;
    private float sweepAngle, startAngle;



    //default constructor
    public CircleTimer(){

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
