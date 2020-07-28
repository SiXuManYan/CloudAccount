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
import com.fatcloud.account.R

/**
 * Created by Wangsw on 2020/7/28 0028 14:31.
 * </br>
 *
 */
object NotificationUtil {

    fun initCloudChannel(applicationContext: Context) {
        PushServiceFactory.init(applicationContext)
        val pushService: CloudPushService = PushServiceFactory.getCloudPushService()
        pushService.register(applicationContext, object : CommonCallback {
            override fun onSuccess(p0: String?) {
                Log.d("阿里云推送初始化", "init cloudchannel success ，deviceId ==>" + pushService.deviceId);

            }

            override fun onFailed(errorCode: String?, errorMessage: String?) {
                Log.d("阿里云推送初始化", "init cloudchannel failed -- errorcode:$errorCode -- errorMessage:$errorMessage");
            }
        })
    }



    /**
     * NotifyId 兼容。
     * 接口返回 notifyId 大于Int 最大值时，使用此ID
     */
    private var nativeNotifyId = 0



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
     * @param notifyId    通知类型（1.系统通知 2.顾客消费通知 3.收到的评论/赞/@ 4.打款成功通知 5.加入门店申请）
     */
    fun showNotification(context: Context,
                         intent: Intent,
                         channelId: String,
                         channelName: String,
                         title: String?,
                         contentText: String?,
                         notifyId: Long) {

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