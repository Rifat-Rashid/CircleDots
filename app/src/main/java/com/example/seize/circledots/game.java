package com.example.seize.circledots;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.games.Games;

import java.util.LinkedList;
import java.util.Queue;
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
    static float distanceBetweenCircles = 0;
    public Display display;
    public Paint mPaint;
    public int score = 0;
    public static final float SCORE_FONT_SIZE = 75f;
    public Typeface FONT_PROXIMA_NOVA_LIGHT;
    public CircleTimer mCircleTimer;
    static int FPS_GAME = 60;
    public int fpg = 0;
    public EliminationGameMode mEliminationGameMode;
    public static boolean isLost;
    public static int player_color_position;
    public Queue<PlayerMoves> playerMovesQueue;
    public String tempScore;
    public float player_shift_faze = 0;
    public static int score_multiplier = 1;
    public GameLostFragment mGameLostFragment;
    public View view;
    public SharedPreferences prefs;
    public TextView t1, t2, t3;
    private ImageButton i1, i2, i3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(0xFFFFFFFF, WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        setContentView(R.layout.game);
        view = (View) findViewById(R.id.view);
        view.setVisibility(View.GONE);
        //imageView.setEnabled(false);
        FONT_PROXIMA_NOVA_LIGHT = Typeface.createFromAsset(getAssets(), "fonts/ProximaNova-Regular.otf");
        t1 = (TextView) findViewById(R.id.ach_textview);
        t2 = (TextView) findViewById(R.id.play_textview);
        t3 = (TextView) findViewById(R.id.rank_textview);

        t1.setTypeface(FONT_PROXIMA_NOVA_LIGHT);
        t2.setTypeface(FONT_PROXIMA_NOVA_LIGHT);
        t3.setTypeface(FONT_PROXIMA_NOVA_LIGHT);

        i1 = (ImageButton) findViewById(R.id.imageButton);
        i2 = (ImageButton) findViewById(R.id.imageButton2);
        i3 = (ImageButton) findViewById(R.id.imageButton3);
        try{
            onLaunch.mGoogleApiClient.connect();
        }catch (Exception e){

        }

        i1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (mGoogleApiClient.isConnected()) {
                        startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(mGoogleApiClient), 1);
                    } else if (!mGoogleApiClient.isConnected()) {
                        //Error with connecting to leaderboards
                        onLaunch.mGoogleApiClient.connect();
                        System.out.println("Could not start leaderboards Intent");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        i3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        _surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        _surfaceHolder = _surfaceView.getHolder();
        _surfaceHolder.addCallback(this);

        playerMovesQueue = new LinkedList<PlayerMoves>();
        isLost = false;
        player_color_position = 0;

        display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        System.out.println(prefs.getInt("key1", 0));


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
                    playerMovesQueue.add(PlayerMoves.RIGHT);

                } else if ((deltaX < 0) && (deltaX < -MIN_DISTANCEX)) {
                    playerMovesQueue.add(PlayerMoves.LEFT);

                } else if ((deltaY < 0) && (deltaY < -MIN_DISTANCEY)) {
                    playerMovesQueue.add(PlayerMoves.UP);

                } else if ((deltaY > 0) && (deltaY > MIN_DISTANCEY)) {
                    playerMovesQueue.add(PlayerMoves.DOWN);

                } else if ((deltaY < 15) && (deltaX < 15)) {
                    //check for a tap
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
                mCircleTimer = new CircleTimer(2 * dotSize + dotSize * 4, 2 * dotSize + dotSize * 4, dotSize * 4, 270f, 360f, getApplicationContext());
                mCircleTimer.start(60);
                mEliminationGameMode = new EliminationGameMode(getApplicationContext());
                mEliminationGameMode.startElimenationGameMode();
                mPlayer.setColor(mEliminationGameMode.getColorAt(0));
                player_shift_faze = 5;

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

        /*
        function: Game loop that updates physics engine and draw
        calls gamePhysicsThread.update() and doDraw(c)
         */
        public void run() {
            long ticksFPS = 1000 / FPS_GAME;
            long startTime;
            long sleepTime;

            while (run) {
                //   startTime = System.currentTimeMillis();
                //   if (System.currentTimeMillis() - sT >= 1000) {
                //       System.out.println(fpg);
                //       fpg = 0;
                //        sT = System.currentTimeMillis();
                //    } else {
                //
                //     }
                Canvas c = null;
                try {
                    c = _surfaceHolder.lockCanvas(null);
                    synchronized (_surfaceHolder) {
                        gamePhysicsThread.update();
                        doDraw(c);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (c != null) {
                        _surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
                if (mPlayer.isMoving()) {
                    mPlayer.setNumFrames(mPlayer.getNumFrames() + 1);
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

        /*
        @Param Canvas
        function: draws all objects to screen
         */
        private void doDraw(final Canvas canvas) {
            if (run) {
                canvas.save();
                canvas.drawColor(Color.parseColor("#FFFFFF"));
                tempScore = "Score: " + String.valueOf(score);
                canvas.drawText(tempScore, canvasWidth / 2 - tempScore.length() / 4 * SCORE_FONT_SIZE, 3 * canvasHeight / 4 + SCORE_FONT_SIZE, mPaint);
                tempScore = null;
                mDotsGrid.Draw(canvas);
                mPlayer.Draw(canvas);
                mCircleTimer.Draw(canvas);
                mEliminationGameMode.Draw(canvas);
                //shade the background
                if (isLost) {
                    canvas.drawColor(Color.parseColor("#78000000"));
                    game.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            view.setVisibility(View.VISIBLE);

                        }
                    });
                }
            }
            canvas.restore();
        }
    }

    class GamePhysicsThread {

        public GamePhysicsThread() {

        }

        public void update() {
            //is the player moving
            if (!isLost) {
                if (!playerMovesQueue.isEmpty()) {
                    if (!mPlayer.isMoving() && mPlayer.isMovingFinished()) {
                        switch (playerMovesQueue.peek()) {
                            case UP:
                                if (!(mPlayer.getCurrentY() <= 0)) {
                                    mPlayer.setCurrentY(mPlayer.getCurrentY() - 1);
                                    mPlayer.setDestinationY(mDotsGrid.getDotObject(mPlayer.getCurrentX(), mPlayer.getCurrentY()).getY() - distanceBetweenCircles / 2);
                                    mPlayer.movePlayer(PlayerMoves.UP);
                                } else {
                                    playerMovesQueue.remove();
                                    mPlayer.setCurrentMove(PlayerMoves.NONE);
                                }
                                break;
                            case DOWN:
                                if (!(mPlayer.getCurrentY() >= mDotsGrid.widthDots() - 1)) {
                                    mPlayer.setCurrentY(mPlayer.getCurrentY() + 1);
                                    mPlayer.setDestinationY(mDotsGrid.getDotObject(mPlayer.getCurrentX(), mPlayer.getCurrentY()).getY() - distanceBetweenCircles / 2);
                                    mPlayer.movePlayer(PlayerMoves.DOWN);
                                } else {
                                    playerMovesQueue.remove();
                                    mPlayer.setCurrentMove(PlayerMoves.NONE);
                                }
                                break;
                            case LEFT:
                                if (!(mPlayer.getCurrentX() <= 0)) {
                                    mPlayer.setCurrentX(mPlayer.getCurrentX() - 1);
                                    mPlayer.setDestinationX(mDotsGrid.getDotObject(mPlayer.getCurrentX(), mPlayer.getCurrentY()).getX() - distanceBetweenCircles / 2);
                                    mPlayer.movePlayer(PlayerMoves.LEFT);
                                } else {
                                    playerMovesQueue.remove();
                                    mPlayer.setCurrentMove(PlayerMoves.NONE);
                                }
                                break;
                            case RIGHT:
                                if (!(mPlayer.getCurrentX() >= mDotsGrid.widthDots() - 1)) {
                                    mPlayer.setCurrentX(mPlayer.getCurrentX() + 1);
                                    mPlayer.setDestinationX(mDotsGrid.getDotObject(mPlayer.getCurrentX(), mPlayer.getCurrentY()).getX() - distanceBetweenCircles / 2);
                                    mPlayer.movePlayer(PlayerMoves.RIGHT);
                                } else {
                                    playerMovesQueue.remove();
                                    mPlayer.setCurrentMove(PlayerMoves.NONE);

                                }
                                break;
                        }
                    }
                } else {
                    // playerMovesQueue.add(PlayerMoves.NONE);
                }
                if (mPlayer.isMoving() && !mPlayer.isMovingFinished()) {
                    switch (playerMovesQueue.peek()) {
                        case UP:
                            if (mPlayer.getY() > mPlayer.getDestinationY()) {
                                //mPlayer.setY(mPlayer.getY() - (distanceBetweenCircles/mPlayer.getMovementFrames())*mPlayer.getNumFrames());
                                mPlayer.setY(mPlayer.getY() - player_shift_faze);
                            } else {
                                float tempY = mPlayer.getDestinationY();
                                mPlayer.setY(tempY);
                                mPlayer.setIsMoving(false);
                                mPlayer.setIsMovingFinished(true);
                                mPlayer.setCurrentMove(PlayerMoves.NONE);
                                mPlayer.setNumFrames(0);
                                playerMovesQueue.remove();
                            }
                            break;
                        case DOWN:
                            if (mPlayer.getY() < mPlayer.getDestinationY()) {
                                float temptY = mPlayer.getY();
                                mPlayer.setY(temptY + player_shift_faze);
                            } else {
                                float tempY = mPlayer.getDestinationY();
                                mPlayer.setY(tempY);
                                mPlayer.setIsMoving(false);
                                mPlayer.setIsMovingFinished(true);
                                mPlayer.setCurrentMove(PlayerMoves.NONE);
                                playerMovesQueue.remove();
                            }
                            break;
                        case LEFT:
                            if (mPlayer.getX() > mPlayer.getDestinationX()) {
                                float temptX = mPlayer.getX();
                                mPlayer.setX(temptX - player_shift_faze);
                            } else {
                                float tempX = mPlayer.getDestinationX();
                                mPlayer.setX(tempX);
                                mPlayer.setIsMoving(false);
                                mPlayer.setIsMovingFinished(true);
                                mPlayer.setCurrentMove(PlayerMoves.NONE);
                                playerMovesQueue.remove();
                            }
                            break;
                        case RIGHT:
                            if (mPlayer.getX() < mPlayer.getDestinationX()) {
                                float temptX = mPlayer.getX();
                                mPlayer.setX(temptX + player_shift_faze);
                            } else {
                                float tempX = mPlayer.getDestinationX();
                                mPlayer.setX(tempX);
                                mPlayer.setIsMoving(false);
                                mPlayer.setIsMovingFinished(true);
                                mPlayer.setCurrentMove(PlayerMoves.NONE);
                                playerMovesQueue.remove();
                            }
                            break;
                        case NONE:
                            break;
                        default:
                            break;
                    }
                }

                //see if the timer is still running
                if (mCircleTimer.getIsRunning()) {
                    //see if we should check colors
                    if (mPlayer.getCheckColors()) {
                        //compare color of player with color of dot on the grid @players (x,y)'s position
                        if (mPlayer.getColor() == mDotsGrid.getDotObject(mPlayer.getCurrentX(), mPlayer.getCurrentY()).getColor()) {
                            //does the color of the player match the color in the sequence?
                            if (mPlayer.getColor() == mEliminationGameMode.getColorAt(player_color_position)) {
                                //check to see if player_color_position is not going past the value of the color array length
                                if (player_color_position == mEliminationGameMode.getColors().length - 1) {
                                    mEliminationGameMode.nextLevel();
                                    mDotsGrid.changeGridColors();
                                    player_color_position = 0;
                                    mPlayer.setColor(mEliminationGameMode.getColorAt(player_color_position));
                                    score += 3 * score_multiplier;
                                    score_multiplier++;
                                } else {
                                    mPlayer.setColor(mEliminationGameMode.getColorAt(player_color_position + 1));
                                    score += 3 * score_multiplier;
                                    player_color_position++;
                                }
                            }
                        } else {
                            endGame();
                        }
                        mPlayer.setCheckColors(false);
                    }

                } else {
                    endGame();
                }
            } else {

            }
        }
    }

    /*
    @Param none;
    Store high score in SharedPreferences
    Condition: if score > oldScore
     */
    public void endGame() {
        SharedPreferences.Editor editor = prefs.edit();
        int oldScore = prefs.getInt("key1", 0);
        if (score > oldScore) {
            editor.putInt("key1", score);
            editor.apply();
            try{
                Games.Leaderboards.submitScore(mGoogleApiClient, "CgkIqd7QlfQZEAIQAQ", score);
            }catch (Exception e){

            }
        }
        score = 0;
        score_multiplier = 1;
        isLost = true;
    }
}
