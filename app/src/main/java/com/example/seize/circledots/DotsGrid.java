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
    private Dot[][] dots_grid = new Dot[10][5];

    public DotsGrid() {

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
                mDot = new Dot(100+ j*(this.screenWidth - 200)/5, 150 + i*(this.screenHeight - 300)/10, 85, this.mContext);
                dots_grid[i][j] = mDot;
            }
        }
    }

    public void Draw(Canvas canvas){
        //mDot.Draw(canvas);
        for(int i = 0; i < dots_grid.length; i++){
            for(int j = 0; j < dots_grid[0].length; j++){
                dots_grid[i][j].Draw(canvas);
            }
        }


    }
}
