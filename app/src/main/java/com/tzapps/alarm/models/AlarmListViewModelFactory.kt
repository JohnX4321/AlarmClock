package com.tzapps.alarm.models

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AlarmListViewModelFactory(val app: Application) : ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AlarmsListViewModel(app) as T
    }
}