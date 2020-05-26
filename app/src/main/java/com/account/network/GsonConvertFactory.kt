package com.jz.yihua.activity.network

import com.account.network.ApiException
import com.account.network.Response
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okio.Buffer
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.ByteArrayInputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.reflect.Type

class GsonConvertFactory private constructor(private var gson: Gson): Converter.Factory() {

    companion object {
        fun create() = GsonConvertFactory(Gson())
    }

    override fun responseBodyConverter(type: Type?,
                                       annotations: Array<out Annotation>?,
                                       retrofit: Retrofit?): Converter<ResponseBody, *> {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return GsonResponseBodyConverter(gson, adapter)
    }

    override fun requestBodyConverter(type: Type?,
                                      parameterAnnotations: Array<out Annotation>?,
                                      methodAnnotations: Array<out Annotation>?,
                                      retrofit: Retrofit?): Converter<*, RequestBody> {
        return GsonRequestBodyConverter(gson, gson.getAdapter(TypeToken.get(type)))
    }

    private class GsonRequestBodyConverter<T>(private var gson: Gson, private var adapter: TypeAdapter<T>): Converter<T, RequestBody> {

        companion object {
            @JvmField
            internal val MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8")
        }

        override fun convert(value: T): RequestBody {
            val buffer = Buffer()
            val writer = OutputStreamWriter(buffer.outputStream(), Charsets.UTF_8)
            val jsonWriter = gson.newJsonWriter(writer)
            adapter.write(jsonWriter, value)
            jsonWriter.close()
            return RequestBody.create(MEDIA_TYPE, buffer.readByteString())
        }
    }

    private class GsonResponseBodyConverter<T>(private var gson: Gson, private var adapter: TypeAdapter<T>): Converter<ResponseBody, T> {

        override fun convert(value: ResponseBody) : T {
            val source = value.source()
            source.request(Long.MAX_VALUE)
            val buffer = source.buffer().clone()

            val response = gson.fromJson(buffer.readUtf8(), Response::class.java)
            if (response.isApiError()) {
                value.close()
                throw ApiException(
                        response.code ?: response.resultCode!!.toInt(),
                        response.message,
                        response.data as LinkedTreeMap<String, String>?
                )
            }
            val inputStream = ByteArrayInputStream(source.readByteArray())
            val jsonReader = gson.newJsonReader(InputStreamReader(inputStream, Charsets.UTF_8))
            value.use {
                return adapter.read(jsonReader)
            }
        }
    }
}