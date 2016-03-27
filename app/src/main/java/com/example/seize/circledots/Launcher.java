package com.example.seize.circledots;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import me.relex.circleindicator.CircleIndicator;

public class Launcher extends AppCompatActivity {
    private TextView title_Text;
    private View v;
    private Button mLaunchButton;
    ViewPager defaultViewPager;
    private CollectionPagerAdapter adapter;
    private TextView subText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager_activity);

        final Typeface FONT_PROXIMA_NOVA_LIGHT = Typeface.createFromAsset(this.getAssets(), "fonts/ProximaNova-Regular.otf");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }else {
            //to do
        }

        adapter = new CollectionPagerAdapter(getSupportFragmentManager());
        defaultViewPager = (ViewPager) findViewById(R.id.viewpager_default);
        defaultViewPager.setOffscreenPageLimit(3);

        final CircleIndicator defaultIndicator = (CircleIndicator) findViewById(R.id.view);
        defaultViewPager.setAdapter(adapter);
        defaultIndicator.setViewPager(defaultViewPager);

        subText = (TextView) findViewById(R.id.textView2);
        subText.setTypeface(FONT_PROXIMA_NOVA_LIGHT);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels/dm.xdpi,2);
        double y = Math.pow(dm.heightPixels/dm.ydpi,2);
        double screenInches = Math.sqrt(x + y);
        Log.d("debug", "Screen inches : " + screenInches);
        System.out.println("Screen inches : " + screenInches);

        screenInches=  (double)Math.round(screenInches * 10) / 10;
        System.out.println("Screen inches : " + screenInches);

    }
}
