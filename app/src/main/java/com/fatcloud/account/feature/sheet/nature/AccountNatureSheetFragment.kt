package com.fatcloud.account.feature.sheet.nature

import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.VibrateUtils
import com.fatcloud.account.R
import com.fatcloud.account.app.CloudAccountApplication
import com.fatcloud.account.base.ui.BaseBottomSheetDialogFragment
import com.fatcloud.account.entity.commons.AccountNature
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import kotlinx.android.synthetic.main.fragment_form_sheet.*

/**
 * Created by Wangsw on 2020/6/17 0017 22:20.
 * </br>
 * 账户性质
 */
class AccountNatureSheetFragment : BaseBottomSheetDialogFragment<AccountNatureSheetPresenter>(), AccountNatureSheetView {

    private var adapter: RecyclerArrayAdapter<AccountNature>? = null

    /**
     * 组成形式id
     */
    private var formId = ""

    private var mOnItemSelectedListener: OnItemSelectedListener? = null


    companion object {
        fun newInstance(): AccountNatureSheetFragment {

            return AccountNatureSheetFragment()
        }
    }


    override fun getLayoutId(): Int = R.layout.fragment_form_sheet

    override fun loadOnVisible() = Unit

    override fun initViews(parent: View) {

        title_tv.text = "请选择账户性质"
        val loudAccountApplication = activity?.application as CloudAccountApplication
        val forms = loudAccountApplication.commonData?.accountNatues

        adapter = getRecyclerAdapter()
        adapter?.addAll(forms)
        content_rv.adapter = adapter
    }


    private fun getRecyclerAdapter(): RecyclerArrayAdapter<AccountNature>? {
        val adapter = object : RecyclerArrayAdapter<AccountNature>(context) {

            override fun OnCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<AccountNature> {
                return AccountNatureSheetHolder(parent)
            }

        }

        adapter.setOnItemClickListener {
            VibrateUtils.vibrate(10)
            val allData = adapter.allData
            val model = allData[it]
            formId = model.id
            mOnItemSelectedListener?.onItemSelected(model)
            dismissAllowingStateLoss()
        }
        return adapter
    }


    fun setOnItemSelectListener(listener: OnItemSelectedListener) {
        mOnItemSelectedListener = listener
    }


    /**
     * 当前选中的账户性质信息
     */
    interface OnItemSelectedListener {

        /**
         * 用户选中了账户形式
         * @param currentSelected  当前选中的账户性质
         */
        fun onItemSelected(currentSelected: AccountNature)

    }
}