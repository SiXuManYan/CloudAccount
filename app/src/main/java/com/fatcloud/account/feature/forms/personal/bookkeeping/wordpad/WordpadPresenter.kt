package com.fatcloud.account.feature.forms.personal.bookkeeping.wordpad

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.alibaba.sdk.android.oss.*
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback
import com.alibaba.sdk.android.oss.common.OSSLog
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider
import com.alibaba.sdk.android.oss.model.PutObjectRequest
import com.alibaba.sdk.android.oss.model.PutObjectResult
import com.blankj.utilcode.util.StringUtils
import com.fatcloud.account.BuildConfig
import com.fatcloud.account.R
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.entity.oss.SecurityTokenModel
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/6/13 0013 18:27.
 * </br>
 *
 */
class WordpadPresenter @Inject constructor(private var view: WordpadView) : BasePresenter(view) {


    fun getOssSecurityToken(lifecycleOwner: LifecycleOwner, byteArray: ByteArray) {

        requestApi(lifecycleOwner, Lifecycle.Event.ON_DESTROY,
            apiService.getOssSecurityToken(),
            object : BaseHttpSubscriber<SecurityTokenModel>(view) {
                override fun onSuccess(data: SecurityTokenModel?) {
                    data?.let {


                        uploadResources(it, false,byteArray)
                    }

                }
            })
    }


    /**
     * @see {@see https://github.com/aliyun/aliyun-oss-android-sdk/blob/master/README-CN.md}
     * @param isEncryptFile 是否为加密上传
     */
    fun uploadResources(stsModel: SecurityTokenModel, isEncryptFile: Boolean, byteArray: ByteArray) {

        // 节点
        val endpoint = BuildConfig.OSS_END_POINT

        // bucketName
        val imageBucketName = if (isEncryptFile) {
            stsModel.AccessBucketName
        } else {
            stsModel.AccessBucketNamePublic
        }

        // 自定义文件的 objectKey 上传到仓库中哪个位置
        val imageObjectKey = if (isEncryptFile) {
            if (BuildConfig.FLAVOR == "dev") {
                StringUtils.getString(R.string.upload_image_encryption_path_dev_format, System.currentTimeMillis().toString())
            } else {
                StringUtils.getString(R.string.upload_image_encryption_path_format, System.currentTimeMillis().toString())
            }
        } else {
            if (BuildConfig.FLAVOR == "dev") {
                StringUtils.getString(R.string.upload_image_path_dev_format, System.currentTimeMillis().toString())
            } else {
                StringUtils.getString(R.string.upload_image_path_format, System.currentTimeMillis().toString())
            }
        }


        val credentialProvider: OSSCredentialProvider =
            OSSStsTokenCredentialProvider(stsModel.AccessKeyId, stsModel.AccessKeySecret, stsModel.SecurityToken)

        val conf = ClientConfiguration().apply {
            connectionTimeout = 15 * 1000   // 连接超时，默认15秒
            socketTimeout = 15 * 1000       // socket超时，默认15秒
            maxConcurrentRequest = 5        // 最大并发请求数，默认5个
            maxErrorRetry = 2               // 失败后最大重试次数，默认2次
        }

        OSSLog.enableLog() //这个开启会支持写入手机sd卡中的一份日志文件位置在SDCard_path\OSSLog\logs.csv

        //初始化OSS服务的客户端oss
        //事实上，初始化OSS的实例对象，应该具有与整个应用程序相同的生命周期，在应用程序生命周期结束时销毁
        val oss: OSS = OSSClient(appContext, "https://$endpoint", credentialProvider, conf)

        // 构造上传请求,第二个数参是ObjectName，第三个参数是本地文件路径
        val put = PutObjectRequest(imageBucketName, imageObjectKey, byteArray)
        put.progressCallback = OSSProgressCallback<PutObjectRequest> { request, currentSize, totalSize ->
            Log.i("上传进度：", "当前进度$currentSize   总进度$totalSize");
        }

        val task = oss.asyncPutObject(put, object : OSSCompletedCallback<PutObjectRequest, PutObjectResult> {
            override fun onSuccess(request: PutObjectRequest?, result: PutObjectResult?) {
                Log.d("PutObject", "UploadSuccess");
                val finalUrl = StringUtils.getString(R.string.final_url_format, imageBucketName, endpoint, imageObjectKey)
                view.uploadAutographSuccess(finalUrl)
            }

            override fun onFailure(request: PutObjectRequest?, clientException: ClientException?, serviceException: ServiceException?) {
                clientException?.printStackTrace()
                if (serviceException != null) {
                    // 服务异常。
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }

        })

        // 等异步上传过程完成
        task.waitUntilFinished();
//        Toast.makeText(getActivity(), "上传成功", Toast.LENGTH_SHORT).show();


    }


}