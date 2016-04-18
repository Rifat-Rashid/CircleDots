package com.example.seize.circledots;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by Rifat Rashid on 4/7/2016.
 */
public class EliminationGameMode implements ColorArrayPallete {
    private int color;
    private Context context;
    private HashMap<Integer, int[]> hashMap;
    final int NUMBER_OF_GENERATED_LEVELS = 15;
    private boolean isLost = false;
    private int currentLevel;
    private Dot[] mDots;
    final String text = "Color Bonuses: ";
    Paint textPaint;

    //default constructor
    public EliminationGameMode() {

    }

    public EliminationGameMode(Context context) {
        this.context = context;
        this.hashMap = new HashMap<>();
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.BLACK);
        final Typeface FONT_PROXIMA_NOVA_LIGHT = Typeface.createFromAsset(this.context.getAssets(), "fonts/ProximaNova-Regular.otf");
        textPaint.setTypeface(FONT_PROXIMA_NOVA_LIGHT);
        textPaint.setTextSize(35);
    }

    public void startElimenationGameMode() {
        generateLevels(NUMBER_OF_GENERATED_LEVELS);
        currentLevel = 1;
        fillArray();
    }

    public void fillArray(){
        Dot tempDot;
        mDots = new Dot[this.hashMap.get(this.currentLevel).length];
        for(int i = 0; i < mDots.length; i++){
            //mystery numbers fix later
            tempDot = new Dot(1080/5 + 75*i + (text.length() * 35)/2 + 10, 500, (1080/10)/4, this.getContext());
            tempDot.setColor(this.hashMap.get(this.currentLevel)[i]);
            mDots[i] = tempDot;
        }
    }

    public int[] getColors(){
        return hashMap.get(currentLevel);
    }

    public int getColorAt(int index){
        //check to see if requested index is within range
        if(!(index >= hashMap.get(currentLevel).length)){
            return hashMap.get(currentLevel)[index];
        }else{
            return -1;
        }
    }

    public void Draw(Canvas canvas){
        for(int i = 0; i < mDots.length; i++){
            mDots[i].Draw(canvas);
        }
        canvas.drawText(text, 1080/5, 510, textPaint);
    }

    public void nextLevel(){
        this.currentLevel++;
        fillArray();
    }

    protected void generateLevels(int numberOfLevels) {
        for (int i = 1; i <= numberOfLevels; i++) {
            try {
                if (!(this.hashMap.containsKey(i))) {
                    this.hashMap.put(i, generateColorArray());
                } else {
                    continue;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int getNUMBER_OF_GENERATED_LEVELS() {
        return NUMBER_OF_GENERATED_LEVELS;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public HashMap<Integer, int[]> getHashMap() {
        return hashMap;
    }

    public void setHashMap(HashMap<Integer, int[]> hashMap) {
        this.hashMap = hashMap;
    }

    public void setCurrentLevel(int currentLevel){
        this.currentLevel = currentLevel;
    }

    public int getCurrentLevel(){
        return this.currentLevel;
    }

    public int[] generateColorArray() {
        Random r = new Random();
        int[] tempArray = new int[r.nextInt(5 - 3 + 1) + 3];
        //fill array with color values
        try {
            for (int i = 0; i < tempArray.length; i++) {
                tempArray[i] = generateColor();
            }
        } catch (IndexOutOfBoundsException obe) {
            obe.printStackTrace();
        }
        return tempArray;
    }

    @Override
    public int getColor() {
        return this.color;
    }

    @Override
    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public int generateColor() {
        TypedArray mTypedArray = this.context.getResources().obtainTypedArray(R.array.colors_Array);
        Random r = new Random();
        return mTypedArray.getColor((r.nextInt(mTypedArray.length())), 0);
    }

    public void setIsLost(boolean b){
        this.isLost = b;
    }

    public boolean getIsLost(){
        return this.isLost;
    }

    @Override
    public String toString(){
        return "";
    }
}
