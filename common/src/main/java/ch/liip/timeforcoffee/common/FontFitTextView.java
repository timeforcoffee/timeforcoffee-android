package ch.liip.timeforcoffee.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class FontFitTextView extends AppCompatTextView implements View.OnClickListener {

    private int mMaxFontSize;

    private OnClickListener clickListener;

    public FontFitTextView(Context context) {
        super(context);
        initialise();
    }

    public FontFitTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.FontFitTextView,
                0, 0);

        try {
            mMaxFontSize = a.getInteger(R.styleable.FontFitTextView_maxFontSize, 50);
        } finally {
            a.recycle();
        }

        initialise();
    }

    private void initialise() {
        mTestPaint = new Paint();
        mTestPaint.set(this.getPaint());
        //max size defaults to the initially specified text size unless it is too small
    }

    /* Re size the font so the specified text fits in the text box
     * assuming the text box is the specified width.
     */
    private void refitText(String text, int textWidth) {
        if (textWidth <= 0)
            return;

        ViewGroup.LayoutParams vlp = this.getLayoutParams();
        int marginLeft = ((RelativeLayout.LayoutParams) vlp).leftMargin;
        int rightMargin = ((RelativeLayout.LayoutParams) vlp).rightMargin;

        int targetWidth = textWidth - marginLeft - rightMargin;
        float hi = 100;
        float lo = 2;
        final float threshold = 0.5f; // How close we have to be

        mTestPaint.set(this.getPaint());

        while ((hi - lo) > threshold) {
            float size = (hi + lo) / 2;
            mTestPaint.setTextSize(size);
            if (mTestPaint.measureText(text) >= targetWidth)
                hi = size; // too big
            else
                lo = size; // too small
        }
        // Use lo so that we undershoot rather than overshoot
        lo = lo > mMaxFontSize ? mMaxFontSize : lo;
        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, lo);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        if (l == this) {
            super.setOnClickListener(l);
        } else {
            clickListener = l;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        //int height = getMeasuredHeight();
        refitText(this.getText().toString(), parentWidth);
        //this.setMeasuredDimension(parentWidth, height);
    }

    @Override
    protected void onTextChanged(final CharSequence text, final int start, final int before, final int after) {
        refitText(text.toString(), this.getWidth());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw) {
            refitText(this.getText().toString(), w);
        }
    }

    @Override
    public void onClick(View view) {
        if (clickListener != null) {
            clickListener.onClick(this);
        }
    }

    //Attributes
    private Paint mTestPaint;
}

