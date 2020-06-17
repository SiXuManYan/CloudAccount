package com.fatcloud.account.feature.upgrade

import android.content.DialogInterface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.ToastUtils
import com.fatcloud.account.R
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.Constants
import com.fatcloud.account.common.ProductUtils
import com.fatcloud.account.entity.upgrade.Upgrade
import com.fatcloud.account.view.dialog.AlertDialog

/**
 * Created by Wangsw on 2020/6/16 0016 21:50.
 * </br>
 * 版本升级
 */
class UpgradeActivity : BaseMVPActivity<UpgradePresenter>(), UpgradeView {


    private var permissionDialog: AlertDialog? = null
    private var upgradeDialog: AlertDialog? = null
    private lateinit var downUtil: DownUtil

    /**
     * 强制升级
     */
    val INHIBITED = 1

    /**
     * 不强制升级
     */
    val UN_INHIBITED = 0


    private var launchSetting = false
    private var isDownload = false

    private var appForce = 0
    private var appVersion = ""
    private var appExplain = ""
    private var appUrl = ""

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

    override fun onBackPressed() {
        if (appForce == UN_INHIBITED) {
            super.onBackPressed()
        }

    }

    private fun initExtra() {
        if (intent.extras == null) {
            finish()
            return
        }


        intent.getIntExtra(Constants.PARAM_APP_FORCE, 0)
        intent.getStringExtra(Constants.PARAM_APP_VERSION)
        intent.getStringExtra(Constants.PARAM_APP_EXPLAIN)
        intent.getStringExtra(Constants.PARAM_APP_URL)

        downUtil = DownUtil(this)
    }


    private fun handlePermission() {

        if (ProductUtils.requestAlbumPermissions(this)) {
            showUpgradeDialog()
        } else {
            if (appForce == UN_INHIBITED) {
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

        permissionDialog = AlertDialog.Builder(this)
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
        permissionDialog?.show()
    }


    /**
     * 进行版本升级
     */
    private fun showUpgradeDialog() {

        upgradeDialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.app_up))
            .setCancelable(false)
            .setMessage(appExplain)
            .setPositiveButton(R.string.confirm, AlertDialog.STANDARD, DialogInterface.OnClickListener { dialog, which ->

                if (!isDownload) {
                    downUtil.startDownload(appUrl, getString(R.string.app_name), R.mipmap.ic_logo_round)
                    isDownload = true
                } else {
                    ToastUtils.showShort(getString(R.string.apk_downloading))
                }
                if (appForce == UN_INHIBITED) {
                    dialog.dismiss()
                    finish()
                }
            })
            .setNegativeButton(R.string.cancel, AlertDialog.STANDARD, DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
                finish()
            }).create()
        upgradeDialog?.show()
    }

    override fun onDestroy() {

        permissionDialog?.dismiss()
        upgradeDialog?.dismiss()
        super.onDestroy()
    }


}