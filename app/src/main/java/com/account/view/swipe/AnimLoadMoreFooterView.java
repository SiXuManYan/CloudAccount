package com.account.view.swipe;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.ColorRes;

import com.aspsine.swipetoloadlayout.SwipeLoadMoreFooterLayout;
import com.blankj.utilcode.util.ColorUtils;
import com.account.R;
import com.account.view.loading.CommonProgressBar;


/**
 * Created on 2019/10/11.
 * SwipeToLoadLayout 通用 上拉加载样式
 */
public class AnimLoadMoreFooterView extends SwipeLoadMoreFooterLayout {

    private static final String TAG = "AnimLoadMoreFooterView";

    private CommonProgressBar common_progress;
    private RelativeLayout refresh_footer_container_rl;

    public AnimLoadMoreFooterView(Context context) {
        this(context, null);
    }

    public AnimLoadMoreFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimLoadMoreFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = View.inflate(getContext(), R.layout.view_common_refresh_footer, this);
        common_progress = view.findViewById(R.id.common_progress);
        refresh_footer_container_rl = view.findViewById(R.id.refresh_footer_container_rl);
    }

    @Override
    public void onPrepare() {
    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {

    }

    @Override
    public void onLoadMore() {
        common_progress.startAnimator();
    }

    @Override
    public void onRelease() {
    }

    @Override
    public void onComplete() {
    }

    @Override
    public void onReset() {
        common_progress.stopAnimator();
    }

    public void startAnimator() {
        common_progress.startAnimator();
    }

    public void setContainerBackgroundColor(@ColorRes int colorResId) {
        refresh_footer_container_rl.setBackgroundColor(ColorUtils.getColor(colorResId));
    }

}
