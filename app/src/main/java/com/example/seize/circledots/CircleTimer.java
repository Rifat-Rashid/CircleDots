package com.example.seize.circledots;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.animation.LinearInterpolator;

import java.util.concurrent.TimeUnit;

/**
 * Created by Seize on 4/7/2016.
 */
public class CircleTimer implements ObjectCoordinates {
    private int x, y, radius;
    private float sweepAngle, startAngle;
    private Paint mPaint;
    private long secondsTimer;


    //default constructor
    public CircleTimer(){

    }

    public CircleTimer(int x, int y, int radius, float startAngle, float sweepAngle){
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.startAngle = startAngle;
        this.sweepAngle = sweepAngle;
        setupPaintStack();
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

    public void setupPaintStack() {
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(Color.parseColor("#c0392b"));
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeWidth(this.radius / 10);
    }

    public long getSecondsTimer(){
        return this.secondsTimer;
    }

    public void setSecondsTimer(long secondsTimer){
        this.secondsTimer = secondsTimer;
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

    public void start(int secs){
        ValueAnimator mTimerAnimator = ValueAnimator.ofFloat(0f, 1f);
        mTimerAnimator.setDuration(TimeUnit.SECONDS.toMillis(secs));
        mTimerAnimator.setInterpolator(new LinearInterpolator());
        mTimerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
            @Override
            public void onAnimationUpdate(ValueAnimator animation){
                drawProgress((float) animation.getAnimatedValue());
            }
        });
        mTimerAnimator.start();
    }

    public void drawProgress(float progress){
        this.sweepAngle = -(360 - (360 * progress));
    }

}
