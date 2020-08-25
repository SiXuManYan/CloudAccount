package com.fatcloud.account.feature.product.detail.share

import android.view.View
import butterknife.OnClick
import cn.sharesdk.framework.Platform
import cn.sharesdk.framework.PlatformActionListener
import com.blankj.utilcode.util.ToastUtils
import com.fatcloud.account.R
import com.fatcloud.account.base.ui.BaseBottomSheetDialogFragment
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.ShareUtil
import java.util.HashMap

/**
 * Created by Wangsw on 2020/7/31 0031 16:09.
 * </br>
 *
 */
class ShareFragment : BaseBottomSheetDialogFragment<SharePresenter>(), ShareView {

    override fun getLayoutId() = R.layout.fragment_product_share

    override fun loadOnVisible() = Unit

    override fun initViews(parent: View) {

    }

    @OnClick(
        R.id.wechat_ll,
        R.id.moment_ll,
        R.id.qq_ll,
        R.id.qzone_ll,
        R.id.next_tv
    )
    fun onClick(view: View) {
        if (CommonUtils.isDoubleClick(view)) {
            return
        }
        dismissAllowingStateLoss()
        when (view.id) {
            R.id.wechat_ll -> {
                ShareUtil.shareWechat4WebPage(
                    "https://www.github.com",
                    "测试标题",
                    "金哥美滋滋",
                    "https://ftacloud-bucket-public.oss-cn-qingdao.aliyuncs.com/product/web-2020710925369952208-app-p8-00-logo.png",
                    null,
                    object : PlatformActionListener {
                        override fun onComplete(p0: Platform?, p1: Int, p2: HashMap<String, Any>?) {
                            ToastUtils.showShort("分享成功")
                        }

                        override fun onCancel(p0: Platform?, p1: Int) {
                            ToastUtils.showShort("分享取消")
                        }

                        override fun onError(p0: Platform?, p1: Int, p2: Throwable?) {
                            ToastUtils.showShort("分享失败")
                        }

                    })


            }
            R.id.moment_ll -> {
                ShareUtil.shareWebpagerMoment(
                    " Share test ......",
                    "https://developer.android.google.cn/images/meetups/hero.png",
                    null,
                    null,
                    "https://developer.android.google.cn/studio",

                    object : PlatformActionListener {
                        override fun onComplete(p0: Platform?, p1: Int, p2: HashMap<String, Any>?) {
                            ToastUtils.showShort("分享成功")
                        }

                        override fun onCancel(p0: Platform?, p1: Int) {
                            ToastUtils.showShort("分享取消")
                        }

                        override fun onError(p0: Platform?, p1: Int, p2: Throwable?) {
                            ToastUtils.showShort("分享失败")
                        }

                    })
            }
            R.id.qq_ll -> {

            }
            R.id.qzone_ll -> {

            }
            R.id.next_tv -> {

            }
            else -> {
            }
        }
    }


}