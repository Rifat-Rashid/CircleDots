package com.example.seize.circledots;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;

import java.util.Random;

/**
 * Created by Seize on 3/30/2016.
 */
public class Dot implements ColorArrayPallete, ObjectCoordinates {
    private int selectedColor;
    private int x, y, radius;
    private Paint mPaint;
    private Context context;

    //default constructor
    public Dot(){

    }

    public Dot(int x, int y, int radius, Context context){
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.context = context;
        setUpPaintStack();
    }

    public void setUpPaintStack(){
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(generateColor());
        this.mPaint.setStyle(Paint.Style.FILL);
    }

    public void Draw(Canvas canvas){
        canvas.drawCircle(getX(), getY(), this.radius, this.mPaint);
    }

    @Override
    public int getColor() {
        return this.mPaint.getColor();
    }

    @Override
    public void setColor(int color) {
        this.mPaint.setColor(color);
    }

    @Override
    public int generateColor() {
        TypedArray mTypedArray = this.context.getResources().obtainTypedArray(R.array.colors_Array);
        Random r = new Random();
        return mTypedArray.getColor((r.nextInt(mTypedArray.length())), 0);
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

    public int getRadius(){
        return this.radius;
    }
}
