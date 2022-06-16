package com.example.diplomclient.arch.adapter

interface Diffable<in T> {
    fun isSame(other: T): Boolean
    fun isContentsSame(other: T): Boolean
}
