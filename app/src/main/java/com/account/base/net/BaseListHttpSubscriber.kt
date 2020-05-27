package com.account.base.net

import com.account.base.common.BaseView
import com.account.network.Response
import com.google.gson.JsonObject
import java.lang.reflect.ParameterizedType
import java.util.*

abstract class BaseListHttpSubscriber<T>(private var key: String?, view: BaseView, showLoading: Boolean = true) : BaseHttpSubscriber<JsonObject>(view, showLoading) {

    override fun onNext(response: Response<JsonObject>) {
        if (!response.isApiError()) {
            val jsonObject = response.data
            if (jsonObject?.has(key)!!) {
                val type = (this::class.java.genericSuperclass as ParameterizedType).actualTypeArguments[0]
                val jsonArray = if (key.isNullOrEmpty()) response.data?.asJsonArray else response.data?.getAsJsonArray(key)
                val list = ArrayList<T>()
                try {
                    for (element in jsonArray!!) {
                        list.add(gson.fromJson(element, type))
                    }
                } catch (e: Exception) {
                    onError(e)
                    return
                }

                onSuccess(jsonObject, list)
            } else {
                onSuccess(jsonObject, ArrayList())
            }
        }
    }

    override fun onSuccess(data: JsonObject?) {
    }

    abstract fun onSuccess(jsonObject: JsonObject, list: ArrayList<T>)
}