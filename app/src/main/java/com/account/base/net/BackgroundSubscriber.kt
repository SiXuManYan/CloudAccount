package com.account.base.net

import com.account.base.common.BaseView

abstract class BackgroundSubscriber<T>(private var view: BaseView) : BaseHttpSubscriber<T>(view) {




    override fun onError(e: Throwable) {

    }

    override fun onStart() {
        request(java.lang.Long.MAX_VALUE)
    }
}