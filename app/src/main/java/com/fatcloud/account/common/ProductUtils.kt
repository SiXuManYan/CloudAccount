package com.fatcloud.account.common

import com.blankj.utilcode.util.VibrateUtils
import com.fatcloud.account.view.EditView

/**
 * Created by Wangsw on 2020/6/12 0012 17:29.
 * </br>
 * 产品相关逻辑处理
 */
object ProductUtils {



    /**
     * 处理经营范围pid
     *  ArrayList<String> ->  ArrayList<Int>
     */
    fun stringList2IntList(selectPid: ArrayList<String>): ArrayList<Int> {
        val scope: ArrayList<Int> = ArrayList()
        selectPid.forEach {
            scope.add(it.toInt())
        }
        return scope
    }


    /**
     * 非空校验，提供震动和抖动反馈
     */
     fun checkEditEmptyWithVibrate(vararg args: EditView) {
        VibrateUtils.vibrate(10)
        args.forEach {
            it.value().isEmpty()
            it.startAnimation(CommonUtils.getShakeAnimation(2))
            return@forEach
        }

    }


}