package com.example.diplomclient.arch.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class AbsDelegateViewHolder<C : DelegateDiffable<C>>(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    abstract fun bind(cell: C, payloads: List<Any>? = null)
}
