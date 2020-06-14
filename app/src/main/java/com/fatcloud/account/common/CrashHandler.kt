package com.fatcloud.account.common

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.os.Process
import android.util.Log
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Admin on 2019/4/2 10:17.
 * </br>
 *
 */
class CrashHandler private constructor() : Thread.UncaughtExceptionHandler {

    companion object {

        private val TAG = "CrashHandler"
        private val DEBUG = true

        private val PATH = Environment.getExternalStorageDirectory().path + "/Cloud/kLog/"
        private val FILE_NAME = "crash"
        private val FILE_NAME_SUFFIX = ".txt"

        @SuppressLint("StaticFieldLeak")
        val instance = CrashHandler()
    }


    private var mDefaultCrashHandler: Thread.UncaughtExceptionHandler? = null
    private var mContext: Context? = null

    fun init(context: Context) {
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
        mContext = context.applicationContext
    }

    override fun uncaughtException(thread: Thread, ex: Throwable) {
        try {
            dumpExceptionToSDCard(ex)
            uploadExceptionToServer()

        } catch (e: IOException) {
            e.printStackTrace()
        }

        ex.printStackTrace()

        if (mDefaultCrashHandler != null) {
            mDefaultCrashHandler!!.uncaughtException(thread, ex)
        } else {
            Process.killProcess(Process.myPid())
        }

    }

    @Throws(IOException::class)
    private fun dumpExceptionToSDCard(ex: Throwable) {

        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
            if (DEBUG) {
                Log.w(TAG, "sdcard unmounted,skip dump exception")
                return
            }
        }

        val dir = File(PATH)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val current = System.currentTimeMillis()
        val time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(current))
        val file = File(PATH + FILE_NAME + time + FILE_NAME_SUFFIX)

        try {
            val pw = PrintWriter(BufferedWriter(FileWriter(file)))
            pw.println(time)
            dumpPhoneInfo(pw)
            pw.println()
            ex.printStackTrace(pw)
            pw.close()
        } catch (e: Exception) {
            Log.e(TAG, "dump crash info failed")
        }

    }

    @Throws(PackageManager.NameNotFoundException::class)
    private fun dumpPhoneInfo(pw: PrintWriter) {
        val pm = mContext!!.packageManager
        val pi = pm.getPackageInfo(mContext!!.packageName, PackageManager.GET_ACTIVITIES)
        pw.print("App Version: ")
        pw.print(pi.versionName)
        pw.print('_')
        pw.println(pi.versionCode)

        //android版本号
        pw.print("OS Version: ")
        pw.print(Build.VERSION.RELEASE)
        pw.print("_")
        pw.println(Build.VERSION.SDK_INT)

        //手机制造商
        pw.print("Vendor: ")
        pw.println(Build.MANUFACTURER)

        //手机型号
        pw.print("Model: ")
        pw.println(Build.MODEL)

        //cpu架构
        pw.print("CPU ABI: ")
        pw.println(Build.CPU_ABI)
    }

    private fun uploadExceptionToServer() {
        //  Upload Exception Message To  Web Server
    }


}
