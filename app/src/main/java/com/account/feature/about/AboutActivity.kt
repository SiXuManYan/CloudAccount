package com.account.feature.about

import android.content.Intent
import android.view.View
import butterknife.OnClick
import com.account.R
import com.account.base.ui.BaseMVPActivity
import com.account.common.Constants
import com.account.common.Html5Url
import com.account.feature.about.contacts.ContactActivity
import com.account.feature.webs.WebCommonActivity

/**
 * Created by Wangsw on 2020/6/1 0001 13:25.
 * </br>
 *
 */
class AboutActivity : BaseMVPActivity<AboutPresenter>(), AboutView {

    override fun getLayoutId() = R.layout.activity_about


    override fun initViews() {
        setMainTitle(getString(R.string.about_us))
    }


    @OnClick(
        R.id.privacy_statement_rl,
        R.id.copyright_rl,
        R.id.contact_us_rl
    )
    fun onClick(view: View) {
        when (view.id) {
            R.id.privacy_statement_rl -> {
                startActivity(
                    Intent(this, WebCommonActivity::class.java)
                        .putExtra(Constants.PARAM_URL, Html5Url.PRIVACY_STATEMENT_URL)
                        .putExtra(Constants.PARAM_TITLE, getString(R.string.privacy_statement))
                        .putExtra(Constants.PARAM_WEB_REFRESH, false)
                )

            }
            R.id.copyright_rl -> {
                startActivity(
                    Intent(this, WebCommonActivity::class.java)
                        .putExtra(Constants.PARAM_URL, Html5Url.COPYRIGHT_URL)
                        .putExtra(Constants.PARAM_TITLE, getString(R.string.copyright_protection))
                        .putExtra(Constants.PARAM_WEB_REFRESH, false)
                )

            }
            R.id.contact_us_rl -> {
                startActivity(ContactActivity::class.java)
            }

            else -> {
            }
        }

    }


}