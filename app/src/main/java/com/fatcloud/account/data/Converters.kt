package com.fatcloud.account.data

import androidx.room.TypeConverter
import com.fatcloud.account.entity.order.IdentityImg
import com.fatcloud.account.entity.order.enterprise.EnterpriseInfo
import com.fatcloud.account.entity.order.enterprise.Shareholder
import com.fatcloud.account.entity.order.persional.NamePhoneBean
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
    @JvmStatic
    fun fromString(value: String?): ArrayList<String?>? {
        val listType: Type = object : TypeToken<ArrayList<String?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    @JvmStatic
    fun fromArrayList(list: ArrayList<String?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }


    @TypeConverter
    @JvmStatic
    fun listToJson(value: List<EnterpriseInfo>?): String {

        return Gson().toJson(value)
    }

    @TypeConverter
    @JvmStatic
    fun jsonToList(value: String): List<EnterpriseInfo>? {

        val objects =
            Gson().fromJson(value, Array<EnterpriseInfo>::class.java) as Array<EnterpriseInfo>
        val list = objects.toList()
        return list
    }


    @TypeConverter
    @JvmStatic
    fun listToJson2(value: List<IdentityImg>?): String {

        return Gson().toJson(value)
    }

    @TypeConverter
    @JvmStatic
    fun jsonToList2(value: String): List<IdentityImg>? {

        val objects =
            Gson().fromJson(value, Array<IdentityImg>::class.java) as Array<IdentityImg>
        val list = objects.toList()
        return list
    }

    @TypeConverter
    @JvmStatic
    fun listToJson3(value: List<Shareholder>?): String {

        return Gson().toJson(value)
    }

    @TypeConverter
    @JvmStatic
    fun jsonToList3(value: String): List<Shareholder>? {

        val objects =
            Gson().fromJson(value, Array<Shareholder>::class.java) as Array<Shareholder>
        val list = objects.toList()
        return list
    }


    @TypeConverter
    @JvmStatic
    fun listToJson4(value: List<NamePhoneBean>?): String {

        return Gson().toJson(value)
    }

    @TypeConverter
    @JvmStatic
    fun jsonToList4(value: String): List<NamePhoneBean>? {

        val objects =
            Gson().fromJson(value, Array<NamePhoneBean>::class.java) as Array<NamePhoneBean>
        val list = objects.toList()
        return list
    }


}