package com.fatcloud.account.app

import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.annotation.IdRes
import com.alibaba.sdk.android.oss.*
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback
import com.alibaba.sdk.android.oss.common.OSSLog
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask
import com.alibaba.sdk.android.oss.model.GetObjectRequest
import com.alibaba.sdk.android.oss.model.GetObjectResult
import com.alibaba.sdk.android.oss.model.PutObjectRequest
import com.alibaba.sdk.android.oss.model.PutObjectResult
import com.blankj.utilcode.util.*
import com.fatcloud.account.BuildConfig
import com.fatcloud.account.R
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.common.AndroidUtil
import com.fatcloud.account.common.Common
import com.fatcloud.account.entity.commons.Commons
import com.fatcloud.account.entity.oss.SecurityTokenModel
import com.fatcloud.account.event.Event
import com.fatcloud.account.event.RxBus
import com.fatcloud.account.event.entity.ImageUploadEvent
import com.fatcloud.account.network.ApiService
import io.reactivex.FlowableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream


/**
 * Created by Wangsw on 2020/5/22 0022 15:52.
 * </br>
 *
 */
class CloudAccountPresenter(val view: CloudAccountView) {

    private val apiService: ApiService = (Utils.getApp() as CloudAccountApplication).apiService


    private var compositeDisposable: CompositeDisposable? = null

    fun addSubscribe(subscription: Disposable) {
        if (compositeDisposable == null) {
//            RetrofitUrlManager.getInstance().putDomain(ApiService.NEW_SERVICE, UrlUtil.SERVER_HOST_V3)
            compositeDisposable = CompositeDisposable()
        }
        compositeDisposable?.add(subscription)
    }

    private fun <T> flowableUICompose(): FlowableTransformer<T, T> {
        return FlowableTransformer {
            it.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    private fun <T> flowableCompose(): FlowableTransformer<T, T> {
        return FlowableTransformer {
            it.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
        }
    }


    /**
     * 订阅事件
     * @param consumer 处理
     */
    fun subsribeEvent(consumer: Consumer<Event>) {
        addRxBusSubscribe(Event::class.java, consumer)
    }


    /**
     * 添加Rxbus的订阅
     * @param eventType 传递类型
     * @param consumer  消费
     */
    fun <T> addRxBusSubscribe(eventType: Class<T>, consumer: Consumer<T>) {
        if (compositeDisposable == null) {
            compositeDisposable = CompositeDisposable()
        }
        compositeDisposable?.add(
            RxBus.toFlowable(eventType).observeOn(AndroidSchedulers.mainThread()).subscribe(consumer, Consumer {
                LogUtils.d(it)
            })
        )
    }


    fun getCommonList() {
        addSubscribe(
            apiService.getCommonList()
                .compose(flowableUICompose())
                .subscribeWith(object : BaseHttpSubscriber<Commons>(view) {
                    override fun onSuccess(data: Commons?) {
                        data?.let {

                            /**
                             * 营业执照注销承诺书
                             */
                            it.commitmentUrl = "/api"+ it.commitmentUrl

                            view.receiveCommonData(it)

                        }
                    }

                })
        )
    }


    /**
     * 获取 token
     * @param objectName 文件路径
     * @param isEncryptFile 是否为加密文件
     */
    fun getOssSecurityToken(
        context: Context,
        isEncryptFile: Boolean,
        isFaceUp: Boolean,
        localFilePatch: String,
        @IdRes fromViewId: Int,
        clx: Class<*>
    ) {


        addSubscribe(
            apiService.getOssSecurityToken().compose(flowableUICompose())
                .subscribeWith(object : BaseHttpSubscriber<SecurityTokenModel>(view) {
                    override fun onSuccess(data: SecurityTokenModel?) {
                        data?.let {
                            val runnable = Runnable {
                                uploadResources(context, it, isEncryptFile, localFilePatch, isFaceUp, fromViewId, clx)
                            }
                            Thread(runnable).start()
                        }
                    }
                })
        )
    }


    /**
     * 上传图片资源至阿里云
     * @see {@see https://github.com/aliyun/aliyun-oss-android-sdk/blob/master/README-CN.md}
     * @param isEncryptFile 是否为加密上传
     */
    fun uploadResources(
        context: Context,
        stsModel: SecurityTokenModel,
        isEncryptFile: Boolean,
        localFilePatch: String,
        isFaceUp: Boolean,
        @IdRes fromViewId: Int,
        clx: Class<*>

    ) {

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
            // 加密图片
            if (BuildConfig.FLAVOR == "dev") {
                StringUtils.getString(R.string.upload_image_encryption_path_dev_format, System.currentTimeMillis().toString())
            } else {
                StringUtils.getString(R.string.upload_image_encryption_path_format, System.currentTimeMillis().toString())
            }
        } else {
            // 普通图片
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
        val oss: OSS = OSSClient(context, "https://$endpoint", credentialProvider, conf)

        // 构造上传请求,第二个数参是ObjectName，第三个参数是本地文件路径
        val put = PutObjectRequest(imageBucketName, imageObjectKey, localFilePatch)
        put.progressCallback = OSSProgressCallback<PutObjectRequest> { request, currentSize, totalSize ->
//            Log.i("上传进度：", "当前进度$currentSize   总进度$totalSize");
        }

        val task = oss.asyncPutObject(put, object : OSSCompletedCallback<PutObjectRequest, PutObjectResult> {
            override fun onSuccess(request: PutObjectRequest?, result: PutObjectResult?) {
//                Log.d("PutObject", "UploadSuccess");

                var finalUrl = ""
                if (isEncryptFile) {
                    finalUrl = imageObjectKey
                } else {
                    finalUrl = StringUtils.getString(R.string.final_url_format, imageBucketName, endpoint, imageObjectKey)
                }

                RxBus.post(ImageUploadEvent(finalUrl, isFaceUp, fromViewId, clx))
            }

            override fun onFailure(request: PutObjectRequest?, clientException: ClientException?, serviceException: ServiceException?) {
                clientException?.printStackTrace()
//                if (serviceException != null) {
                // 服务异常。
//                    Log.e("ErrorCode", serviceException.getErrorCode());
//                    Log.e("RequestId", serviceException.getRequestId());
//                    Log.e("HostId", serviceException.getHostId());
//                    Log.e("RawMessage", serviceException.getRawMessage());
//                }
            }

        })

        // 等异步上传过程完成
        task.waitUntilFinished();
//        Toast.makeText(getActivity(), "上传成功", Toast.LENGTH_SHORT).show();


    }


    /**
     * 获取 token
     * @param objectName 文件路径
     * @param isEncryptFile 是否为加密文件
     */
    fun getOssSecurityTokenForSignUrl(context: Context, objectKey: String, ossCallBack: CloudAccountApplication.OssSignCallBack) {


        addSubscribe(
            apiService.getOssSecurityToken().compose(flowableUICompose())
                .subscribeWith(object : BaseHttpSubscriber<SecurityTokenModel>(view) {
                    override fun onSuccess(data: SecurityTokenModel?) {
                        data?.let {
                            val runnable = Runnable {
                                // 节点
                                val endpoint = BuildConfig.OSS_END_POINT

                                val credentialProvider: OSSCredentialProvider =
                                    OSSStsTokenCredentialProvider(it.AccessKeyId, it.AccessKeySecret, it.SecurityToken)

                                val conf = ClientConfiguration().apply {
                                    connectionTimeout = 15 * 1000   // 连接超时，默认15秒
                                    socketTimeout = 15 * 1000       // socket超时，默认15秒
                                    maxConcurrentRequest = 5        // 最大并发请求数，默认5个
                                    maxErrorRetry = 2               // 失败后最大重试次数，默认2次
                                }

                                val oss: OSS = OSSClient(context, "https://$endpoint", credentialProvider, conf)

                                val url: String = oss.presignConstrainedObjectURL(it.AccessBucketName, objectKey, 30 * 60)
                                ThreadUtils.runOnUiThread {
                                    ossCallBack.ossUrlSignEnd(url)
                                }
                            }
                            Thread(runnable).start()
                        }
                    }
                })
        )


    }


    /**
     * 签名URL
     */
    fun preSignConstrainedObjectURL(context: Context, stsModel: SecurityTokenModel, objectKey: String): String {

        // 节点
        val endpoint = BuildConfig.OSS_END_POINT

        val credentialProvider: OSSCredentialProvider =
            OSSStsTokenCredentialProvider(stsModel.AccessKeyId, stsModel.AccessKeySecret, stsModel.SecurityToken)

        val conf = ClientConfiguration().apply {
            connectionTimeout = 15 * 1000   // 连接超时，默认15秒
            socketTimeout = 15 * 1000       // socket超时，默认15秒
            maxConcurrentRequest = 5        // 最大并发请求数，默认5个
            maxErrorRetry = 2               // 失败后最大重试次数，默认2次
        }

        val oss: OSS = OSSClient(context, "https://$endpoint", credentialProvider, conf)

        val url: String = oss.presignConstrainedObjectURL(stsModel.AccessBucketName, objectKey, 30 * 60)
        return url
    }


    /**
     * 获取 token
     * @param objectName 文件路径
     * @param isEncryptFile 是否为加密文件
     */
    fun getOssSecurityTokenForDownload(context: Context, objectKey: String?) {


        val sdStatus = Environment.getExternalStorageState()
        if (sdStatus != Environment.MEDIA_MOUNTED) { // 检测sd是否可用
            ToastUtils.showShort("SD卡不可用")
            return
        }

        addSubscribe(
            apiService.getOssSecurityToken()
                .compose(flowableUICompose())
                .subscribeWith(object : BaseHttpSubscriber<SecurityTokenModel>(view) {
                    override fun onSuccess(data: SecurityTokenModel?) {

                        if (data == null) {
                            return
                        }
                        val runnable = Runnable {
                            // 节点
                            val endpoint = BuildConfig.OSS_END_POINT

                            val credentialProvider: OSSCredentialProvider =
                                OSSStsTokenCredentialProvider(data.AccessKeyId, data.AccessKeySecret, data.SecurityToken)

                            val conf = ClientConfiguration().apply {
                                connectionTimeout = 15 * 1000   // 连接超时，默认15秒
                                socketTimeout = 15 * 1000       // socket超时，默认15秒
                                maxConcurrentRequest = 5        // 最大并发请求数，默认5个
                                maxErrorRetry = 2               // 失败后最大重试次数，默认2次
                            }

                            val oss: OSS = OSSClient(context, "https://$endpoint", credentialProvider, conf)


                            //objectKey等同于objectname，表示从OSS下载文件时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。
                            val get = GetObjectRequest(data.AccessBucketName, objectKey)

                            oss.asyncGetObject(get, object : OSSCompletedCallback<GetObjectRequest?, GetObjectResult> {

                                override fun onSuccess(request: GetObjectRequest?, result: GetObjectResult) {

                                    //开始读取数据。
                                    val length = result.contentLength
                                    val buffer = ByteArray(length.toInt())
                                    var readCount = 0
                                    while (readCount < length) {
                                        try {
                                            readCount += result.objectContent.read(buffer, readCount, length.toInt() - readCount)
                                        } catch (e: Exception) {
                                            OSSLog.logInfo(e.toString())
                                        }
                                    }

                                    //将下载后的文件存放在指定的本地路径。

                                    val path = Environment.getExternalStorageDirectory().absolutePath + Common.IMAGE_SAVE_PATH
                                    val name = "commitment.png"
                                    // 创建文件路径
                                    val file = File(path)
                                    if (!file.exists()) {
                                        file.mkdirs()
                                    }
                                    // 创建文件
                                    val fileName = File(path + name)
                                    if (fileName.exists()) {
                                        fileName.delete()
                                    }
                                    fileName.createNewFile()

                                    var fout: FileOutputStream? = null
                                    try {
                                        fout = FileOutputStream(fileName)
                                        fout.write(buffer)
                                        fout.close()

//                                        ThreadUtils.runOnUiThread {
//                                            ossCallBack.ossUrlSignEnd(url)
//                                        }

                                        AndroidUtil.exportToGallery(path + name, context)
                                        ToastUtils.showShort("下载成功")
                                    } catch (e: Exception) {
                                        OSSLog.logInfo(e.toString())
                                    } finally {
                                        fout?.close()
                                    }
                                }

                                override fun onFailure(request: GetObjectRequest?, clientExcepion: ClientException, serviceException: ServiceException) {
                                    // 请求异常
                                    if (clientExcepion != null) {
                                        // 本地异常如网络异常等
                                        clientExcepion.printStackTrace();
                                    }
                                    if (serviceException != null) {
                                        // 服务异常
                                        Log.e("ErrorCode", serviceException.getErrorCode());
                                        Log.e("RequestId", serviceException.getRequestId());
                                        Log.e("HostId", serviceException.getHostId());
                                        Log.e("RawMessage", serviceException.getRawMessage());
                                    }
                                }
                            })


                        }
                        Thread(runnable).start()


                    }
                })
        )


    }


}