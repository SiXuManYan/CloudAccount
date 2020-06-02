package com.account.entity.users

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.account.app.CloudAccountApplication
import com.account.common.CommonUtils
import com.account.common.Constants
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

    "data": {
        "token": "2ecd511fb1f143ad922679e73e08fc18",
        "username": "17640339671",
        "nickName": "Fta:笔张"
    }

*/

    var token = ""

    @ColumnInfo(name = "user_name")
    var username = ""

    @ColumnInfo(name = "nick_name")
    var nickName = ""




}