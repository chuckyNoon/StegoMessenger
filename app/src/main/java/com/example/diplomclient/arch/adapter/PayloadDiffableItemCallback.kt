package com.aita.adapter

import androidx.recyclerview.widget.DiffUtil

internal class PayloadDiffableItemCallback<T : PayloadDiffable<T>> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = oldItem.isSame(newItem)
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = oldItem.isContentsSame(newItem)
    override fun getChangePayload(oldItem: T, newItem: T): Any? = oldItem.getChangePayload(newItem)
}
