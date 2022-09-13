package com.example.stegomessenger.content_dialog

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.stegomessenger.arch.util.AppDepsProvider
import com.example.stegomessenger.arch.infra.AbsViewModel

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