package com.example.diplomclient.arch.adapter

import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.Executor

abstract class AbsBlockingDiffableListAdapter<T : Diffable<T>, H : RecyclerView.ViewHolder>(cells: List<T>) :
    AbsDiffableListAdapter<T, H>(cells, calculateDiffExecutor = BlockingExecutor) {

    final override fun submitList(list: List<T>?) {
        super.submitList(list)
        throw UnsupportedOperationException(buildIllegalAccessMessage())
    }

    final override fun submitList(list: List<T>?, commitCallback: Runnable?) {
        super.submitList(list, commitCallback)
        throw UnsupportedOperationException(buildIllegalAccessMessage())
    }

    private fun buildIllegalAccessMessage(): String {
        return "AbsBlockingDiffableListAdapter inheritors do not support submitList(...) method. " +
            "Use recyclerView.swapAdapter(${javaClass.simpleName}(...), false) instead."
    }

    internal object BlockingExecutor : Executor {
        override fun execute(command: Runnable): Unit = command.run()
    }
}
