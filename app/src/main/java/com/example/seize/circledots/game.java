package com.example.seize.circledots;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class game extends onLaunch implements SurfaceHolder.Callback {

    private Handler handlerApplication;
    private SurfaceHolder _surfaceHolder;
    private SurfaceView _surfaceView;
    private GameLoopThread thread;
    public DotsGrid mDotsGrid;
    public Player mPlayer;
    public float oldX, newX;
    public float oldY, newY;
    static final int MIN_DISTANCEY = 170;
    static final int MIN_DISTANCEX = 170;
    static int distanceBetweenCircles = 0;
    public Display display;
    public Paint mPaint;
    public int score = 0;
    public static final float SCORE_FONT_SIZE = 75f;
    public Typeface FONT_PROXIMA_NOVA_LIGHT;
    public CircleTimer mCircleTimer;
    static int FPS_GAME = 62;
    static long countdDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(0xFFFFFFFF, WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.game);
        FONT_PROXIMA_NOVA_LIGHT = Typeface.createFromAsset(getAssets(), "fonts/ProximaNova-Regular.otf");

        _surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        _surfaceHolder = _surfaceView.getHolder();
        _surfaceHolder.addCallback(this);

        display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    }

    //swipe gesture listener
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                oldX = event.getX();
                oldY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                newX = event.getX();
                newY = event.getY();
                float deltaY = newY - oldY;
                float deltaX = newX - oldX;
                if ((deltaX > 0) && (deltaX > MIN_DISTANCEX)) {
                    //if the players movement does not go outside the grid range
                    if (!(mPlayer.getCurrentX() >= mDotsGrid.widthDots() - 1)) {
                        mPlayer.setCurrentX(mPlayer.getCurrentX() + 1);
                        mPlayer.setDestinationX(mDotsGrid.getDotObject(mPlayer.getCurrentX(), mPlayer.getCurrentY()).getX() - distanceBetweenCircles / 2);
                        mPlayer.movePlayer(PlayerMoves.RIGHT);
                    }
                    Toast.makeText(this, "swiped right", Toast.LENGTH_SHORT).show();

                } else if ((deltaX < 0) && (deltaX < -MIN_DISTANCEX)) {
                    if (!(mPlayer.getCurrentX() <= 0)) {
                        mPlayer.setCurrentX(mPlayer.getCurrentX() - 1);
                        mPlayer.setDestinationX(mDotsGrid.getDotObject(mPlayer.getCurrentX(), mPlayer.getCurrentY()).getX() - distanceBetweenCircles / 2);
                        mPlayer.movePlayer(PlayerMoves.LEFT);
                    }
                    Toast.makeText(this, "swiped left", Toast.LENGTH_SHORT).show();

                } else if ((deltaY < 0) && (deltaY < -MIN_DISTANCEY)) {
                    if (!(mPlayer.getCurrentY() <= 0)) {
                        mPlayer.setCurrentY(mPlayer.getCurrentY() - 1);
                        mPlayer.setDestinationY(mDotsGrid.getDotObject(mPlayer.getCurrentX(), mPlayer.getCurrentY()).getY() - distanceBetweenCircles / 2);
                        mPlayer.movePlayer(PlayerMoves.UP);
                    }
                    Toast.makeText(this, "swiped up", Toast.LENGTH_SHORT).show();

                } else if ((deltaY > 0) && (deltaY > MIN_DISTANCEY)) {
                    if (!(mPlayer.getCurrentY() >= mDotsGrid.widthDots() - 1)) {
                        mPlayer.setCurrentY(mPlayer.getCurrentY() + 1);
                        mPlayer.setDestinationY(mDotsGrid.getDotObject(mPlayer.getCurrentX(), mPlayer.getCurrentY()).getY() - distanceBetweenCircles / 2);
                        mPlayer.movePlayer(PlayerMoves.DOWN);
                    }
                    Toast.makeText(this, "swiped down", Toast.LENGTH_SHORT).show();

                } else if ((deltaY < 15) && (deltaX < 15)) {
                    mPlayer.setCheckColors(true);
                    Toast.makeText(this, "Tap", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        thread = new GameLoopThread(_surfaceHolder, new Handler() {

            @Override
            public void close() {

            }

            @Override
            public void flush() {

            }

            @Override
            public void publish(LogRecord logRecord) {

            }
        });
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        thread.setSurfaceSize(width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    class GameLoopThread extends Thread {
        private int canvasWidth;
        private int canvasHeight;
        private boolean run = false;
        private GamePhysicsThread gamePhysicsThread;

        public GameLoopThread(SurfaceHolder surfaceHolder, Handler handler) {
            _surfaceHolder = surfaceHolder;
            handlerApplication = handler;
            gamePhysicsThread = new GamePhysicsThread();
        }

        public void doStart() {
            synchronized (_surfaceHolder) {
                //load here
                int dotSize = (canvasWidth / 10) / 4;
                distanceBetweenCircles = (3 * canvasWidth / 5) / 5;
                mDotsGrid = new DotsGrid(canvasWidth, canvasHeight, dotSize, getApplicationContext());
                mPlayer = new Player(mDotsGrid.getDotObject(3, 3).getX() - distanceBetweenCircles / 2, mDotsGrid.getDotObject(3, 3).getY() - distanceBetweenCircles / 2, distanceBetweenCircles, distanceBetweenCircles, dotSize, getApplicationContext());
                mDotsGrid.toString();
                mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                mPaint.setAntiAlias(true);
                mPaint.setTypeface(FONT_PROXIMA_NOVA_LIGHT);
                mPaint.setColor(Color.parseColor("#95a5a6"));
                mPaint.setTextSize(SCORE_FONT_SIZE);
                mCircleTimer = new CircleTimer(canvasWidth / 2, (mDotsGrid.getDotObject(0, 0).getY() - dotSize / 2) / 4, dotSize * 2, 270f, 360f);
                mCircleTimer.start(5);
            }
        }

        public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
            int width = bm.getWidth();
            int height = bm.getHeight();
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            // CREATE A MATRIX FOR THE MANIPULATION
            Matrix matrix = new Matrix();
            // RESIZE THE BIT MAP
            matrix.postScale(scaleWidth, scaleHeight);
            // RECREATE THE NEW BITMAP
            Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
            return resizedBitmap;
        }

        public void run() {
            long ticksFPS = 1000 / FPS_GAME;
            long startTime;
            long sleepTime;
            while (run) {
                startTime = System.currentTimeMillis();
                Canvas c = null;
                try {
                    c = _surfaceHolder.lockCanvas(null);
                    synchronized (_surfaceHolder) {
                        gamePhysicsThread.update();
                        doDraw(c);
                    }
                } finally {
                    if (c != null) {
                        _surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
                sleepTime = (System.currentTimeMillis() - startTime);
                if (sleepTime <= ticksFPS) {
                    try {
                        sleep(ticksFPS - sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public void setRunning(boolean b) {
            run = b;
        }

        public void setSurfaceSize(int width, int height) {
            synchronized (_surfaceHolder) {
                canvasHeight = height;
                canvasWidth = width;
                doStart();
            }
        }

        private void doDraw(final Canvas canvas) {
            if (run) {
                canvas.save();
                canvas.drawColor(Color.parseColor("#FFFFFF"));
                String tempScoreString = "Score: " + String.valueOf(score);
                canvas.drawText("Score: " + String.valueOf(score), canvasWidth / 2 - tempScoreString.length() / 4 * SCORE_FONT_SIZE, 3 * canvasHeight / 4 + SCORE_FONT_SIZE, mPaint);
                mDotsGrid.Draw(canvas);
                mPlayer.Draw(canvas);
                mCircleTimer.Draw(canvas);
            }
            canvas.restore();
        }
    }

    class GamePhysicsThread {
        public GamePhysicsThread() {

        }

        public void update() {
            //is the player moving
            if (mPlayer.isMoving() && !mPlayer.isMovingFinished()) {
                switch (mPlayer.getCurrentMove()) {
                    case UP:
                        if (mPlayer.getY() > mPlayer.getDestinationY()) {
                            int temptY = mPlayer.getY();
                            mPlayer.setY(temptY - 5);
                        } else {
                            int tempY = mPlayer.getDestinationY();
                            mPlayer.setY(tempY);
                            mPlayer.setIsMoving(false);
                            mPlayer.setIsMovingFinished(true);
                            mPlayer.setCurrentMove(PlayerMoves.NONE);
                        }
                        break;
                    case DOWN:
                        if (mPlayer.getY() < mPlayer.getDestinationY()) {
                            int temptY = mPlayer.getY();
                            mPlayer.setY(temptY + 5);
                        } else {
                            int tempY = mPlayer.getDestinationY();
                            mPlayer.setY(tempY);
                            mPlayer.setIsMoving(false);
                            mPlayer.setIsMovingFinished(true);
                            mPlayer.setCurrentMove(PlayerMoves.NONE);
                        }
                        break;
                    case LEFT:
                        if (mPlayer.getX() > mPlayer.getDestinationX()) {
                            int temptX = mPlayer.getX();
                            mPlayer.setX(temptX - 5);
                        } else {
                            int tempX = mPlayer.getDestinationX();
                            mPlayer.setX(tempX);
                            mPlayer.setIsMoving(false);
                            mPlayer.setIsMovingFinished(true);
                            mPlayer.setCurrentMove(PlayerMoves.NONE);
                        }
                        break;
                    case RIGHT:
                        if (mPlayer.getX() < mPlayer.getDestinationX()) {
                            int temptX = mPlayer.getX();
                            mPlayer.setX(temptX + 5);
                        } else {
                            int tempX = mPlayer.getDestinationX();
                            mPlayer.setX(tempX);
                            mPlayer.setIsMoving(false);
                            mPlayer.setIsMovingFinished(true);
                            mPlayer.setCurrentMove(PlayerMoves.NONE);
                        }
                        break;
                    case NONE:
                        break;
                    default:
                        break;
                }
            }

            //check if colors match
            if (mPlayer.getCheckColors()) {
                if (mPlayer.getColor() == mDotsGrid.getDotObject(mPlayer.getCurrentX(), mPlayer.getCurrentY()).getColor()) {
                    mPlayer.setColor(mPlayer.generateColor());
                    mDotsGrid.getDotObject(mPlayer.getCurrentX(), mPlayer.getCurrentY()).setColor(mDotsGrid.getDotObject(mPlayer.getCurrentX(), mPlayer.getCurrentY()).generateColor());
                    score++;
                    mPlayer.setCheckColors(false);
                } else {
                    score--;
                    mPlayer.setCheckColors(false);
                }
            }

            //angle measures


        }
    }
}
