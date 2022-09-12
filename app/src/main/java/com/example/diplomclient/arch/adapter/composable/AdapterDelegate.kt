package com.example.diplomclient.arch.adapter.composable

import android.view.ViewGroup

interface AdapterDelegate<C : DelegateDiffable<C>, H : AbsDelegateViewHolder<C>> {

    val cellClass: Class<C>

    fun onCreateViewHolder(parent: ViewGroup): H

    fun isUsingCellAsPayload(): Boolean = false

    fun onBindViewHolder(
        holder: H,
        cell: C,
        payloads: List<Any>? = null,
    ) {
        if (!isUsingCellAsPayload()) {
            holder.bind(cell = cell, payloads = payloads)
            return
        }

        val latestCell = if (payloads.isNullOrEmpty()) {
            cell
        } else {
            @Suppress("UNCHECKED_CAST")
            payloads.last() as C
        }
        holder.bind(
            cell = latestCell,
            payloads = payloads,
        )
    }
}
