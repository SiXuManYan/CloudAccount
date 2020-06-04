package com.fatcloud.account.entity.users

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fatcloud.account.app.CloudAccountApplication
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.blankj.utilcode.util.Utils

/**
 * 用户信息
 * @date 2018/11/11
 */
@Entity(tableName = "tb_user")
class User {

    companion object {
        private var instance: User? = null
            get() {
                if (field == null) {
                    field = (Utils.getApp() as CloudAccountApplication).database.userDao().findUser()
                    if (field == null) {
                        field = User()
                    }
                }
                return field
            }

        @Synchronized
        fun get() = instance!!

        fun isLogon(): Boolean {
            val isLogin = CommonUtils.getShareDefault().getBoolean(Constants.SP_LOGIN)
            if (isLogin && get().id == null) {
                clearAll()
                return false
            }
            return isLogin
        }

        fun update() {
            instance = null
        }

        fun clearAll() {
            (Utils.getApp() as CloudAccountApplication).database.userDao().clear()
            CommonUtils.getShareDefault().remove(Constants.SP_LOGIN)
            CommonUtils.getShareDefault().remove(Constants.SP_NOVICE)
            instance = null
        }
    }

    @PrimaryKey
    var id: Long = 0


/*

     "data" : {
        "token" : "ae00bd7586a34015ba628b04fa9ad158",
        "username" : "13200010001",
        "nickName" : "晌笆蹈悼绝",
        "headUrl" : "ios_20200526202947196_202001205.png"
      }

*/

    var token = ""

    /**
     * 用户名，手机号
     */
    @ColumnInfo(name = "user_name")
    var username = ""

    /**
     * 用户昵称
     */
    @ColumnInfo(name = "nick_name")
    var nickName = ""

    /**
     * 用户头像
     */
    @ColumnInfo(name = "head_url")
    var headUrl = ""




}