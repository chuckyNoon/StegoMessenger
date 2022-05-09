package com.example.diplomclient.test

import android.util.Log
import com.aita.arch.dispatcher.Dispatchable
import com.aita.arch.store.Middleware
import com.example.diplomclient.arch.flux.Action
import com.example.diplomclient.arch.network.ApiHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

class TestMiddleware(
    private val apiHelper: ApiHelper
) : Middleware<TestState> {

    override fun onReduced(
        dispatchable: Dispatchable,
        action: Action,
        oldState: TestState,
        newState: TestState
    ) {
        when (action) {
            is TestAction.ClickLoad ->
                GlobalScope.launch(Dispatchers.Main) {
                    loadMessage(dispatchable)
                }
        }
    }

    private suspend fun loadMessage(dispatchable: Dispatchable) {
        try {
            val resp = apiHelper.getResponse()
            Log.d("ff", "r")
            dispatchable.dispatch(TestAction.DataLoaded(resp.value))
        } catch (e: Exception) {
            Log.d("ff", "fail" + e.message.toString() + " " + e.stackTraceToString())
        }
    }
}
