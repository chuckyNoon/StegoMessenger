package com.example.diplomclient.overview.model

import android.view.LayoutInflater
import android.view.ViewGroup
import com.aita.adapter.composable.AbsDelegateViewHolder
import com.aita.adapter.composable.AdapterDelegate
import com.aita.adapter.composable.DelegateDiffable
import com.example.diplomclient.R

object DividerCell : DelegateDiffable<DividerCell> {

    override fun isSame(other: DelegateDiffable<*>): Boolean =
        other is ChatCell
}

class DividerHolder(
    parent: ViewGroup,
    inflater: LayoutInflater
) : AbsDelegateViewHolder<DividerCell>(
    inflater.inflate(R.layout.item_divider, parent, false)
) {
    override fun bind(cell: DividerCell, payloads: List<Any>?) {
    }
}

class DividerAdapterDelegate(
    private val inflater: LayoutInflater,
) : AdapterDelegate<DividerCell, DividerHolder> {

    override val cellClass: Class<DividerCell> = DividerCell::class.java

    override fun onCreateViewHolder(parent: ViewGroup): DividerHolder =
        DividerHolder(
            parent,
            inflater
        )
}
