package com.fatcloud.account.pushs

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.alibaba.sdk.android.push.CloudPushService
import com.alibaba.sdk.android.push.CommonCallback
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory
import com.blankj.utilcode.util.LogUtils
import com.fatcloud.account.R
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.fatcloud.account.entity.users.User
import com.fatcloud.account.feature.account.login.LoginActivity

/**
 * Created by Wangsw on 2020/7/28 0028 14:31.
 * </br>
 *
 */
object NotificationUtil {


    /**
     * NotifyId 兼容。
     * 接口返回 notifyId 大于Int 最大值时，使用此ID
     */
    private var nativeNotifyId = 0


    private const val CHANNEL_ID_SYSTEM = "channel_id_system"
    private const val CHANNEL_NAME_SYSTEM = "系统消息通知"

    private const val CHANNEL_ID_INTERACTION = "CHANNEL_ID_INTERACTION"
    private const val CHANNEL_NAME_INTERACTION = "社区互动通知"

    private const val CHANNEL_ID_OTHER = "channel_id_other"
    private const val CHANNEL_NAME_OTHER = "其他通知"


    fun initCloudChannel(applicationContext: Context) {

        try {

            PushServiceFactory.init(applicationContext) // crash
            val pushService: CloudPushService = PushServiceFactory.getCloudPushService()
            pushService.register(applicationContext, object : CommonCallback {
                override fun onSuccess(p0: String?) {
                    val deviceId = pushService.deviceId
                    LogUtils.d("阿里云推送初始化", "init cloudchannel success ，deviceId ==>" + deviceId)
                    CommonUtils.getShareDefault().put(Constants.SP_PUSH_DEVICE_ID, deviceId)
                }

                override fun onFailed(errorCode: String?, errorMessage: String?) {
                    LogUtils.d("阿里云推送初始化", "init cloudchannel failed -- errorcode:$errorCode -- errorMessage:$errorMessage");
                }
            })

        } catch (e: Exception) {

            LogUtils.d("阿里云推送初始化", "crash == " + e.printStackTrace());

        }


    }



    fun handlePush(context: Context, forPenetratePush: Boolean) {

        var notifyTitle: String? = ""
        var notifyMessage: String? = ""
        var notifyId: Long = 0
        var notifyIntent: Intent? = null

        var defChannelId = CHANNEL_ID_OTHER
        var defChannelName = CHANNEL_NAME_OTHER

        try {
//            val jsonObject = JSONObject(model.data)
//            val gson = Gson()
//            val notificationModel = gson.fromJson<NotificationModel>(jsonObject.toString(), NotificationModel::class.java) ?: return

//            notifyId = notificationModel.id
//            val dataid = notificationModel.dataid
//
//            notifyTitle = notificationModel.title
//            notifyMessage = notificationModel.message
//
//            when (notificationModel.type) {
//                2 -> {
//
//                    notifyIntent = Intent(context,ScheduleActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                }
//
//                else -> {
//                }
//            }
            if (!User.isLogon()) {
                context.startActivity(Intent(context.applicationContext, LoginActivity::class.java))
                return
            }

            notifyIntent?.let {

                if (forPenetratePush) {
                    // 处理透传消息，直接跳转至目标页面
                    context.startActivity(it)
                } else {
                    showNotification(context, it, defChannelId, defChannelName, notifyTitle, notifyMessage, notifyId)
                }
            }
        } catch (e: Exception) {
            Log.e("getui", "json 解析失败 exception :$e")
        }
    }



    /**
     * 发送通知
     * 8.0以上会创建通知渠道
     *
     * @param context
     * @param intent
     * @param channelId   通知渠道id
     * @param channelName 通知渠道说明（用户可见）
     * @param title
     * @param contentText
     * @param notifyId    通知类型（1.系统Device type does not match with app type通知 2.顾客消费通知 3.收到的评论/赞/@ 4.打款成功通知 5.加入门店申请）
     */
    fun showNotification(
        context: Context,
        intent: Intent,
        channelId: String,
        channelName: String,
        title: String?,
        contentText: String?,
        notifyId: Long
    ) {

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder: NotificationCompat.Builder

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // 创建通知渠道
            builder = NotificationCompat.Builder(context, channelId)

            // 在这里可以设置 channel 的重要程度，(如:拿channelId来衡量)
            val defImportance = NotificationManager.IMPORTANCE_DEFAULT
            createNotificationChannel(context, channelId, channelName, defImportance)
        } else {
            builder = NotificationCompat.Builder(context, "")
        }

        builder.setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_VIBRATE)


        // 默认配置
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)

        val notification = builder.setContentTitle(title)
            .setStyle(NotificationCompat.BigTextStyle())
            .setContentText(contentText)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.ic_logo_notification)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_logo_round))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()


        val notificationId = if (notifyId > Int.MAX_VALUE) {
            nativeNotifyId++
        } else {
            notifyId.toInt()
        }
        manager.notify(notificationId, notification)
    }


    /**
     * 创建通知渠道
     *
     * @param context
     * @param channelId   渠道id
     * @param channelName 渠道描述(用户可见)
     * @param importance  重要性
     */
    @TargetApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(context: Context, channelId: String, channelName: String, importance: Int) {

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(channelId, channelName, importance)
        manager.createNotificationChannel(channel)
    }


}