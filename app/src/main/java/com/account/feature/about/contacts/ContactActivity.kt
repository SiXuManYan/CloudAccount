package com.account.feature.about.contacts

import android.content.Intent
import android.net.Uri
import android.view.View
import butterknife.OnClick
import com.account.R
import com.account.base.ui.BaseMVPActivity
import com.account.common.CommonUtils
import com.account.common.Constants
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

            }
            R.id.qq_rl -> {

            }
            R.id.email_rl -> {

            }


            else -> {
            }
        }

    }

}