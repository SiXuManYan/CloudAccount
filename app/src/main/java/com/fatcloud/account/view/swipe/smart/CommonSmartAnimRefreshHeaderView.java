package com.fatcloud.account.view.swipe.smart;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;

import com.fatcloud.account.R;
import com.fatcloud.account.view.loading.CommonProgressBar;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshKernel;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.scwang.smart.refresh.layout.constant.SpinnerStyle;

/**
 * Created by Wangsw on 2020/05/27
 * SmartRefresh 的 通用 下拉刷新样式
 */
public class CommonSmartAnimRefreshHeaderView extends FrameLayout implements RefreshHeader {


    private CommonProgressBar common_progress;
    private TextView refresh_tv;
    private RelativeLayout parent_rl;




    public CommonSmartAnimRefreshHeaderView(Context context) {
        this(context, null);
    }

    public CommonSmartAnimRefreshHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonSmartAnimRefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        View view = View.inflate(getContext(), R.layout.view_common_refresh_header, this);
        common_progress = view.findViewById(R.id.common_progress);
        refresh_tv = view.findViewById(R.id.refresh_tv);
        parent_rl = view.findViewById(R.id.parent_rl);
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
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        refresh_tv.setText("加载完毕");
        return 500;
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        switch (newState) {
            case PullDownToRefresh:
                refresh_tv.setText("下拉刷新");
                break;
            case Refreshing:
                refresh_tv.setText("加载中...");
                common_progress.startAnimator();
                break;
            case ReleaseToRefresh:
                refresh_tv.setText("松开刷新");
                break;
            case RefreshFinish:
                common_progress.stopAnimator();
                break;
            default:
                break;
        }
    }


    @Override
    public void setPrimaryColors(int... colors) {

    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {

    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {

    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
    }


    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    /**
     * 更改背景颜色
     * @param id
     */
    public void setHeaderBackgroundColor(@ColorRes int id){
        parent_rl.setBackgroundColor(getResources().getColor(id));
    }

    /**
     * 更改
     * @param id
     */
    public void setRefreshTextColor(@ColorRes int id){
        refresh_tv.setTextColor(getResources().getColor(id));
    }


}
