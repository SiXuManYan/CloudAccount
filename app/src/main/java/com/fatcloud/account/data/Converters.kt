package com.fatcloud.account.data

import androidx.room.TypeConverter
import com.fatcloud.account.entity.order.enterprise.EnterpriseInfo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.math.BigDecimal


object Converters {

    @TypeConverter
    @JvmStatic
    fun bigDecimalToText(value: BigDecimal?): String {
        value?.let {
            return value.toPlainString()
        }
        return ""
    }

    @TypeConverter
    @JvmStatic
    fun textToBigDecimal(value: String): BigDecimal {
        return if (value.isEmpty()) {
            BigDecimal.ZERO
        } else {
            BigDecimal(value)
        }
    }

    @TypeConverter
    fun fromString(value: String?): ArrayList<String?>? {
        val listType: Type = object : TypeToken<ArrayList<String?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<String?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun listToJson(value: List<EnterpriseInfo>?): String {

        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): List<EnterpriseInfo>? {

        val objects =
            Gson().fromJson(value, Array<EnterpriseInfo>::class.java) as Array<EnterpriseInfo>
        val list = objects.toList()
        return list
    }


}