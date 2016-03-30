package com.example.seize.circledots;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Seize on 3/30/2016.
 */
public class Dot implements ColorArrayPallete, ObjectCoordinates {
    private Color selectedColor;
    private int x, y, radius;
    private Paint mPaint;

    //default constructor
    public Dot(){

    }

    public Dot(int x, int y, int radius){
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public void Draw(Canvas canvas){

    }

    @Override
    public Color getColor() {
        return this.selectedColor;
    }

    @Override
    public void setColor(Color color) {
        this.selectedColor = color;
    }

    @Override
    public void generateColor() {

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
