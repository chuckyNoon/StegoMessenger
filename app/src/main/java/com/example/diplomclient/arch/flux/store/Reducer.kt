package com.aita.arch.store

import androidx.annotation.CheckResult
import com.example.diplomclient.arch.flux.Action

interface Reducer<T : Any> {

    @CheckResult
    fun acceptsAction(action: Action): Boolean

    fun reduce(oldState: T, action: Action): T
}
