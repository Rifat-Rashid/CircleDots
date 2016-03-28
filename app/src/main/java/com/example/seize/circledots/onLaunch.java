package com.example.seize.circledots;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
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

import me.relex.circleindicator.CircleIndicator;

public class onLaunch extends AppCompatActivity {

    private Button mLaunchButton;
    ViewPager defaultViewPager;
    private CollectionPagerAdapter adapter;
    private TextView terms_of_use_TextView;
    private static final String TERMS_OF_USE_TEXT = "By continuing, you agree to our Terms of Service and Privacy Policy";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager_activity);

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



    }

    private int[] getDisplayMetrics(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        //double screenInches = Math.sqrt(x + y);
        //System.out.println("Screen inches : " + screenInches);
        //screenInches = (double) Math.round(screenInches * 10) / 10;
        //System.out.println("Screen inches : " + screenInches);
    }
}
