package com.aita.adapter

interface PayloadDiffable<in T> : Diffable<T> {
    fun getChangePayload(newItem: T): Any?
}
