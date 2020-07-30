package com.fatcloud.account.feature.message

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseJsonArrayHttpSubscriber
import com.fatcloud.account.entity.message.Message
import com.google.gson.JsonArray
import java.util.ArrayList
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/7/30 0030 16:00.
 * </br>
 *
 */
class MessagePresenter @Inject constructor(private var view: MessageView) : BasePresenter(view) {

    override fun loadList(lifecycle: LifecycleOwner, page: Int, pageSize: Int, lastItemId: String?) {
        requestApi(lifecycle, Lifecycle.Event.ON_DESTROY,
            apiService.getMessageList(pageSize, lastItemId),

            object : BaseJsonArrayHttpSubscriber<Message>(view, false) {

                override fun onSuccess(jsonArray: JsonArray?, list: ArrayList<Message>, lastItemId: String?) {
                    view.bindList(list, lastItemId)
                }
            }
        )

    }

}