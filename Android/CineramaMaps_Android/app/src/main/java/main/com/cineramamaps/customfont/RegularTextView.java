package main.com.cineramamaps.customfont;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class RegularTextView extends TextView {


    public RegularTextView(Context context) {
        super(context);
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "avenir_light.ttf");
        this.setTypeface(tf);
    }

    public RegularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "avenir_light.ttf");
        this.setTypeface(tf);
    }

    public RegularTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "avenir_light.ttf");
        this.setTypeface(tf);
    }

    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);


    }

}