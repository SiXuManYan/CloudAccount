package com.account.view.swipe;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorRes;

import com.aspsine.swipetoloadlayout.SwipeRefreshHeaderLayout;
import com.blankj.utilcode.util.ColorUtils;
import com.account.R;
import com.account.view.loading.CommonProgressBar;

/**
 * SwipeToLoadLayout  下拉刷新头
 *
 * @date 2019/2/21
 */
public class AnimRefreshHeaderView extends SwipeRefreshHeaderLayout {

    private static final String TAG = "AnimRefreshHeaderView";

    private CommonProgressBar common_progress;
    private TextView refresh_tv;
    private RelativeLayout parent_rl;

    private HeaderListener headerListener;


    public AnimRefreshHeaderView(Context context) {
        this(context, null);
    }

    public AnimRefreshHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimRefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setHeaderListener(HeaderListener headerListener) {
        this.headerListener = headerListener;
    }

    private void init() {
        View view = View.inflate(getContext(), R.layout.view_common_refresh_header, this);
        common_progress = view.findViewById(R.id.common_progress);
        refresh_tv = view.findViewById(R.id.refresh_tv);
        parent_rl = view.findViewById(R.id.parent_rl);
    }


    @Override
    public void onRefresh() {
        common_progress.startAnimator();
        refresh_tv.setText("加载中...");

        Log.d(TAG, "onRefresh: ");
    }

    @Override
    public void onPrepare() {
        refresh_tv.setText("下拉刷新");
        if (headerListener != null) {
            headerListener.onPrepare();
        }
        Log.d(TAG, "onPrepare: ");
    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {

        if (y < getMeasuredHeight()) {
            refresh_tv.setText("下拉刷新");
        } else {
            refresh_tv.setText("松开刷新");
        }
    }

    @Override
    public void onRelease() {
        Log.d(TAG, "onRelease: ");
    }

    @Override
    public void onComplete() {

        refresh_tv.setText("加载完毕");
        Log.d(TAG, "onComplete: ");
    }

    @Override
    public void onReset() {
        if (headerListener != null) {
            headerListener.onReset();
        }
        common_progress.stopAnimator();
        Log.d(TAG, "onReset: ");
    }

    public TextView getSloganText() {
        return refresh_tv;
    }

    public TextView getRefreshText() {
        return refresh_tv;
    }

    public void setContainerBackgroundColor(@ColorRes int colorResId) {
        parent_rl.setBackgroundColor(ColorUtils.getColor(colorResId));
    }


    public interface HeaderListener {
        void onPrepare();

        void onReset();
    }
}
