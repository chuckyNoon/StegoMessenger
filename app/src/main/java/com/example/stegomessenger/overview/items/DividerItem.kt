package com.example.stegomessenger.overview.items

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.stegomessenger.arch.adapter.AbsDelegateViewHolder
import com.example.stegomessenger.arch.adapter.AdapterDelegate
import com.example.stegomessenger.arch.adapter.DelegateDiffable
import com.example.stegomessenger.R

object DividerCell : DelegateDiffable<DividerCell> {

    override fun isSame(other: DelegateDiffable<*>): Boolean =
        other is DividerCell
}

class DividerHolder(
    parent: ViewGroup,
    inflater: LayoutInflater
) : AbsDelegateViewHolder<DividerCell>(
    inflater.inflate(R.layout.item_divider, parent, false)
) {
    override fun bind(cell: DividerCell, payloads: List<Any>?) = Unit
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
