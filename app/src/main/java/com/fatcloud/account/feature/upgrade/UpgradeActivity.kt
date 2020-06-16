package com.fatcloud.account.feature.upgrade

import android.content.DialogInterface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ColorUtils
import com.fatcloud.account.R
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.Constants
import com.fatcloud.account.common.ProductUtils
import com.fatcloud.account.entity.upgrade.Upgrade
import com.fatcloud.account.view.dialog.AlertDialog

/**
 * Created by Wangsw on 2020/6/16 0016 21:50.
 * </br>
 *
 */
class UpgradeActivity : BaseMVPActivity<UpgradePresenter>(), UpgradeView {


    private lateinit var upgrade: Upgrade

    private var launchSetting = false
    private val isDownload = false

    override fun getLayoutId(): Int = R.layout.activity_upgrade

    override fun showLoading() = showLoadingDialog()

    override fun hideLoading() = dismissLoadingDialog()


    override fun initViews() {
        initExtra()
        handlePermission()
    }

    override fun onResume() {
        super.onResume()
        if (launchSetting) {
            launchSetting = false
            handlePermission()
        }
    }

    override fun onBackPressed() = Unit

    private fun initExtra() {
        if (intent.extras == null || !intent.extras!!.containsKey(Constants.PARAM_DATA)) {
            finish()
            return
        }
        upgrade = intent.extras!!.getSerializable(Constants.PARAM_DATA) as Upgrade
    }


    private fun handlePermission() {

        if (ProductUtils.requestAlbumPermissions(this)) {
            showUpgradeDialog()
        } else {
            if (upgrade.appForce == 0) {
                // 非强制升级
                doUninhibited()
            } else {
                // 强制升级直接关闭页面
                finish()
                System.exit(0)
            }


        }
    }


    /**
     * 非强制升级时，
     *
     *
     * 用户未授权存储权限，引导跳转至设置页面
     */
    private fun doUninhibited() {

        val hint01 = getString(R.string.new_version_hint_01)
        val hint02 = getString(R.string.new_version_hint_02)
        val hint03 = getString(R.string.new_version_hint_03)
        val stringBuilder = SpannableStringBuilder(hint01 + hint02 + hint03)
        val span = ForegroundColorSpan(ColorUtils.getColor(R.color.color_red_foreground))
        stringBuilder.setSpan(span, hint01.length, (hint01 + hint02).length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        AlertDialog.Builder(this)
            .setMessage(stringBuilder)
            .setPositiveButton(R.string.confirm, AlertDialog.STANDARD, DialogInterface.OnClickListener { dialog, which ->
                launchSetting = true
                AppUtils.launchAppDetailsSettings()
                dialog.dismiss()
            })
            .setNegativeButton(R.string.cancel, AlertDialog.STANDARD, DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            })
            .create()
            .show()
    }


    /**
     * 进行版本升级
     */
    private fun showUpgradeDialog() {

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.app_up))
            .setCancelable(false)
            .setMessage(upgrade.appExplain)
            .setPositiveButton(R.string.confirm, AlertDialog.STANDARD, DialogInterface.OnClickListener { dialog, which ->

                dialog.dismiss()
            })
            .setNegativeButton(R.string.cancel, AlertDialog.STANDARD, DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            })
            .create()
            .show()

    }


}