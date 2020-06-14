package com.fatcloud.account.event.entity

import androidx.annotation.IdRes

/**
 * Created by Wangsw on 2020/6/13 0013 19:12.
 * </br>
 *
 */
class ImageUploadEvent(val finalUrl: String, val isFaceUp: Boolean,val  fromViewId: Int)