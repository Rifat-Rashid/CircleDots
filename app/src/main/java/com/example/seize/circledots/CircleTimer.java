package com.example.seize.circledots;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.animation.LinearInterpolator;

import java.util.concurrent.TimeUnit;

/**
 * Created by Seize on 4/7/2016.
 */
public class CircleTimer implements ObjectCoordinates {
    private float x, y;
    private int radius;
    private float sweepAngle, startAngle;
    private Paint mPaint;
    private Paint sPaint;
    private long secondsTimer;
    private Context context;
    private int seconds;
    private Typeface FONT_PROXIMA_NOVA_LIGHT;
    private RectF rectF;
    private String indicator;
    private int secondsGoneBy = 0;
    private Paint textPaint;
    private boolean isTimerRunning;
    private int[] colorArray;
    public ValueAnimator mTimerAnimator;

    //default constructor
    public CircleTimer() {

    }

    public CircleTimer(int x, int y, int radius, float startAngle, float sweepAngle, Context context) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.startAngle = startAngle;
        this.sweepAngle = sweepAngle;
        this.context = context;
        this.FONT_PROXIMA_NOVA_LIGHT = Typeface.createFromAsset(this.context.getAssets(), "fonts/ProximaNova-Regular.otf");
        this.rectF = new RectF(this.x - this.radius, this.y - this.radius, this.x + this.radius, this.y + this.radius);
        setupPaintStack();
    }

    public CircleTimer(int x, int y, int radius, float startAngle, float sweepAngle, Paint mPaint) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.startAngle = startAngle;
        this.sweepAngle = sweepAngle;
        this.mPaint = mPaint;
    }

    public void Draw(Canvas canvas) {
        //api 21. Worry about this later!
        canvas.drawText(indicator, rectF.left + this.radius - (textPaint.measureText(indicator, 0, indicator.length())) / 2, rectF.top + this.radius + (textPaint.descent() - textPaint.ascent()) / 4, textPaint);
        canvas.drawArc(this.x - this.radius, this.y - this.radius, this.x + radius, this.y + radius, 270, 360, false, this.sPaint);
        canvas.drawArc(this.x - this.radius, this.y - this.radius, this.x + radius, this.y + radius, this.startAngle, this.sweepAngle, false, this.mPaint);
    }

    public void setupPaintStack() {
        Resources res = context.getResources();
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(res.getColor(android.R.color.holo_orange_dark));
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeWidth(this.radius / 8);

        this.textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.textPaint.setAntiAlias(true);
        this.textPaint.setColor(res.getColor(android.R.color.holo_orange_dark));
        this.textPaint.setTypeface(FONT_PROXIMA_NOVA_LIGHT);
        this.textPaint.setTextSize(this.radius * .8f);

        this.sPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.sPaint.setAntiAlias(true);
        this.sPaint.setColor(res.getColor(android.R.color.holo_orange_light));
        this.sPaint.setStyle(Paint.Style.STROKE);
        this.sPaint.setStrokeWidth(this.radius / 8);

        colorArray = new int[]{res.getColor(android.R.color.holo_orange_dark), res.getColor(android.R.color.holo_orange_light), Color.parseColor("#e74c3c"), Color.parseColor("#1F9EA3")};
    }

    public void changeColor(int index){
        try {
            this.mPaint.setColor(colorArray[index]);
            this.sPaint.setColor(colorArray[index + 1]);
            this.textPaint.setColor(colorArray[index]);
        }catch (IndexOutOfBoundsException oob){
            oob.printStackTrace();
        }
    }

    public long getSecondsTimer() {
        return this.secondsTimer;
    }

    public void setSecondsTimer(long secondsTimer) {
        this.secondsTimer = secondsTimer;
    }

    @Override
    public float getX() {
        return this.x;
    }

    @Override
    public float getY() {
        return this.y;
    }

    @Override
    public void setX(float x) {
        this.x = x;
    }

    @Override
    public void setY(float y) {
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

    public boolean getIsRunning() {
        return this.isTimerRunning;
    }

    public void setIsRunning(boolean isTimerRunning) {
        this.isTimerRunning = isTimerRunning;
    }

    public void start(int secs) {
        this.sweepAngle = 360;
        this.seconds = secs;
        this.isTimerRunning = true;
        mTimerAnimator = ValueAnimator.ofFloat(0f, 1f);
        mTimerAnimator.setDuration(TimeUnit.SECONDS.toMillis(secs));
        mTimerAnimator.setInterpolator(new LinearInterpolator());
        mTimerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //System.out.println(animation.getAnimatedValue());
                drawProgress((float) animation.getAnimatedValue());
            }
        });
        mTimerAnimator.start();
    }

    public void drawProgress(float progress) {
        this.sweepAngle = -(360 - (360 * progress));
        this.secondsGoneBy = (int) (seconds - (seconds * progress));
        indicator = String.valueOf(secondsGoneBy);
        if (this.secondsGoneBy <= 0) {
            this.isTimerRunning = false;
        }
    }
}
