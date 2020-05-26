package com.account.view.swipe;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.aspsine.swipetoloadlayout.SwipeLoadMoreFooterLayout;
import com.blankj.utilcode.util.SizeUtils;
import com.account.R;

/**
 * Created by Aspsine on 2015/9/2.
 *
 * SwipeToLoadLayout 品牌 上拉加载样式
 */
public class BrandAnimLoadMoreFooterView extends SwipeLoadMoreFooterLayout {

    private ProgressBar progressBar;

    public BrandAnimLoadMoreFooterView(Context context) {
        this(context, null);
    }

    public BrandAnimLoadMoreFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BrandAnimLoadMoreFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        RelativeLayout relativeLayout = new RelativeLayout(getContext());
        addView(relativeLayout);
        relativeLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getResources().getDimensionPixelOffset(R.dimen.load_more_footer_height)));

        TextView loadMoreText = new TextView(getContext());
        loadMoreText.setId(com.aspsine.swipetoloadlayout.R.id.swipe_refresh_text);
        loadMoreText.setTextColor(ContextCompat.getColor(getContext(), R.color.colorFontGray));
        loadMoreText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        loadMoreText.setText(R.string.loading);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        loadMoreText.setLayoutParams(layoutParams);
        relativeLayout.addView(loadMoreText);

        progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleSmallInverse);
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        if (Build.VERSION.SDK_INT > 16) {
            layoutParams.addRule(RelativeLayout.START_OF, com.aspsine.swipetoloadlayout.R.id.swipe_refresh_text);
        } else {
            layoutParams.addRule(RelativeLayout.LEFT_OF, com.aspsine.swipetoloadlayout.R.id.swipe_refresh_text);
        }
        layoutParams.setMargins(0, 0, SizeUtils.dp2px(48f), 0);
        progressBar.setLayoutParams(layoutParams);
        progressBar.setIndeterminateDrawable(ContextCompat.getDrawable(getContext(), R.drawable.small_loading));
        relativeLayout.addView(progressBar);
    }

    @Override
    public void onPrepare() {
    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
        if (!isComplete) {
//            ivSuccess.setVisibility(GONE);
            progressBar.setVisibility(GONE);
//            if (-y >= mFooterHeight) {
//                loadMoreText.setText("RELEASE TO LOAD MORE");
//            } else {
//                loadMoreText.setText("SWIPE TO LOAD MORE");
//            }
        }
    }

    @Override
    public void onLoadMore() {
        progressBar.setVisibility(VISIBLE);
    }

    @Override
    public void onRelease() {

    }

    @Override
    public void onComplete() {
        progressBar.setVisibility(GONE);
//        ivSuccess.setVisibility(VISIBLE);
    }

    @Override
    public void onReset() {
    }
}
