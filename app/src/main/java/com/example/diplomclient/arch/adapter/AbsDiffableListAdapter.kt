package com.aita.adapter

import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.Executor
import java.util.concurrent.Executors

abstract class AbsDiffableListAdapter<T : Diffable<T>, H : RecyclerView.ViewHolder> @JvmOverloads constructor(
    cells: List<T>,
    itemCallback: DiffUtil.ItemCallback<T> = DiffableItemCallback(),
    calculateDiffExecutor: Executor = DIFF_EXECUTOR
) : ListAdapter<T, H>(
    AsyncDifferConfig.Builder(itemCallback)
        .setBackgroundThreadExecutor(calculateDiffExecutor)
        .build()
) {

    init {
        super.submitList(cells)
    }

    companion object {
        internal val DIFF_EXECUTOR = Executors.newSingleThreadExecutor()
    }
}
