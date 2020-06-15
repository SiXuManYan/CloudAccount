package com.fatcloud.account.feature.my

import android.content.DialogInterface
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import butterknife.OnClick
import com.blankj.utilcode.util.*
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.fatcloud.account.R
import com.fatcloud.account.app.CloudAccountApplication
import com.fatcloud.account.app.Glide
import com.fatcloud.account.base.ui.BaseFragment
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.fatcloud.account.common.ProductUtils
import com.fatcloud.account.entity.users.User
import com.fatcloud.account.event.entity.ImageUploadEvent
import com.fatcloud.account.extend.RoundTransFormation
import com.fatcloud.account.feature.about.AboutActivity
import com.fatcloud.account.feature.account.login.LoginActivity
import com.fatcloud.account.feature.matisse.Matisse
import com.fatcloud.account.feature.order.lists.OrderListActivity
import com.fatcloud.account.view.dialog.AlertDialog
import com.fatcloud.account.view.dialog.InputDialog
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_my.*

/**
 * Created by Wangsw on 2020/5/25 0025 16:42.
 * </br>
 *
 */
class MyPageFragment : BaseFragment<MyPagePresenter>(), MyPageView {

    init {
        needMenuControl = true
    }

    override fun showLoading() = showLoadingDialog()

    override fun hideLoading() = dismissLoadingDialog()

    override fun getLayoutId() = R.layout.fragment_my


    override fun initViews(parent: View) {

        val layoutParams = login_out_tv.layoutParams as RelativeLayout.LayoutParams
        layoutParams.topMargin = BarUtils.getStatusBarHeight()

        banner_rl.layoutParams.apply {
            height = (ScreenUtils.getScreenWidth() / 1.78).toInt()
        }

        initEvent()
    }


    private fun initEvent() {
        // 图片上传成功
        presenter.subsribeEventEntity<ImageUploadEvent>(Consumer {
            if (it.formWhichClass != this.javaClass) {
                return@Consumer
            }
            presenter.updateAvatarAndNickname(this@MyPageFragment, it.finalUrl, null)

        })

        presenter.subsribeEvent(Consumer {
            when (it.code) {
                Constants.EVENT_NEED_REFRESH -> {
                    loadOnVisible()

                }
                else -> {
                }
            }
        })
    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            if (isViewCreated) {
                CommonUtils.getFriendlyTime(time_tv)
            }
        }
    }


    override fun loadOnVisible() {
        loginInit()
    }

    private fun loginInit() {
        CommonUtils.getFriendlyTime(time_tv)
        if (!User.isLogon()) {
            ic_avatar_civ.setImageResource(R.drawable.ic_avatar_default)
            ic_avatar_civ.isClickable = false
            name_tv.text = getString(R.string.un_login_name)
            user_id_tv.visibility = View.GONE
            login_out_tv.visibility = View.INVISIBLE

            // 后期开放
            income_rl.visibility = View.GONE
            qr_rl.visibility = View.GONE
            spread_rl.visibility = View.GONE
            return
        }

        val user = User.get()

        Glide.with(context!!)
            .load(User.get().headUrl)
            .apply(
                RequestOptions().transform(
                    MultiTransformation(
                        CenterCrop(),
                        RoundTransFormation(context, 4)
                    )
                )
            )
            .error(R.drawable.ic_error_image_load)
            .into(ic_avatar_civ)
        ic_avatar_civ.isClickable = true

        name_tv.text = user.nickName
        user_id_tv.apply {
            text = user.username.toString()
            visibility = View.VISIBLE
        }
        login_out_tv.visibility = View.VISIBLE

        // 后期开放
        income_rl.visibility = View.GONE
        qr_rl.visibility = View.GONE
        spread_rl.visibility = View.GONE
    }


    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data == null) {
            return
        }

        when (requestCode) {

            Constants.REQUEST_MEDIA -> {
                // 相册选择图片
                val elements = Matisse.obtainPathResult(data)
                if (elements.isNotEmpty()) {
                    val fileDirPath = elements[0]
                    val fromViewId = data.getIntExtra(Matisse.MEDIA_FROM_VIEW_ID, 0)
                    if (fromViewId != 0) {
                        val fromView = activity!!.findViewById<ImageView>(fromViewId)
                        if (fromView != null) {
                            Glide.with(this).load(fileDirPath).into(fromView)
                        }
                    }
                    val application = activity!!.application as CloudAccountApplication
                    application.getOssSecurityToken(false, true, fileDirPath, fromViewId, this@MyPageFragment.javaClass)
                }
            }
            else -> {
            }
        }


    }


    @OnClick(
        R.id.login_out_tv,
        R.id.ic_avatar_civ,
        R.id.user_info_ll,
        R.id.order_rl,
        R.id.income_rl,
        R.id.qr_rl,
        R.id.spread_rl,
        R.id.about_rl,
        R.id.name_tv

    )
    fun onClick(view: View) {
        when (view.id) {

            R.id.name_tv -> {
                handleNameClick(view)
            }
            R.id.login_out_tv -> {
                VibrateUtils.vibrate(10)
                AlertDialog.Builder(context)
                    .setMessage(getString(R.string.login_out_hint))
                    .setPositiveButton(R.string.confirm, AlertDialog.STANDARD, DialogInterface.OnClickListener { dialog, _ ->
                        run {
                            dialog.dismiss()
                            presenter.loginOutRequest(this@MyPageFragment)

                            startActivity(LoginActivity::class.java)
                        }
                    })
                    .setNegativeButton(R.string.cancel, AlertDialog.STANDARD, DialogInterface.OnClickListener { dialog, _ ->
                        dialog.dismiss()
                    })
                    .create()
                    .show()

            }
            R.id.ic_avatar_civ -> {
                ProductUtils.handleMediaSelectForFragment(this, Matisse.IMG, view.id)
            }
            R.id.user_info_ll -> {

            }
            R.id.order_rl -> {
                startActivity(OrderListActivity::class.java)
            }
            R.id.income_rl -> {

            }
            R.id.qr_rl -> {

            }
            R.id.spread_rl -> {

            }

            R.id.about_rl -> {
                startActivity(AboutActivity::class.java)
            }


            else -> {
            }
        }

    }

    private fun handleNameClick(view: View) {
        if (User.isLogon()) {
            InputDialog.Builder(context!!)
                .setTitle("修改昵称")
                .setMessage("请输入昵称")
                .setPositiveButton(R.string.confirm, DialogInterface.OnClickListener { dialog, _ ->
                    val content = (dialog as InputDialog).getInputContent().toString().trim()
                    if (content.isNotEmpty()) {
                        dialog.dismiss()
                        name_tv.text = content
                        presenter.updateAvatarAndNickname(this@MyPageFragment, null, content)

                        KeyboardUtils.hideSoftInput(activity!!)
                    }
                })
                .setNegativeButton(R.string.cancel, DialogInterface.OnClickListener { dialog, _ ->
                    dialog.dismiss()
                    KeyboardUtils.hideSoftInput(activity!!)
                }).create().show()

            KeyboardUtils.showSoftInput(activity!!)
        } else {
            startActivity(LoginActivity::class.java)
        }
    }

    override fun updateAvatarAndNicknameSuccess() {
        ToastUtils.showShort("更新成功")
    }


}