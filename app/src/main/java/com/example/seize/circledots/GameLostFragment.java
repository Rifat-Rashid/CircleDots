package com.example.seize.circledots;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;

/**
 * Created by Seize on 4/12/2016.
 */
public class GameLostFragment extends View{
    private int height, width, rx, ry;
    private Paint paint;
    private Paint paint1;
    private RectF rectF;
    private float x, y;
    Context context;

    public GameLostFragment(Context context) {
        super(context);
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        RectF rectF = new RectF(displayMetrics.widthPixels/2 - displayMetrics.widthPixels/4, displayMetrics.heightPixels/2 - displayMetrics.heightPixels/4, displayMetrics.widthPixels/2 + displayMetrics.widthPixels/4, displayMetrics.heightPixels/2 + displayMetrics.heightPixels/4);
        canvas.drawRoundRect(rectF, 45, 45,paint);
        canvas.drawCircle(this.x + this.rectF.width()/2 - 75, this.y + this.rectF.height()/2, 75, paint1);
    }

    public void setHeight(int height) {
        this.height = height;
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
