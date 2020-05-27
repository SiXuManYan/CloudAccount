package com.account.view.swipe.smart;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.account.R;
import com.account.view.loading.CommonProgressBar;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshKernel;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.scwang.smart.refresh.layout.constant.SpinnerStyle;

/**
 * Created by Wangsw on 2020/05/27
 * SmartRefresh 的 普通 上拉加载样式
 */
public class CommonSmartRefreshFooter extends FrameLayout implements RefreshFooter {


    private CommonProgressBar common_progress;

    public CommonSmartRefreshFooter(Context context) {
        this(context, null);
    }

    public CommonSmartRefreshFooter(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonSmartRefreshFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = View.inflate(getContext(), R.layout.view_common_refresh_footer, this);
        common_progress = view.findViewById(R.id.common_progress);

    }


    @Override
    public boolean setNoMoreData(boolean noMoreData) {
        return false;
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }

    @Override
    public void setPrimaryColors(int... colors) {

    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {
        Log.d("footer", "----onInitialized");
    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {
        Log.d("footer", "----onMoving");
    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        Log.d("footer", "----onReleased");
    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        Log.d("footer", "----onStartAnimator");
    }

    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        Log.d("footer", "----onFinish");
        return 0;
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        switch (newState) {
            case LoadFinish:
                common_progress.stopAnimator();
                break;
            case PullUpToLoad:
                common_progress.startAnimator();
                break;
            default:
                break;
        }
        Log.d("footer", "----onStateChanged ---" + newState.name());
    }
}
