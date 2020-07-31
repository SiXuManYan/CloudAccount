package com.fatcloud.account.common;

import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.fatcloud.account.R;

/**
 * Created by Administrator on 2017/9/14.
 */

public class PopupWindowUtil {

    /**
     * @param anchorView
     * @param resource
     * @return
     */
    public static PopupWindow showTipPopupWindow(final View anchorView, int resource) {

        final View contentView = LayoutInflater.from(anchorView.getContext()).inflate(resource, null);
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        final PopupWindow popupWindow = new PopupWindow(contentView, contentView.getMeasuredWidth(), contentView.getMeasuredHeight(), false);

        // 点击外部消失popwindow
        popupWindow.setOutsideTouchable(true);

        // 让键盘弹出时，不会挡住pop窗口。
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        /**
         * 如果不设置PopupWindow的背景，有些版本无论点击外部区域还是Back键都无法dismiss弹框
         */
        popupWindow.setBackgroundDrawable(new ColorDrawable());

        /**
         * 如果希望showAsDropDown方法能够在下面空间不足时自动在anchorView的上面弹出
         * 必须在创建PopupWindow的时候指定高度，不能用wrap_content
         */
        popupWindow.showAsDropDown(anchorView);

        return popupWindow;
    }




}
