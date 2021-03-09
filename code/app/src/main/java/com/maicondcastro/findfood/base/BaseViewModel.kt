package com.maicondcastro.findfood.base

import android.app.Application
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maicondcastro.findfood.utils.SingleLiveEvent

abstract class BaseViewModel(app: Application) : AndroidViewModel(app) {

    val navigationCommand: SingleLiveEvent<NavigationCommand> = SingleLiveEvent()
    val showLoading: SingleLiveEvent<Boolean> = SingleLiveEvent()
    val showSnackBar: SingleLiveEvent<String> = SingleLiveEvent()
    val showSnackBarAction: SingleLiveEvent<SnackBarAction> = SingleLiveEvent()
    var showNoData: MutableLiveData<Boolean> = MutableLiveData()

    data class SnackBarAction(
        @StringRes
        val message: Int,
        @StringRes
        val btnText: Int,
        val action: (View) -> Unit
    )
}