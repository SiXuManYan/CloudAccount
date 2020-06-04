package com.fatcloud.account.data

import androidx.room.TypeConverter
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
    fun textToBigDecimal(value: String) : BigDecimal {
        return if (value.isEmpty()) {
            BigDecimal.ZERO
        } else {
            BigDecimal(value)
        }
    }
}