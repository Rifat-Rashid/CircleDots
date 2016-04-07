package com.example.seize.circledots;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import java.io.BufferedReader;
import java.util.Random;

/**
 * Created by Rifat Rashid on 4/4/2016.
 */
public class Player implements ColorArrayPallete, ObjectCoordinates {
    private int x, y;
    private int width, height;
    private Paint eraser;
    private Context context;
    private Canvas tempCanvas;
    private Bitmap player_bitmap;
    private int player_color;
    private int travelDistance = 0;
    private int circle_radius;
    private boolean isMoving = false;
    private boolean isMovingFinished = true;
    private int oldX = 0;
    private PlayerMoves currentMove = PlayerMoves.NONE;
    private int destinationX, destinationY;
    private int currentX = 3;
    private int currentY = 3;
    private boolean checkColors = false;


    public Player() {

    }

    public Player(int x, int y, int width, int height, int circle_radius, Context context) {
        this.x = x;
        this.y = y;
        this.destinationX = x;
        this.destinationY = y;
        this.width = width;
        this.height = height;
        this.circle_radius = circle_radius;
        this.context = context;
        setUpPaintStack();
        setUpPlayerBitmap();
    }

    private void setUpPaintStack() {
        this.player_color = generateColor();
    }

    private void setUpPlayerBitmap() {
        this.player_bitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888);
        this.eraser = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.eraser.setAntiAlias(true);
        this.eraser.setColor(0xFFFFFFFF);
        this.eraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        this.tempCanvas = new Canvas(player_bitmap);
    }

    public void Draw(Canvas canvas) {
        player_bitmap.eraseColor(Color.TRANSPARENT);
        tempCanvas.drawColor(this.player_color);
        tempCanvas.drawCircle(getWidth() / 2, getHeight() / 2, this.circle_radius, this.eraser);
        canvas.drawBitmap(this.player_bitmap, getX(), getY(), null);
    }

    public void setCurrentMove(PlayerMoves playerMoves){
        this.currentMove = playerMoves;
    }

    public void movePlayer(PlayerMoves playerMoves){
        this.currentMove = playerMoves;
        this.setIsMoving(true);
        this.setIsMovingFinished(false);
    }

    public boolean getCheckColors(){
        return this.checkColors;
    }

    public void setCheckColors(boolean checkColors){
        this.checkColors = checkColors;
    }

    public int getCurrentX(){
        return this.currentX;
    }

    public int getCurrentY(){
        return this.currentY;
    }

    public void setCurrentX(int currentX){
        this.currentX = currentX;
    }

    public void setCurrentY(int currentY){
        this.currentY = currentY;
    }

    public int getDestinationX(){
        return this.destinationX;
    }

    public int getDestinationY(){
        return this.destinationY;
    }

    public void setDestinationX(int destinationX){
        this.destinationX = destinationX;
    }

    public void setDestinationY(int destinationY){
        this.destinationY = destinationY;
    }

    public PlayerMoves getCurrentMove(){
        return this.currentMove;
    }

    @Override
    public int getColor() {
        return this.player_color;
    }

    @Override
    public void setColor(int color) {
        this.player_color = color;
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

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    public int getTravelDistance() {
        return this.travelDistance;
    }

    public void setTravelDistance(int travelDistance) {
        this.travelDistance = travelDistance;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setIsMoving(boolean isMoving) {
        this.isMoving = isMoving;
    }

    public boolean isMovingFinished() {
        return isMovingFinished;
    }

    public void setIsMovingFinished(boolean isMovingFinished) {
        this.isMovingFinished = isMovingFinished;
    }

    public int getOldX() {
        return oldX;
    }

    public void setOldX(int oldX) {
        this.oldX = oldX;
    }
}
