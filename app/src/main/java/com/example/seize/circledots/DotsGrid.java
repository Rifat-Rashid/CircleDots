package com.example.seize.circledots;

import android.content.Context;
import android.graphics.Canvas;

/**
 * Created by Seize on 3/30/2016.
 */
public class DotsGrid {
    private Dot mDot;
    private int screenWidth, screenHeight, dotRadius;
    private Context mContext;
    private Dot[][] dots_grid = new Dot[6][6];

    public DotsGrid() {

    }

    public int widthDots(){
        return 6;
    }

    public int heightDots(){
        return 6;
    }

    public DotsGrid(int screenWidth, int screenHeight, int dotRadius, Context context) {
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        this.dotRadius = dotRadius;
        this.mContext = context;
        createField();
    }

    public void createField(){
        for(int i = 0; i < dots_grid.length; i++){
            for(int j = 0; j <dots_grid[0].length; j++){
                mDot = new Dot(j*(3*screenWidth/5)/5 + screenWidth/5, i*(3*screenWidth/5)/5 + screenHeight/3, (screenWidth/10)/4, this.mContext);
                dots_grid[i][j] = mDot;
            }
        }
    }

    public Dot getDotObject(int x, int y){
        return dots_grid[y][x];
    }

    public void Draw(Canvas canvas){
        for(int i = 0; i < dots_grid.length; i++){
            for(int j = 0; j < dots_grid[0].length; j++){
                dots_grid[i][j].Draw(canvas);
            }
        }
    }

    public void changeGridColors(){
        for(int i = 0; i < dots_grid.length; i++){
            for(int j = 0; j < dots_grid[0].length; j++){
                dots_grid[i][j].setColor(dots_grid[i][j].generateColor());
            }
        }
    }

    @Override
    public String toString(){
        StringBuilder mStringBuilder = new StringBuilder();
        mStringBuilder.append("Dots Grid Sequence{\n");
        for(int i = 0; i < dots_grid.length; i++){
            for(int j = 0; j < dots_grid[0].length; j++){
                mStringBuilder.append("[" + dots_grid[j][i].getX() + ", " + dots_grid[j][i].getY() + "] ");
            }
            mStringBuilder.append("\n");
        }
        return mStringBuilder.toString();
    }
}
