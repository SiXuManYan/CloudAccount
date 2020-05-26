package com.account.view.extend;


import android.content.Context;
import android.util.AttributeSet;

import com.jude.easyrecyclerview.EasyRecyclerView;

public class ScrollRecyclerView extends EasyRecyclerView {


    public ScrollRecyclerView(Context context) {
        super(context);
    }

    public ScrollRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canScrollVertically(int direction) {
        return mRecycler.canScrollVertically(direction);
    }
}
