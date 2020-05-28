package com.account.base.net

import com.account.base.common.BaseView
import com.account.network.Response
import com.google.gson.JsonArray
import java.lang.reflect.ParameterizedType
import java.util.*

abstract class BaseJsonArrayHttpSubscriber<T>(view: BaseView, showLoading: Boolean = true) : BaseHttpSubscriber<JsonArray>(view, showLoading) {

    override fun onNext(response: Response<JsonArray>) {
        if (!response.isApiError()) {
            val type = (this::class.java.genericSuperclass as ParameterizedType).actualTypeArguments[0]
            val jsonArray = response.data
            val list = ArrayList<T>()
            try {
                jsonArray?.let {
                    for (element in it) {
                        list.add(gson.fromJson(element, type))
                    }
                }

            } catch (e: Exception) {
                onError(e)
                return
            }

            onSuccess(jsonArray, list)
        }
    }

    override fun onSuccess(data: JsonArray?) {
    }

    abstract fun onSuccess(jsonObject: JsonArray?, list: ArrayList<T>)
}