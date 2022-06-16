package com.example.diplomclient.arch.adapter

import androidx.recyclerview.widget.DiffUtil

internal class DiffableItemCallback<T : Diffable<T>> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = oldItem.isSame(newItem)
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = oldItem.isContentsSame(newItem)
}
