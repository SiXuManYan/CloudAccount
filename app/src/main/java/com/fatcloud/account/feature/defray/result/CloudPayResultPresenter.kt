package com.fatcloud.account.feature.defray.result

import android.widget.TextView
import com.fatcloud.account.R
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.common.Constants
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/6/16 0016 20:58.
 * </br>
 *
 */
class CloudPayResultPresenter @Inject constructor(private var view: CloudPayResultView) : BasePresenter(view) {



    /**
     * 倒计时
     * @param countdownView 倒计时显示的view
     * @param default       默认显示的文字
     */
    fun countdown(countdownView: TextView) {
        addSubscribe(
            Flowable.intervalRange(0, Constants.WAIT_DELAYS + 1L, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    countdownView.text = appContext.getString(R.string.count_down_format, 2 - it)
                }
                .doOnComplete {
                    view.countdownComplete()
                }
                .subscribe())
    }


}