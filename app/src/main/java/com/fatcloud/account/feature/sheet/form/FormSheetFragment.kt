package com.fatcloud.account.feature.sheet.form

import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.VibrateUtils
import com.fatcloud.account.R
import com.fatcloud.account.app.CloudAccountApplication
import com.fatcloud.account.base.ui.BaseBottomSheetDialogFragment
import com.fatcloud.account.entity.commons.Form
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import kotlinx.android.synthetic.main.fragment_form_sheet.*
import kotlinx.android.synthetic.main.fragment_product_sheet.content_rv

/**
 * Created by Wangsw on 2020/6/17 0017 20:37.
 * </br>
 * 组成形式,选择sheet
 */
class FormSheetFragment : BaseBottomSheetDialogFragment<FormSheetPresenter>(), FormSheetView {


    private var adapter: RecyclerArrayAdapter<Form>? = null

    /**
     * 组成形式id
     */
    private var formId = ""

    private var mOnItemSelectedListener: OnItemSelectedListener? = null


    companion object {
        fun newInstance(): FormSheetFragment {

            return FormSheetFragment()
        }
    }


    override fun getLayoutId(): Int = R.layout.fragment_form_sheet

    override fun loadOnVisible() = Unit

    override fun initViews(parent: View) {

        title_tv.text = "请选择组成形式"
        val loudAccountApplication = activity?.application as CloudAccountApplication
        val forms = loudAccountApplication.commonData?.forms

        adapter = getRecyclerAdapter()
        adapter?.addAll(forms)
        content_rv.adapter = adapter
    }


    private fun getRecyclerAdapter(): RecyclerArrayAdapter<Form>? {
        val adapter = object : RecyclerArrayAdapter<Form>(context) {

            override fun OnCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<Form> {
                return FormSheetHolder(parent)
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


    fun setOnFormSelectListener(listener: OnItemSelectedListener) {
        mOnItemSelectedListener = listener
    }


    interface OnItemSelectedListener {

        /**
         * 用户选中了组成形式
         * @param currentSelected  当前选中的组成形式
         */
        fun onItemSelected(currentSelected: Form)

    }


}