package com.example.diplomclient.content_dialog

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aita.arch.di.regular.AppDepsProvider
import com.example.diplomclient.arch.infra.AbsViewModel

class ContentViewModel(app: Application, appDepsProvider: AppDepsProvider) :
    AbsViewModel(app, appDepsProvider) {

    private val _viewStateLiveData: MutableLiveData<ContentViewState> = MutableLiveData()
    val viewStateLiveData: LiveData<ContentViewState> = _viewStateLiveData

    init {
        attachManagedStore(
            initialState = ContentState.EMPTY,
            reducer = ContentReducer(),
            middleware = listOf(
                ContentMiddleware()
            ),
        ) { newState: ContentState ->
            _viewStateLiveData.value = newState.viewState
        }
    }
}