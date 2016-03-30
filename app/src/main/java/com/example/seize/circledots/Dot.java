package com.example.seize.circledots;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

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
        this.mPaint.setStyle(Paint.Style.FILL);
        this.mPaint.setColor(generateColor());
    }

    public void Draw(Canvas canvas){
        canvas.drawCircle(getX(), getY(), this.radius, this.mPaint);
    }

    @Override
    public int getColor() {
        return this.selectedColor;
    }

    @Override
    public void setColor(int color) {
        this.selectedColor = color;
    }

    @Override
    public int generateColor() {
        TypedArray mTypedArray = this.context.getResources().obtainTypedArray(R.array.colors);
        int[] colors = new int[mTypedArray.length()];
        Random r = new Random();
        int tempColor = colors[(r.nextInt()*mTypedArray.length())];
        mTypedArray.recycle();
        return tempColor;
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
}
