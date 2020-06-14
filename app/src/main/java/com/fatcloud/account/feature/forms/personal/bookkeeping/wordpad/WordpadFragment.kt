package com.fatcloud.account.feature.forms.personal.bookkeeping.wordpad

import android.view.View
import butterknife.OnClick
import com.fatcloud.account.R
import com.fatcloud.account.base.ui.BaseBottomSheetDialogFragment
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.event.RxBus
import com.fatcloud.account.event.entity.WordpadEvent
import com.github.gcacace.signaturepad.views.SignaturePad
import kotlinx.android.synthetic.main.fragment_word_pad.*

/**
 * Created by Wangsw on 2020/6/13 0013 18:27.
 * </br>
 *
 */
class WordpadFragment : BaseBottomSheetDialogFragment<WordpadPresenter>(), WordpadView {


    companion object {
        fun newInstance(): WordpadFragment {
            return WordpadFragment()
        }
    }


    override fun getLayoutId() = R.layout.fragment_word_pad

    override fun loadOnVisible() = Unit

    override fun initViews(parent: View) {


        signature_pad.setOnSignedListener(object : SignaturePad.OnSignedListener {
            override fun onStartSigning() {

            }

            override fun onClear() {

            }

            override fun onSigned() {

            }

        })

    }


    @OnClick(
        R.id.resign_tv,
        R.id.commit_tv
    )
    fun onClick(view: View) {
        if (CommonUtils.isDoubleClick(view)) {
            return
        }
        when (view.id) {
            R.id.resign_tv -> {
                signature_pad.clear()
            }
            R.id.commit_tv -> {
                val signatureSvg = signature_pad.signatureSvg
                RxBus.post(WordpadEvent(signatureSvg))
                dismissAllowingStateLoss()
            }
            else -> {
            }
        }
    }


}