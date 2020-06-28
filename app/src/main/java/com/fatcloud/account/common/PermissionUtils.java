package com.fatcloud.account.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;

import com.tbruyelle.rxpermissions2.RxPermissions;

/**
 * Created by Wangsw on 2020/6/28 0028 13:29.
 * </br>
 */
public class PermissionUtils {


    @SuppressLint("CheckResult")
    public static void permissionAny(Context context, @NonNull OnPermissionCallBack onPermissionCallBack, String... permissions) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onPermissionCallBack.onPermissionCallBack(true);
        } else {
            if (context == null) {
                return;
            }
            new RxPermissions((Activity) context).request(permissions).subscribe(onPermissionCallBack::onPermissionCallBack);
        }


    }

    public interface OnPermissionCallBack {
        public void onPermissionCallBack(boolean granted);
    }

}
