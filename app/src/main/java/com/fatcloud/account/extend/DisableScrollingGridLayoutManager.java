package com.fatcloud.account.extend;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.GridLayoutManager;

/**
 * Created by Wangsw on 2020/5/26 0026 14:49.
 * </br>
 * 禁用滑动
 */
public class DisableScrollingGridLayoutManager extends GridLayoutManager {

    private boolean isScrollEnabled = true;

    public DisableScrollingGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public DisableScrollingGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public DisableScrollingGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        return isScrollEnabled && super.canScrollVertically();
    }

//    @Override
//    public boolean canScrollHorizontally() {
//        return isScrollEnabled && super.canScrollVertically();
//    }
}
