package com.aita.adapter.composable

interface DelegateDiffable<in C> {

    fun isSame(other: DelegateDiffable<*>): Boolean

    fun getChangePayload(newCell: C): Any? = null
}
