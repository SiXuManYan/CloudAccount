package com.fatcloud.account.backstage

import android.content.Context
import androidx.work.*
import com.blankj.utilcode.util.LogUtils
import com.fatcloud.account.common.Constants
import com.blankj.utilcode.util.Utils
import java.lang.Exception

class DataServiceFaker {

    companion object {

        fun startService(context: Context, action: Int) {
            try {
                startService(context, action, null)
            } catch (e: Exception) {
                LogUtils.d("DataServiceFaker初始化", "crash == " + e.printStackTrace());
            }
        }

        /**
         * 通过 WorkManager 执行类似Service的耗时操作
         * @see WorkManager
         * @see Worker
         */
        fun startService(context: Context, action: Int, data: Data? = null) {
            WorkManager.getInstance(Utils.getApp())
                .enqueue(
                    OneTimeWorkRequest.Builder(DataWork::class.java)
                        .setInputData(Data.Builder().putInt(Constants.ACTION_DATA_WORK, action).apply {
                            data?.let {
                                putAll(it)
                            }
                        }.build()).build()
                )
        }
    }
}