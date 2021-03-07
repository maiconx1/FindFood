package com.maicondcastro.findfood.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.maicondcastro.findfood.utils.SingleLiveEvent

abstract class BaseViewModel(app: Application) : AndroidViewModel(app) {

    val navigationCommand: SingleLiveEvent<NavigationCommand> = SingleLiveEvent()
    val showLoading: SingleLiveEvent<Boolean> = SingleLiveEvent()
    val showSnackBar: SingleLiveEvent<String> = SingleLiveEvent()
    val showSnackBarInt: SingleLiveEvent<Int> = SingleLiveEvent()
    val showNoData: MutableLiveData<Boolean> = MutableLiveData()
}