package com.example.seize.circledots;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class game extends onLaunch implements SurfaceHolder.Callback {

    private Handler handlerApplication;
    private SurfaceHolder _surfaceHolder;
    private SurfaceView _surfaceView;
    static Paint counter = new Paint(Paint.ANTI_ALIAS_FLAG);
    private GameLoopThread thread;
    public DotsGrid mDotsGrid;
    public Player mPlayer;
    public UserDisplay mUserDisplay;

    static int FPS_GAME = 61;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(0xFFFFFFFF, WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.game);
        mUserDisplay = new UserDisplay(this);

        _surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        _surfaceHolder = _surfaceView.getHolder();
        _surfaceHolder.addCallback(this);
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
                int dotSize = (canvasWidth/10)/4;
                int distanceBetweenCircles = (3*canvasWidth/5)/5;
                mDotsGrid = new DotsGrid(canvasWidth, canvasHeight, dotSize, getApplicationContext());
                mPlayer = new Player(mDotsGrid.getDotObject(3, 3).getX() - distanceBetweenCircles/2, mDotsGrid.getDotObject(3, 3).getY() - distanceBetweenCircles/2, distanceBetweenCircles, distanceBetweenCircles, dotSize, getApplicationContext());
                System.out.println(mDotsGrid.getDotObject(3, 5).getX());
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
                Canvas c = null;
                startTime = System.currentTimeMillis();
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
                long tempMilli = System.currentTimeMillis();
                sleepTime = ticksFPS - (tempMilli - startTime);
                try {
                    if (sleepTime >= 0) {
                        sleep(sleepTime);
                    } else {
                        //sleep(10);
                    }
                } catch (Exception e) {

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
                mDotsGrid.Draw(canvas);
                mPlayer.Draw(canvas);
            }
            canvas.restore();
        }
    }

    class GamePhysicsThread {
        public GamePhysicsThread() {

        }

        public void update() {

        }
    }
}
