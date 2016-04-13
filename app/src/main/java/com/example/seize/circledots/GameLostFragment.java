package com.example.seize.circledots;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by Seize on 4/12/2016.
 */
public class GameLostFragment {
    private int height, width, rx, ry;
    private Paint paint;
    private RectF rectF;
    private float x, y;

    public GameLostFragment() {

    }

    public GameLostFragment(float x, float y, int height, int width){
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;

        this.rectF = new RectF(this.x - width, this.y - height, this.x + width, this.y + height);
        //this.paint = new Paint()
    }

    public void Draw(Canvas canvas) {

    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getRx() {
        return rx;
    }

    public void setRx(int rx) {
        this.rx = rx;
    }

    public int getRy() {
        return ry;
    }

    public void setRy(int ry) {
        this.ry = ry;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

}
