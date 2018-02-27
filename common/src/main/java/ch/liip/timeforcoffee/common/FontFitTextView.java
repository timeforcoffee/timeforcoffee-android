package ch.liip.timeforcoffee.common;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class FontFitTextView extends AppCompatTextView {

    final int mMaxFontSize = 40;
    private Paint mTestPaint;

    public FontFitTextView(Context context) {
        super(context);
        initialise();
    }

    public FontFitTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialise();
    }

    private void initialise() {
        mTestPaint = new Paint();
        mTestPaint.set(this.getPaint());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);

        refitText(this.getText().toString(), parentHeight, parentWidth);
    }

    @Override
    protected void onTextChanged(final CharSequence text, final int start, final int before, final int after) {
        refitText(text.toString(), this.getHeight(), this.getWidth());
    }


    /* Re size the font so the specified text fits in the text box
     * assuming the text box is the specified width.
     */
    private void refitText(String text, int textHeight, int textWidth) {
        if (textWidth <= 0) return;

        ViewGroup.LayoutParams vlp = this.getLayoutParams();
        int marginTop = ((RelativeLayout.LayoutParams) vlp).topMargin;
        int marginBottom = ((RelativeLayout.LayoutParams) vlp).bottomMargin;
        int marginLeft = ((RelativeLayout.LayoutParams) vlp).leftMargin;
        int marginRight = ((RelativeLayout.LayoutParams) vlp).rightMargin;
        int targetHeight = textHeight - marginTop - marginBottom;
        int targetWidth = textWidth - marginLeft - marginRight;

        float testSize = 80;
        Rect testTextSize = new Rect();
        mTestPaint.set(this.getPaint());

        while (true) {
            testSize -= 2;

            mTestPaint.setTextSize(testSize);
            mTestPaint.getTextBounds(text, 0, text.length(), testTextSize);

            if (testTextSize.width() < targetWidth || testTextSize.height() < targetHeight) {
                break; // Small enough
            }
        }

        // Use lo so that we undershoot rather than overshoot
        testSize = testSize > mMaxFontSize ? mMaxFontSize : testSize;
        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, testSize);
    }
}

