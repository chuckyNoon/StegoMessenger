package com.example.stegomessenger.arch.infra

import androidx.annotation.CallSuper
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import com.example.stegomessenger.arch.redux.dispatcher.Dispatchable
import com.example.stegomessenger.arch.redux.dispatcher.Dispatcher
import com.example.stegomessenger.arch.redux.dispatcher.NewStateListener
import com.example.stegomessenger.arch.redux.disposable.Disposable
import com.example.stegomessenger.arch.redux.store.Middleware
import com.example.stegomessenger.arch.redux.store.Reducer
import com.example.stegomessenger.arch.redux.store.Store

abstract class AbsViewModel1(
    private val dispatcher: Dispatcher
) :
    ViewModel(),
    Dispatchable by dispatcher {

    private val disposables = mutableListOf<Disposable>()

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        for (disposable in disposables) {
            disposable.dispose()
        }
    }

    @MainThread
    protected fun <T : Any> attachManagedStore(
        initialState: T,
        reducer: Reducer<T>,
        middleware: List<Middleware<T>> = emptyList(),
        newStateListener: NewStateListener<T>,
    ): Unit = keepTrackOf(
        disposable = dispatcher.attachStore(
            Store(initialState, reducer, middleware),
            newStateListener,
        )
    )

    @MainThread
    protected fun keepTrackOf(disposable: Disposable) {
        disposables.add(disposable)
    }
}