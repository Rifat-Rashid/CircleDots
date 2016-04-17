package com.example.seize.circledots;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by Seize on 3/30/2016.
 */
public class Dot implements ColorArrayPallete, ObjectCoordinates {
    private int selectedColor;
    private float x, y;
    private int radius;
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

    public int getRadius(){
        return this.radius;
    }
}
