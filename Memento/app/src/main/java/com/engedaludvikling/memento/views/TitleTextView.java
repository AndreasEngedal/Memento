package com.engedaludvikling.memento.views;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

public class TitleTextView extends android.support.v7.widget.AppCompatTextView {

    public TitleTextView(Context context) {
        super(context);
        setFont(context);
    }

    public TitleTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setFont(context);
    }

    public TitleTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFont(context);
    }

    private void setFont(Context context) {
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/JosefinSans-Bold.ttf");
        setTypeface(font);
    }
}
