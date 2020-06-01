package com.account.entity.users

import androidx.room.*
import com.blankj.utilcode.util.Utils
import com.account.app.CloudAccountApplication
import com.account.common.CommonUtils
import com.account.common.Constants
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

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
//            (Utils.getApp() as CloudAccountApplication).database.userDao().clear()
            CommonUtils.getShareDefault().remove(Constants.SP_LOGIN)
            CommonUtils.getShareDefault().remove(Constants.SP_NOVICE)
            instance = null
        }
    }

    @PrimaryKey
    var id: Long = 0

    @Ignore
    var yihuaId: Long? = null
        set(value) {
            value?.let {
                field = value
                this.id = value
            }
        }

    var phone: String? = null

    var name: String? = null

    /**
     * 用户性别,0-默认,1-男,2-女
     */
    var sex: Int? = null

    /**
     * 用户年龄,0-未知,1-六零后及其他,2-七零后,3-八零后,4-九零后,5-零零后
     */
    var age: Int? = null

    @ColumnInfo(name = "invite_code")
    var inviteCode: String? = null

    @ColumnInfo(name = "device_id")
    var deviceId: String? = null

    var address: String? = null

    /**
     * 用户状态,0-正常,1-测试审查,2-账户锁定,3-禁言,4-封号
     */
    var status: Int? = null


    @ColumnInfo(name = "push_code")
    var pushCode: String? = null

    @ColumnInfo(name = "login_code")
    var loginCode: String? = null

    @Ignore
    var logincode: String? = null


    /**
     * 用户类型,0-普通用户, 1-认证商家, 2-益划官方认证, 10-小益 11 达人
     */
    @ColumnInfo(name = "owner_level")
    var ownerLevel: Int? = null


    /** 用户头像 */
    var photo: String? = null

    /** 用户头像小图 */
    @ColumnInfo(name = "small_photo")
    var smallPhoto: String? = null

    /** 益划币余额 */
    @TypeConverters
    var money: BigDecimal? = null


    /**
     * 可提现金额
     */
    @ColumnInfo(name = "rechange_amount")
    var rechangeAmount: String? = null


    /** 密码MD5 */
    @Ignore
    var password: String? = null

    // 5.0 新增

    /** 是否绑定过微信 获取用户信息接口返回*/
    var wechatBindFlag: Boolean = false

    /** 微信昵称 */
    var wechatNickname: String? = ""

    /** 是否设置过密码 */
    var passwordSetFlag: Boolean = false

    /** 是否设置过头像 */
    var photoSetFlag: Boolean = false


    /**
     * 是否绑定过微信(登录接口返回)
     */
    var bindWxFlag: Boolean = false


}