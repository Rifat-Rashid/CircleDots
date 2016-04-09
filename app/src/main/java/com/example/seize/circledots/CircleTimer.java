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
    private int x, y, radius;
    private float sweepAngle, startAngle;
    private Paint mPaint;
    private Paint sPaint;
    private long secondsTimer;
    private Context context;
    private int seconds;
    private Typeface FONT_PROXIMA_NOVA_LIGHT;
    private Rect rect;
    private RectF rectF;
    private String indicator;
    private int secondsGoneBy = 0;
    private Paint textPaint;

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
        this.rect = new Rect(this.x - this.radius, this.y - this.radius, this.x + this.radius, this.y + this.radius);
        this.rectF = new RectF(this.rect);
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
        indicator = String.valueOf(secondsGoneBy);
        canvas.drawText(indicator, rectF.left + this.radius - (textPaint.measureText(indicator, 0, indicator.length())) / 2, rectF.top + this.radius + (textPaint.descent() - textPaint.ascent()) / 2, textPaint);
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
    }

    public long getSecondsTimer() {
        return this.secondsTimer;
    }

    public void setSecondsTimer(long secondsTimer) {
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

    public void start(int secs) {
        this.seconds = secs;
        ValueAnimator mTimerAnimator = ValueAnimator.ofFloat(0f, 1f);
        mTimerAnimator.setDuration(TimeUnit.SECONDS.toMillis(secs));
        mTimerAnimator.setInterpolator(new LinearInterpolator());
        mTimerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                System.out.println(animation.getAnimatedValue());
                drawProgress((float) animation.getAnimatedValue());
            }
        });
        mTimerAnimator.start();
    }

    public void drawProgress(float progress) {
        this.sweepAngle = -(360 - (360 * progress));
        this.secondsGoneBy = (int) (seconds - (seconds * progress));
    }

}
