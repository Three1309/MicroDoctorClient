package com.zhuolang.fu.microdoctorclient.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by wunaifu on 2017/5/16.
 */
public class MyTextView extends TextView{
    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    int mLastx = -1;

    int mLasty = -1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                if (mLasty != -1) {
                    RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams)this.getLayoutParams();
                    linearParams.leftMargin +=(x-mLastx);
                    linearParams.topMargin +=(y-mLasty);
                    this.setLayoutParams(linearParams);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        mLastx = x;
        mLasty = y;
        return true;
    }
}
