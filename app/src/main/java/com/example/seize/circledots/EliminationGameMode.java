package com.example.seize.circledots;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by Rifat Rashid on 4/7/2016.
 */
public class EliminationGameMode implements ColorArrayPallete {
    private int color;
    private Context context;
    private HashMap<Integer, int[]> hashMap;
    final int NUMBER_OF_GENERATED_LEVELS = 4;

    //default constructor
    public EliminationGameMode() {

    }

    public EliminationGameMode(Context context) {
        this.context = context;
        this.hashMap = new HashMap<>();
    }

    public void startElimenationGameMode() {
        generateLevels(NUMBER_OF_GENERATED_LEVELS);
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
}
