package com.example.seize.circledots;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Seize on 3/23/2016.
 */
public class DemoObjectFragment extends Fragment {
    public static final String ARG_OBJECT = "object";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.style_chooser_page, container, false);
        Bundle args = getArguments();
        int index = args.getInt(ARG_OBJECT);
        int descRef = R.string.firstPageDesc;
        int imageRef = R.drawable.nexus_5_device_base_half1;
        final Typeface FONT_PROXIMA_NOVA_LIGHT = Typeface.createFromAsset(getActivity().getAssets(), "fonts/ProximaNova-Regular.otf");
        switch (index){
            case 0:
                break;
            case 1:
                descRef = R.string.secondPageDesc;
                imageRef = R.drawable.nexus_5_device_base_half1;
                break;
            case 2:
                descRef = R.string.thirdPageDesc;
                imageRef = R.drawable.nexus_5_device_base_half1;
                break;
        }
        if(index != 0){
            ImageView imga = (ImageView) rootView.findViewById(R.id.applogo_icon);
            imga.setVisibility(View.INVISIBLE);
            TextView title = (TextView) rootView.findViewById(R.id.dots_title);
            title.setVisibility(View.INVISIBLE);
            TextView tv = (TextView) rootView.findViewById(R.id.sub_text);
            tv.setVisibility(View.VISIBLE);
            tv.setText(getResources().getString(descRef));
            tv.setTypeface(FONT_PROXIMA_NOVA_LIGHT);
            ImageView iv = (ImageView) rootView.findViewById(R.id.img);
            iv.setVisibility(View.VISIBLE);
            iv.setImageResource(imageRef);
        }else{
            TextView tv = (TextView) rootView.findViewById(R.id.sub_text);
            tv.setVisibility(View.INVISIBLE);
            ImageView imga = (ImageView) rootView.findViewById(R.id.applogo_icon);
            imga.setVisibility(View.VISIBLE);
            ImageView iv = (ImageView) rootView.findViewById(R.id.img);
            iv.setVisibility(View.INVISIBLE);
            TextView title = (TextView) rootView.findViewById(R.id.dots_title);
            title.setTypeface(FONT_PROXIMA_NOVA_LIGHT);
            title.setVisibility(View.VISIBLE);
        }
        return rootView;
    }
}
