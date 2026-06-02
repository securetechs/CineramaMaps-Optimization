package main.com.cineramamaps.constant;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;

public class SmoothViewpager extends ViewPager {

    private int baseElevation = 0;
    private int raisingElevation = 1;
    private float smallerScale = 0.6f;

    public SmoothViewpager(Context context) {
        super(context);
        postInitViewPager();
    }

    public SmoothViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
        postInitViewPager();
    }


    private void postInitViewPager() {
        try {
            Class<?> viewpager = ViewPager.class;
            Field mScroller;
            mScroller = viewpager.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(getContext());
            mScroller.set(this, scroller);

            transform_viewPager();
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
    }


    private void transform_viewPager() {
        Resources r = getResources();
        int partialWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics());
        int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, r.getDisplayMetrics());

        int viewPagerPadding = partialWidth + pageMargin;
        setPageMargin(pageMargin);
        setPadding(viewPagerPadding, 0, viewPagerPadding, 0);

        setPageTransformer(false, new CardsPagerTransformerBasic(baseElevation, raisingElevation, smallerScale));
    }

    //TODO: onMeasure Height & Width
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        // Unspecified means that the ViewPager is in a ScrollView WRAP_CONTENT.
        // At Most means that the ViewPager is not in a ScrollView WRAP_CONTENT.
        if (mode == MeasureSpec.UNSPECIFIED || mode == MeasureSpec.AT_MOST) {
            // super has to be called in the beginning so the child views can be initialized.
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int height = 0;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                int h = child.getMeasuredHeight();
                if (h > height) height = h;
            }
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }
        // super has to be called again so the new specs are treated as exact measurements
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
