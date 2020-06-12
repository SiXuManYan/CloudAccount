package com.fatcloud.account.common

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

}