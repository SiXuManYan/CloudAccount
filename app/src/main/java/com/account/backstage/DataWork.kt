package com.account.backstage

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.account.common.Constants


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
            else -> {

            }

        }
        return Result.success()
    }



}