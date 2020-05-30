package com.account.base.net

import com.account.base.common.BaseView
import com.account.network.Response
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import java.lang.reflect.ParameterizedType
import java.util.*

abstract class BaseListHttpSubscriber<T>(private var key: String?, view: BaseView, showLoading: Boolean = true) :
    BaseHttpSubscriber<JsonObject>(view, showLoading) {

    override fun onNext(response: Response<JsonObject>) {
        if (response.isApiError()) {
            return
        }

        val list = ArrayList<T>()
        var lastItemId: String? = ""

        val jsonObject = response.data
        if (!jsonObject?.has(key)!!) {
            onSuccess(jsonObject, list, lastItemId)
            return
        }

        val type = (this::class.java.genericSuperclass as ParameterizedType).actualTypeArguments[0]
        val jsonArray = if (key.isNullOrEmpty()) response.data?.asJsonArray else response.data?.getAsJsonArray(key)


        try {
            jsonArray?.let {

                it.forEachIndexed { index, element ->
                    list.add(gson.fromJson(element, type))

                    // 获取最后一项item id
                    if (index == it.size() - 1) {
                        if (element is JsonObject && element.has("id")) {
                            lastItemId = element.get("id").toString()
                        }
                    }

                }

            }


        } catch (e: Exception) {
            onError(e)
            return
        }

        onSuccess(jsonObject, list, lastItemId)

    }

    override fun onSuccess(data: JsonObject?) {
    }

    abstract fun onSuccess(jsonObject: JsonObject, list: ArrayList<T>, lastItemId: String?)


}