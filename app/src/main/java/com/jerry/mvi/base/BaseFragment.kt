package com.jerry.mvi.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jerry.mvi.R
import com.jerry.mvi.ui.MainActivity

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BaseFragment(@LayoutRes contentLayoutId : Int = 0) : Fragment(contentLayoutId) {

    lateinit var mainActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
    }

    open fun showBack(): Boolean = false

    fun displayRetryDialog(mess: Any){
        var message = ""
        if (mess is Int)
            message = getString(mess)
        else if (mess is String)
            message = mess

        activity?.let {
            MaterialAlertDialogBuilder(activity!!)
                .setMessage(message)
                .setPositiveButton(R.string.retry) { dialog, which ->
                    dialog.dismiss()
                    doRetry()
                }
                .setNegativeButton(android.R.string.cancel){dialog, which ->
                    dialog.dismiss()
                    afterClickedCancel()
                }
                .setCancelable(false)
                .show()
        }
    }

    fun showLoading(show : Boolean){
        mainActivity?.let {
            it.showLoading(show)
        }
    }


    abstract fun doRetry()

    open fun afterClickedCancel(){}
}