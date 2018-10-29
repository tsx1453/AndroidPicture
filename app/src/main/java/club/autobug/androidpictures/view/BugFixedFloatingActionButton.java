package club.autobug.androidpictures.view;

import android.content.Context;
import android.graphics.Matrix;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.util.AttributeSet;

public class BugFixedFloatingActionButton extends FloatingActionButton {

    private Matrix imageMatrix;

    public BugFixedFloatingActionButton(Context context) {
        super(context);
    }

    public BugFixedFloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BugFixedFloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        imageMatrix = getImageMatrix();
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        setImageMatrix(imageMatrix);
    }
}
