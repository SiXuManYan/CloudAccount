package com.fatcloud.account.backstage

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.blankj.utilcode.util.ToastUtils
import com.fatcloud.account.common.Constants


class DataWork(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams), ServiceView {

    val presenter = DataWorkPresenter(this)


    override fun showError(code: Int, message: String) {
    }


    override fun doWork(): Result {

        when (inputData.getInt(Constants.ACTION_DATA_WORK, 0)) {
            // 处理具体耗时逻辑
            Constants.ACTION_SYNC -> {
                presenter.getNewsCategoryToDataBase()

            }

            Constants.ACTION_START_LOCATION -> {
                ToastUtils.showShort("高德")
            }

            else -> {

            }

        }
        return Result.success()
    }


}