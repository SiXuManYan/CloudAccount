package com.account.view.swipe;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.aspsine.swipetoloadlayout.SwipeRefreshHeaderLayout;
import com.blankj.utilcode.util.SizeUtils;
import com.account.R;

/**
 * SwipeToLoadLayout 品牌 下拉刷新头
 *
 * @date 2019/2/21
 */
public class BrandAnimRefreshHeaderView extends SwipeRefreshHeaderLayout {

    private ImageView animImage;
    private TextView refreshText;

    private AnimationDrawable animationDrawable;

    private HeaderListener headerListener;

    private TextView sloganText;

    public BrandAnimRefreshHeaderView(Context context) {
        this(context, null);
    }

    public BrandAnimRefreshHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BrandAnimRefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setHeaderListener(HeaderListener headerListener){
        this.headerListener=headerListener;
    }

    private void init() {
        RelativeLayout relativeLayout = new RelativeLayout(getContext());
        addView(relativeLayout);
        relativeLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        LinearLayout linearLayout = new LinearLayout(getContext());
        relativeLayout.addView(linearLayout);
        linearLayout.setPadding(0, SizeUtils.dp2px(10f), 0, 0);
        linearLayout.setId(R.id.swipe_refresh_header);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        linearLayout.setLayoutParams(layoutParams);

        sloganText = new TextView(getContext());
        linearLayout.addView(sloganText);
        sloganText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        sloganText.setText(R.string.slogan);
        sloganText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        sloganText.setTextColor(ContextCompat.getColor(getContext(), R.color.colorFontGray));

        refreshText = new TextView(getContext());
        linearLayout.addView(refreshText);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.topMargin = SizeUtils.dp2px(2f);
        refreshText.setLayoutParams(params);
        refreshText.setText("下拉刷新");
        refreshText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        refreshText.setTextColor(ContextCompat.getColor(getContext(), R.color.colorFontHint));

        View alignView = new View(getContext());
        relativeLayout.addView(alignView);
        layoutParams = new RelativeLayout.LayoutParams(1, 1);
        alignView.setLayoutParams(layoutParams);
        layoutParams.addRule(RelativeLayout.ALIGN_LEFT, R.id.swipe_refresh_header);
        alignView.setId(R.id.swipe_refresh_align);

        animImage = new ImageView(getContext());
        relativeLayout.addView(animImage);
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = SizeUtils.dp2px(10f);
        layoutParams.rightMargin = SizeUtils.dp2px(10f);
        layoutParams.addRule(RelativeLayout.LEFT_OF, R.id.swipe_refresh_align);
        animImage.setLayoutParams(layoutParams);
        animImage.setBackgroundResource(R.drawable.swipe_loading);
        animationDrawable = (AnimationDrawable) animImage.getBackground();
    }

    @Override
    public void onRefresh() {
//        animationDrawable.start();
        refreshText.setText("加载中...");
    }

    @Override
    public void onPrepare() {
        Log.d("TwitterRefreshHeader", "onPrepare()" + refreshText.getVisibility());
//        animImage.clearAnimation();
        animationDrawable.start();
        refreshText.setText("下拉刷新");
        if(headerListener!=null){
            headerListener.onPrepare();
        }
    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            refreshText.setText("松开刷新");
        }
    }

    @Override
    public void onRelease() {
        Log.d("TwitterRefreshHeader", "onRelease()");
        if (!animationDrawable.isRunning()) {
            animationDrawable.start();
        }

    }

    @Override
    public void onComplete() {
        refreshText.setText("加载完毕");
    }

    @Override
    public void onReset() {
        Log.d("TwitterRefreshHeader", "onReset()");
        animationDrawable.stop();
        animImage.clearAnimation();
        if(headerListener!=null){
            headerListener.onReset();
        }
    }

    public TextView getSloganText() {
        return sloganText;
    }

    public TextView getRefreshText() {
        return refreshText;
    }

    public interface HeaderListener {
        void onPrepare();

        void onReset();
    }
}
