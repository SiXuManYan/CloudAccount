package com.fatcloud.account.base.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.Unbinder
import com.blankj.utilcode.util.ToastUtils
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.common.BaseView
import com.fatcloud.account.common.Constants
import com.fatcloud.account.entity.users.User
import com.fatcloud.account.view.dialog.LoadingDialog

import dagger.android.support.DaggerFragment
import javax.inject.Inject

/**
 * MVP的Fragment的基类
 */
abstract class BaseFragment<P : BasePresenter> : DaggerFragment(), BaseView {

    private var unbinder: Unbinder? = null
    var isViewCreated = false
    var isViewVisible = false
    protected var hasLoad = false
    private var lastClickTime = 0L
    private var clicked: HashSet<Int>? = null

    protected var needMenuControl = false
    private var loadingDialog: LoadingDialog? = null

    @Inject
    lateinit var presenter: P


    fun presenterIsInitialized() = this::presenter.isInitialized

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?) = inflater.inflate(getLayoutId(), container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        unbinder = ButterKnife.bind(this, view)
        initViews(view)
        isViewCreated = true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lazyLoad()
    }

    //    override fun onDestroyView() {
//        super.onDestroyView()
//        destroy()
//    }

    override fun onDestroy() {
        super.onDestroy()
        destroy()
    }

    fun destroy() {
        unbinder?.unbind()
        presenter.detachView()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isViewVisible = isVisibleToUser
        lazyLoad()
        if (isVisibleToUser) {
            // 页面统计
            // AnalyticPageUtil.pageEvent(this)

        }
    }

    override fun setMenuVisibility(menuVisible: Boolean) {
        if (needMenuControl && view != null) {
            view?.visibility = if (menuVisible) View.VISIBLE else View.GONE
        }
        super.setMenuVisibility(menuVisible)
    }

    protected fun showLoadingDialog() {
        dismissLoadingDialog()
        loadingDialog = LoadingDialog.Builder(context).setCancelable(true).create()
        loadingDialog!!.show()
    }

    protected fun dismissLoadingDialog() {
        if (loadingDialog != null && loadingDialog!!.isShowing) {
            loadingDialog!!.dismiss()
        }
        loadingDialog = null
    }

    override fun showError(code: Int, message: String) {
        if (code >= 0) {
            ToastUtils.showShort(if (message.isNullOrEmpty()) "出现错误($code)" else message)
        } else {
            ToastUtils.showShort(message)
        }
    }

    protected fun <T> startActivity(target: Class<T>) {
        startActivity(Intent(context!!.applicationContext, target))
    }

    protected fun <T> startActivity(target: Class<T>, bundle: Bundle) {
        startActivity(Intent(context!!.applicationContext, target).putExtras(bundle))
    }

    protected fun <T> startActivityWithArgument(target: Class<T>, key: String, value: Any) {
        val bundle = Bundle()
        when (value) {
            is String -> bundle.putString(key, value)
            is Long -> bundle.putLong(key, value)
            is Int -> bundle.putInt(key, value)
            is Boolean -> bundle.putBoolean(key, value)
        }
        startActivity(target, bundle)
    }

    protected fun <T> startActivityForResult(target: Class<T>, requestCode: Int) {
        startActivityForResult(Intent(context!!.applicationContext, target), requestCode)
    }

    protected fun <T> startActivityForResultAfterLogin(target: Class<T>, bundle: Bundle, requestCode: Int) {
        if (User.isLogon()) {
            startActivityForResult(Intent(context!!.applicationContext, target).putExtras(bundle), requestCode)
        } else {
//            startActivity(SignUpActivity::class.java)
        }
    }

    protected fun <T> startActivityAfterLogin(target: Class<T>) {
        if (User.isLogon()) {
            startActivity(target)
        } else {
//            startActivity(SignUpActivity::class.java)
        }
    }

    protected fun <T> startActivityAfterLogin(target: Class<T>, bundle: Bundle) {
        if (User.isLogon()) {
            startActivity(target, bundle)
        } else {
//            startActivity(SignUpActivity::class.java)
        }
    }

    protected fun startActivityAfterLogin(intent: Intent) {

        if (User.isLogon()) {
            startActivity(intent)
        } else {
//            startActivity(SignUpActivity::class.java)
        }
    }


    protected fun startWebActivity(bundle: Bundle) {
//        startActivity(Intent(context!!.applicationContext, WebActivity::class.java).putExtras(bundle))
    }

    protected fun startWebActivity(title: String, url: String) {
        val bundle = Bundle()
        bundle.putString(Constants.WEB_TITLE, title)
        bundle.putString(Constants.WEB_URL, url)
        startWebActivity(bundle)
    }

    protected fun onOnceClick(view: View): Boolean {
        if (clicked == null) {
            clicked = HashSet()
        }
        if (clicked!!.contains(view.id) && (System.currentTimeMillis() - lastClickTime) < Constants.CLICK_INTERVAL) {
            return false
        }
        lastClickTime = System.currentTimeMillis()
        clicked!!.add(view.id)
        return true
    }

    open fun lazyLoad() {
        if (!hasLoad && isViewVisible && isViewCreated) {
            hasLoad = true
            loadOnVisible()
        }
    }

    /**
     * 获取布局ID
     */
    abstract fun getLayoutId(): Int

    /**
     * 初始化控件
     */
    abstract fun initViews(parent: View)

    /**
     * 延迟加载
     */
    abstract fun loadOnVisible()




}