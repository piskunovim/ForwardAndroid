package ru.forwardmobile.tforwardpayment;

import android.util.AttributeSet;
import android.widget.ScrollView;
import android.content.Context;

/**
 * Created by PiskunovI on 29.08.2014.
 */
public class ScrollViewOrange extends ScrollView {

    // fade to green by default
    private static int mFadeColor = 0xFF00FF00;

    public ScrollViewOrange(Context context, AttributeSet attrs)
    {
        this(context, attrs,0);
    }
    public ScrollViewOrange(Context context, AttributeSet attrs, int defStyle)
    {
        super(context,attrs,defStyle);
        setFadingEdgeLength(30);
        setVerticalFadingEdgeEnabled(true);
    }
    @Override
    public int getSolidColor()
    {
        return mFadeColor;
    }
    public void setFadeColor( int fadeColor )
    {
        mFadeColor = fadeColor;
    }
    public int getFadeColor()
    {
        return mFadeColor;
    }
}
