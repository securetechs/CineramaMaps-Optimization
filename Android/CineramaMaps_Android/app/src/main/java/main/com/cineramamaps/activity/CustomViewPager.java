package main.com.cineramamaps.activity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class CustomViewPager extends ViewPager {

    public interface TouchListener {
        void onTouchChange(boolean isTouched);
    }

    private TouchListener touchListener;

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setTouchListener(TouchListener listener) {
        this.touchListener = listener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {

            case MotionEvent.ACTION_DOWN:

                if (touchListener != null) {
                    touchListener.onTouchChange(true);
                }

                break;

            case MotionEvent.ACTION_UP:

            case MotionEvent.ACTION_CANCEL:

                if (touchListener != null) {
                    touchListener.onTouchChange(false);
                }

                break;
        }

        return super.dispatchTouchEvent(ev);
    }
}
