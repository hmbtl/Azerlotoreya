package nms.az.azerlotereya.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by anar on 7/7/15.
 */
public class SquareTextView extends android.support.v7.widget.AppCompatTextView {
    private static final float RATIO = 4f / 3f;

    public SquareTextView(Context context, AttributeSet attrs){
        super(context,attrs);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
