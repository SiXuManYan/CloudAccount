package com.account.base.other

import com.account.base.common.BaseView
import com.account.base.net.BaseHttpSubscriber

abstract class BackgroundSubscriber<T>(private var view: BaseView) : BaseHttpSubscriber<T>(view) {




    override fun onError(e: Throwable) {

    }

    override fun onStart() {
        request(java.lang.Long.MAX_VALUE)
    }
}