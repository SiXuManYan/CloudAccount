package com.fatcloud.account.backstage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.fatcloud.account.common.Constants
import com.fatcloud.account.entity.upgrade.Upgrade
import com.fatcloud.account.feature.upgrade.UpgradeActivity


class DataWork(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams), ServiceView {

    val presenter = DataWorkPresenter(this)


    override fun showError(code: Int, message: String) {
    }


    override fun doWork(): Result {

        when (inputData.getInt(Constants.ACTION_DATA_WORK, 0)) {
            // 处理具体耗时逻辑
            Constants.ACTION_SYNC -> {
                presenter.getNewsCategoryToDataBase()
                presenter.checkAppVersion()

            }


            else -> {

            }

        }
        return Result.success()
    }

    override fun doAppUpgrade(data: Upgrade) {
        applicationContext.startActivity(
            Intent(applicationContext, UpgradeActivity::class.java),
            Bundle().apply {
                putSerializable(Constants.PARAM_DATA, data)
            })
    }


}