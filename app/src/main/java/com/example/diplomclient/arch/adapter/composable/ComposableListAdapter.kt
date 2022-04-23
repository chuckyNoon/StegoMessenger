package com.aita.adapter.composable

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.aita.adapter.AbsBlockingDiffableListAdapter
import com.aita.adapter.AbsDiffableListAdapter

private typealias Cell = DelegateDiffable<*>
private typealias CellDiffable = DelegateDiffable<Cell>
private typealias Holder = AbsDelegateViewHolder<CellDiffable>

class ComposableListAdapter @JvmOverloads constructor(
    delegates: List<AdapterDelegate<*, *>>,
    cells: List<Cell> = emptyList(),
    private val isUsedForNestedRecyclerView: Boolean = false,
    isDiffCalculationAsync: Boolean = true,
) :
    ListAdapter<Cell, Holder>(
        AsyncDifferConfig.Builder(DelegateDiffableItemCallback.newInstance())
            .setBackgroundThreadExecutor(
                if (isDiffCalculationAsync) {
                    AbsDiffableListAdapter.DIFF_EXECUTOR
                } else {
                    AbsBlockingDiffableListAdapter.BlockingExecutor
                }
            )
            .build()
    ) {

    init {
        if (cells.isNotEmpty()) {
            super.submitList(cells)
        }
    }

    private val cellClassToViewTypeMap: Map<Class<*>, Int>
    private val delegates: List<AdapterDelegate<CellDiffable, Holder>>

    init {
        val cellClassToViewTypeMap = mutableMapOf<Class<*>, Int>()
        val delegatesList = mutableListOf<AdapterDelegate<CellDiffable, Holder>>()
        for ((viewType, delegate) in delegates.withIndex()) {
            val cellClass = delegate.cellClass
            require(!cellClassToViewTypeMap.containsKey(cellClass)) {
                "More than one AdapterDelegate uses cellClass=$cellClass"
            }
            cellClassToViewTypeMap[cellClass] = viewType
            @Suppress("UNCHECKED_CAST")
            delegatesList.add(delegate as AdapterDelegate<CellDiffable, Holder>)
        }
        this.cellClassToViewTypeMap = cellClassToViewTypeMap
        this.delegates = delegatesList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val delegate = delegates[viewType]
        return delegate.onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val viewType = getItemViewType(position)
        val delegate = delegates[viewType]
        val cell = getItem(position)
        @Suppress("UNCHECKED_CAST")
        delegate.onBindViewHolder(
            holder = holder,
            cell = cell as CellDiffable,
            payloads = null,
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int, payloads: List<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
            return
        }

        val viewType = getItemViewType(position)
        val delegate = delegates[viewType]
        val cell = getItem(position)
        @Suppress("UNCHECKED_CAST")
        delegate.onBindViewHolder(
            holder = holder,
            cell = cell as CellDiffable,
            payloads = payloads,
        )
    }

    override fun getItemViewType(position: Int): Int {
        val cell = getItem(position)
        val cellClass = cell.javaClass
        val viewType = cellClassToViewTypeMap[cellClass]
        requireNotNull(viewType) {
            "No viewType found for cellClass=$cellClass. This should not happen!"
        }
        return viewType
    }

    override fun submitList(list: List<Cell>?) {
        require(!isUsedForNestedRecyclerView) { ERR_NOT_SUPPORTED }
        super.submitList(list)
    }

    override fun submitList(list: List<Cell>?, commitCallback: Runnable?) {
        require(!isUsedForNestedRecyclerView) { ERR_NOT_SUPPORTED }
        super.submitList(list, commitCallback)
    }

    companion object {
        private const val ERR_NOT_SUPPORTED: String =
            "submitList(...) method is not supported for " +
                "ComposableListAdapter with isUsedForNestedRecyclerView=true. " +
                "Use RecyclerView.swapAdapter(ComposableListAdapter(...), false) instead."
    }
}

private class DelegateDiffableItemCallback<T : DelegateDiffable<T>> : DiffUtil.ItemCallback<T>() {

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun newInstance(): DiffUtil.ItemCallback<Cell> =
            DelegateDiffableItemCallback<CellDiffable>() as DiffUtil.ItemCallback<Cell>
    }

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
        oldItem.isSame(newItem)

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
        oldItem == newItem

    override fun getChangePayload(oldItem: T, newItem: T): Any? =
        oldItem.getChangePayload(newItem)
}
