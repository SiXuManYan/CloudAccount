package com.fatcloud.account.backstage

import android.content.Context
import android.text.TextUtils
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.fatcloud.account.app.CloudAccountApplication
import com.fatcloud.account.base.common.BaseNoDaggerPresenter
import com.fatcloud.account.base.net.BaseJsonArrayHttpSubscriber
import com.fatcloud.account.data.CloudDataBase
import com.fatcloud.account.entity.news.NewsCategory
import com.blankj.utilcode.util.Utils
import com.fatcloud.account.BuildConfig
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.fatcloud.account.entity.upgrade.Upgrade
import com.fatcloud.account.event.RxBus
import com.google.gson.JsonArray
import io.reactivex.Flowable
import java.math.BigDecimal
import java.util.*

class DataWorkPresenter constructor(private var serviceView: ServiceView) : BaseNoDaggerPresenter(serviceView) {


    val database: CloudDataBase by lazy { (Utils.getApp() as CloudAccountApplication).database }
    val locationClient: AMapLocationClient by lazy { aMapLocationClientProvider(Utils.getApp(), serviceView) }

    private fun aMapLocationClientProvider(context: Context, service: ServiceView): AMapLocationClient {

        // 配置参数 单次定位或连续定位
        val option = AMapLocationClientOption().apply {
            //地址信息
            isNeedAddress = true
            //低功耗(不会使用GPS和其他传感器，只会使用网络定位（Wi-Fi和基站定位）；)
            locationMode = AMapLocationClientOption.AMapLocationMode.Battery_Saving
            // 设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
            interval = Constants.LOCATION_INTERVAL

            // 设置是否允许模拟位置,默认为true，允许模拟位置
            isMockEnable = false

        }


        val locationClient = AMapLocationClient(context).apply {
            // 给定位客户端对象设置定位参数
            setLocationOption(option)
            // 设置定位回调监听
            setLocationListener(service)
        }
        return locationClient
    }


    /**
     * 开启定位
     */
    fun startLocation() {
        if (locationClient.isStarted) {
            stopLocation()
        }
        locationClient.startLocation()
    }

    /**
     * 关闭定位
     */
    fun stopLocation() = locationClient.stopLocation()

    override fun detachView() {
        stopLocation()
        locationClient.onDestroy()
        super.detachView()
    }


    /**
     * 获取资讯分类并存储db
     */
    fun getNewsCategoryToDataBase() {
        addSubscribe(
            apiService.getNewsCategory()
                .compose(flowableUICompose())
                .subscribeWith(object : BaseJsonArrayHttpSubscriber<NewsCategory>(serviceView) {
                    override fun onSuccess(jsonArray: JsonArray?, list: ArrayList<NewsCategory>, lastItemId: String?) {
                        if (list.isNotEmpty()) database.newsCategoryDao().addAllServiceSubTypes(list)
                    }
                })
        )
    }

    /**
     * 保存定位数据
     * @param location 定位数据
     */
    fun savePosition(location: AMapLocation) {
        if (!location.city.isNullOrEmpty()) {
            addSubscribe(
                Flowable.just(location)
                    .compose(flowableCompose())
                    .subscribe {
                        val spUtils = CommonUtils.getShareLocation()
                        if (it != null) {

                            spUtils.put(Constants.SP_LOCAL_CODE, it.cityCode)
                            spUtils.put(Constants.SP_LOCAL_NAME, it.city)
                            spUtils.put(Constants.SP_LONGITUDE, location.longitude.toString())
                            spUtils.put(Constants.SP_LATITUDE, location.latitude.toString())
                            spUtils.put(Constants.SP_ADDRESS, location.address)

                            RxBus.post(location)
                        }
                    })
        }
    }


}