package com.account.extend

import com.google.gson.reflect.TypeToken
import kotlin.reflect.KProperty0

inline fun <reified T> genericType() = object : TypeToken<T>() {}.type!!

