package com.account.feature.my

import android.content.DialogInterface
import android.content.Intent
import android.view.View
import android.widget.RelativeLayout
import butterknife.OnClick
import com.account.R
import com.account.app.Glide
import com.account.base.ui.BaseFragment
import com.account.common.CommonUtils
import com.account.common.Constants
import com.account.entity.users.User
import com.account.event.Event
import com.account.event.RxBus
import com.account.extend.RoundTransFormation
import com.account.feature.about.AboutActivity
import com.account.feature.account.login.LoginActivity
import com.account.view.dialog.AlertDialog
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.VibrateUtils
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
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
            name_tv.text = getString(R.string.un_login_name)
            name_tv.isClickable = true
            user_id_tv.visibility = View.GONE
            login_out_tv.visibility = View.INVISIBLE
            spread_rl.visibility = View.GONE
            return
        }

        val user = User.get()

        Glide.with(context!!)
            //            .load(User.get().photo)
            .load(CommonUtils.getTestUrl())
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

        name_tv.text = user.nickName
        name_tv.isClickable = false
        user_id_tv.apply {
            text = user.username.toString()
            visibility = View.VISIBLE
        }
        login_out_tv.visibility = View.VISIBLE
        spread_rl.visibility = View.VISIBLE
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
                startActivity(LoginActivity::class.java)
            }
            R.id.login_out_tv -> {
                VibrateUtils.vibrate(10)
                AlertDialog.Builder(context)
                    .setMessage(getString(R.string.login_out_hint))
                    .setPositiveButton(R.string.confirm, AlertDialog.STANDARD, DialogInterface.OnClickListener { dialog, _ ->
                        run {
                            dialog.dismiss()
                            presenter.loginOut()


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

            }
            R.id.user_info_ll -> {

            }
            R.id.order_rl -> {

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


}