package com.fatcloud.account.feature.my

import android.net.Uri
import com.fatcloud.account.base.common.BaseTaskView

interface MyPageView :BaseTaskView{
    fun updateAvatarAndNicknameSuccess()
    fun updateMessageUnReadNumber(messageUnReadNumber: Long)
    fun onShootingPermissionResult(uri: Uri)
    fun loginOutSuccess()

}
