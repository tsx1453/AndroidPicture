package club.autobug.androidpictures.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import club.autobug.androidpictures.R;

public class TagLayout extends ViewGroup {

    private String TAG = "TagLayoutDev";

    private int layoutLeft = 0;
    private int layoutTop = 0;
    private int vertivcal = 30;
    private List<String> selectedTags;
    private onTagItemClickListener onTagItemClickListener;

    {
        selectedTags = new ArrayList<>();
    }

    public TagLayout(Context context, List<String> tags) {
        super(context);
        addTags(tags);
    }

    public TagLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TagLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TagLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heightCount = 0;
        int widthCount = 0;
        int w = getMeasuredWidth();
        measureChildren(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.AT_MOST), heightMeasureSpec);
        if (getChildAt(0) != null) {
            heightCount += getChildAt(0).getMeasuredHeight();
        }
        for (int i = 0; i < getChildCount(); i++) {
//            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
            if (widthCount + getChildAt(i).getMeasuredWidth() > w) {
                heightCount += (getChildAt(i).getMeasuredHeight() + vertivcal);
                widthCount = 0;
            }
            widthCount += getChildAt(i).getMeasuredWidth();
        }
        setMeasuredDimension(w, heightCount + vertivcal);
//        Log.d(TAG, "TagLayout->onMeasure: " + heightCount);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layoutLeft = vertivcal;
        layoutTop = vertivcal;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (layoutLeft + child.getMeasuredWidth() >= getWidth()) {
                layoutLeft = vertivcal;
                layoutTop += child.getMeasuredHeight() + vertivcal;
            }
//            Log.d(TAG, "TagLayout->onLayout: " + i + "->" + child.getWidth() + "," + child.getHeight());
            child.layout(layoutLeft, layoutTop, layoutLeft + child.getMeasuredWidth(), layoutTop + child.getMeasuredHeight());
//            Log.d(TAG, "TagLayout->onLayout: " + child.getWidth() + "," + getWidth() + ",(" + child.getLeft() + "," + child.getTop() + ")");
            layoutLeft += (child.getMeasuredWidth() + vertivcal);
        }
    }

    public void addTags(List<String> tags) {
//        Log.d(TAG, "TagLayout->addTags: " + tags.size());
        if (tags.size() == 0) {
            final TextView textView = new TextView(getContext());
            textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            textView.setText("没有标签");
            textView.setTextSize(20);
            textView.setPadding(10, 5, 10, 5);
            addView(textView);
            return;
        }
        for (String s : tags) {
            final TextView textView = new TextView(getContext());
            textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            textView.setText(s);
            textView.setTextSize(20);
            textView.setPadding(10, 5, 10, 5);
            textView.setBackground(getContext().getDrawable(R.drawable.tag_item_bg));
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tag = ((TextView) v).getText().toString();
                    if (selectedTags.contains(tag)) {
                        selectedTags.remove(tag);
                        v.setSelected(false);
                    } else {
                        selectedTags.add(tag);
                        v.setSelected(true);
                    }
                    if (onTagItemClickListener != null) {
                        onTagItemClickListener.onClick(selectedTags);
                    }
                }
            });
            addView(textView);
        }
//        Log.d(TAG, "TagLayout->addTags: " + getChildCount() + "," + getHeight());
    }

    public List<String> getSelectedTags() {
        return selectedTags;
    }

    public void destory() {
        selectedTags.clear();
        removeAllViews();
    }

    public TagLayout.onTagItemClickListener getOnTagItemClickListener() {
        return onTagItemClickListener;
    }

    public void setOnTagItemClickListener(TagLayout.onTagItemClickListener onTagItemClickListener) {
        this.onTagItemClickListener = onTagItemClickListener;
    }

    public void deleteTags(List<String> list) {
        List<View> deleteList = new ArrayList<>();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (list.contains(((TextView) child).getText().toString())) {
                deleteList.add(child);
            }
        }
        for (View i : deleteList) {
            removeView(i);
        }
    }

    public interface onTagItemClickListener {
        void onClick(List<String> selectedList);
    }

}
