package main.com.cineramamaps.activity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class OutlinedTextView extends AppCompatTextView {

    private int outlineColor = Color.WHITE;
    private float outlineWidth = 4f;

    public OutlinedTextView(Context context) {
        super(context);
    }

    public OutlinedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OutlinedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOutlineColor(int color) {
        this.outlineColor = color;
        invalidate();
    }

    public void setOutlineWidth(float width) {
        this.outlineWidth = width;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int originalColor = getCurrentTextColor();

        // Draw outline
        getPaint().setStyle(Paint.Style.STROKE);
        getPaint().setStrokeWidth(outlineWidth);
        setTextColor(outlineColor);
        super.onDraw(canvas);

        // Draw fill
        getPaint().setStyle(Paint.Style.FILL);
        setTextColor(originalColor);
        super.onDraw(canvas);
    }
}
