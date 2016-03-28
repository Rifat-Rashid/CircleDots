package com.example.seize.circledots;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.plus.Plus;

import me.relex.circleindicator.CircleIndicator;

public class onLaunch extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Button mLaunchButton;
    private GoogleApiClient mGoogleApiClient;
    ViewPager defaultViewPager;
    private CollectionPagerAdapter adapter;
    private TextView terms_of_use_TextView;
    protected PowerManager.WakeLock wakeLock;
    private static final String TERMS_OF_USE_TEXT = "By continuing, you agree to our Terms of Service and Privacy Policy";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager_activity);
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "Lock");
        this.wakeLock.acquire();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API)
                .addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN)
                .addScope(Games.SCOPE_GAMES)
                .build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        } else {
            //to do
            //find support library that changes status bar color!
        }


        adapter = new CollectionPagerAdapter(getSupportFragmentManager());
        defaultViewPager = (ViewPager) findViewById(R.id.viewpager_default);
        defaultViewPager.setOffscreenPageLimit(3);

        final CircleIndicator defaultIndicator = (CircleIndicator) findViewById(R.id.view);
        defaultViewPager.setAdapter(adapter);
        defaultIndicator.setViewPager(defaultViewPager);

        final Typeface FONT_PROXIMA_NOVA_LIGHT = Typeface.createFromAsset(this.getAssets(), "fonts/ProximaNova-Regular.otf");
        terms_of_use_TextView = (TextView) findViewById(R.id.textView2);
        terms_of_use_TextView.setTypeface(FONT_PROXIMA_NOVA_LIGHT);

        //Change color of "Terms of Service" and "Privacy Policy" to a dark gray
        Spannable WordtoSpan = new SpannableString(TERMS_OF_USE_TEXT);
        //Terms of Sevice Color Change
        WordtoSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#424242")), TERMS_OF_USE_TEXT.indexOf("Terms"),
                TERMS_OF_USE_TEXT.indexOf("Terms") + new String("Terms of Service").length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //Privacy Policy Color Change
        WordtoSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#424242")),
                TERMS_OF_USE_TEXT.indexOf("Privacy"),
                TERMS_OF_USE_TEXT.indexOf("Privacy") + new String("Privacy Policy").length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        terms_of_use_TextView.setText(WordtoSpan);

        UserDisplay userDisplay = new UserDisplay(this);
        System.out.println(userDisplay.toString());


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.wakeLock.release();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.wakeLock.acquire();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.wakeLock.release();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
