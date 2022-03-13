package com.jerry.mvi.base

import androidx.lifecycle.ViewModel
import com.jerry.mvi.R

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.net.SocketTimeoutException
import java.net.UnknownHostException


open class BaseViewModel () : ViewModel() {

    protected val job = SupervisorJob()


    protected val mUiScope = CoroutineScope(Dispatchers.Main + job) //Dispatchers.Main main thread , UI thread
    protected val mIoScope = CoroutineScope(Dispatchers.IO + job)// using for a job / something using long time like http call

    override fun onCleared() {
        super.onCleared()
        this.job.cancel()
    }

    fun returnError(t:Throwable) : Any{
        if (t is SocketTimeoutException)
            return R.string.time_out_error;
        else if (t is UnknownHostException)
            return R.string.unknown_host_error;
        else
            return t.toString()
    }

}