package com.example.diplomclient.overview.model

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.aita.adapter.composable.AbsDelegateViewHolder
import com.aita.adapter.composable.AdapterDelegate
import com.aita.adapter.composable.DelegateDiffable
import com.example.diplomclient.R

data class MessageCell(
    val contentText: String,
    val dateText: String
) : DelegateDiffable<MessageCell> {

    override fun isSame(other: DelegateDiffable<*>): Boolean =
        other is MessageCell && contentText == other.contentText
}

class MessageHolder(
    parent: ViewGroup,
    inflater: LayoutInflater
) : AbsDelegateViewHolder<MessageCell>(
    inflater.inflate(R.layout.item_message, parent, false)
) {

    private val contentTextView = itemView.findViewById<TextView>(R.id.name_tv)
    private val dateTextView = itemView.findViewById<TextView>(R.id.date_tv)

    override fun bind(cell: MessageCell, payloads: List<Any>?) {
        contentTextView.text = cell.contentText
        dateTextView.text = cell.dateText
    }
}

class MessageAdapterDelegate(
    private val inflater: LayoutInflater,
) : AdapterDelegate<MessageCell, MessageHolder> {

    override val cellClass: Class<MessageCell> = MessageCell::class.java

    override fun onCreateViewHolder(parent: ViewGroup): MessageHolder =
        MessageHolder(
            parent,
            inflater
        )
}
