package com.fatcloud.account.feature.about.contacts

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import butterknife.OnClick
import com.blankj.utilcode.util.ToastUtils
import com.fatcloud.account.R
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.blankj.utilcode.util.VibrateUtils

/**
 * Created by Wangsw on 2020/6/1 0001 14:22.
 * </br>
 * 联系我们
 */
class ContactActivity : BaseMVPActivity<ContactPresenter>(), ContactView {

    override fun getLayoutId() = R.layout.activity_contact

    override fun initViews() {
        setMainTitle(R.string.contact_us)
    }


    @OnClick(
        R.id.telephone_rl,
        R.id.wechat_rl,
        R.id.qq_rl,
        R.id.email_rl
    )
    fun onClick(view: View) {
        if (CommonUtils.isDoubleClick(view)) {
            return
        }
        VibrateUtils.vibrate(20)

        when (view.id) {

            R.id.telephone_rl -> {

                startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Constants.CONSUMER_HOT_LINE)))
            }
            R.id.wechat_rl -> {
                saveToClipboard("微信号", getString(R.string.fta_account_wechat))
            }
            R.id.qq_rl -> {
                saveToClipboard("服务QQ", getString(R.string.fta_account_qq))
            }
            R.id.email_rl -> {
                saveToClipboard("电子邮箱", getString(R.string.fta_account_email))
            }


            else -> {
            }
        }

    }

    private fun saveToClipboard(label: String, content: String) {
        val cmb: ClipboardManager = context!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        cmb.primaryClip = ClipData.newPlainText(label, content)
        ToastUtils.showShort(label + "已成功复制到您的剪切板")
    }

}