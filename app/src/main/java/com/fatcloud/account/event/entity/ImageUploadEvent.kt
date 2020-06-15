package com.fatcloud.account.event.entity

/**
 * Created by Wangsw on 2020/6/13 0013 19:12.
 * </br>
 *
 */
class ImageUploadEvent(val finalUrl: String, val isFaceUp: Boolean, val fromViewId: Int, val formWhichClass: Class<*>)