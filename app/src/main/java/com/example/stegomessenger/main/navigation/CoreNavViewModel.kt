package com.example.stegomessenger.main.navigation

import androidx.lifecycle.LiveData
import com.example.stegomessenger.arch.SingleEventLiveData
import com.example.stegomessenger.arch.infra.AbsViewModel
import com.example.stegomessenger.arch.redux.dispatcher.Dispatcher
import com.example.stegomessenger.arch.util.Prefs
import com.example.stegomessenger.common.PrefsContract
import com.example.stegomessenger.common.launchBackgroundWork
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class CoreNavViewModel @Inject constructor(
    dispatcher: Dispatcher,
    coreNavReducer: CoreNavReducer,
    coreMiddleware: CoreMiddleware,
    prefs: Prefs
) :
    AbsViewModel(dispatcher) {

    private val _navigationLiveData = SingleEventLiveData<CoreNav>()
    val navigationLiveData: LiveData<CoreNav> = _navigationLiveData

    private val _errorLiveData = SingleEventLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData

    init {
        attachManagedStore(
            initialState = CoreNavState.EMPTY,
            reducer = coreNavReducer,
            middleware = listOf(coreMiddleware),
        ) { newState: CoreNavState ->
            newState.navigationEvent?.readValue()?.let {
                _navigationLiveData.value = it
            }
            newState.errorEvent?.readValue()?.let {
                _errorLiveData.value = it
            }
        }
        launchBackgroundWork {
            dispatch(CoreAction.ReloadChats)
            delay(5000) // TODO: delete
            dispatch(CoreAction.SyncWithServer)
        }
        performInitialNavigation(prefs)
    }

    private fun performInitialNavigation(prefs: Prefs) {
        val savedToken = prefs.getString(PrefsContract.TOKEN, null)
        if (savedToken == null) {
            dispatch(CoreAction.ShowLogin)
        } else {
            dispatch(CoreAction.ShowOverviewFragment)
        }
    }
}
