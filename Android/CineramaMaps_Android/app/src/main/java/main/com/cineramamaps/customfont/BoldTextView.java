package main.com.cineramamaps.customfont;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class BoldTextView extends TextView {


    public BoldTextView(Context context) {
        super(context);
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "avenir_black_bold.ttf");
       // Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "cairo_bold.ttf");
        this.setTypeface(tf);
    }

    public BoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "avenir_black_bold.ttf");
        this.setTypeface(tf);
    }

    public BoldTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "avenir_black_bold.ttf");
        this.setTypeface(tf);
    }

    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);


    }

}