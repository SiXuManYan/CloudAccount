package com.account.feature.my

import android.view.View
import butterknife.OnClick
import com.account.R
import com.account.app.Glide
import com.account.base.ui.BaseFragment
import com.account.common.Common
import com.account.common.CommonUtils
import com.account.common.Constants
import com.account.entity.users.User
import com.account.extend.RoundTransFormation
import com.account.feature.about.AboutActivity
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
        initEvent()
    }


    private fun initEvent() {
        presenter.subsribeEvent(Consumer {
            when (it.code) {
                Constants.EVENT_NEED_REFRESH -> {
                    User.get().id?.let {
                        loadOnVisible()

                    }

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
            user_id_tv.visibility = View.GONE
            login_out_tv.visibility = View.GONE
            spread_rl.visibility = View.GONE
            return
        }

        val user = User.get()

        Glide.with(context!!)
            //            .load(User.get().photo)
            .load(Common.TEST_IMG_URL)
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

        name_tv.text = user.name
        user_id_tv.apply {
            visibility = View.VISIBLE
            text = user.id.toString()
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
        R.id.about_rl

    )
    fun onClick(view: View) {
        when (view.id) {
            R.id.login_out_tv -> {

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