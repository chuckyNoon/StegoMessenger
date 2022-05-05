package com.example.diplomclient.test.model

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.aita.adapter.composable.AbsDelegateViewHolder
import com.aita.adapter.composable.AdapterDelegate
import com.aita.adapter.composable.DelegateDiffable
import com.example.diplomclient.R

data class MessageCell(
    val text: String
) : DelegateDiffable<MessageCell> {

    override fun isSame(other: DelegateDiffable<*>): Boolean =
        other is MessageCell && text == other.text
}

class MessageHolder(
    private val parent: ViewGroup,
    private val inflater: LayoutInflater
) : AbsDelegateViewHolder<MessageCell>(
    inflater.inflate(R.layout.message_item, parent, false)
) {

    private val textTv = itemView.findViewById<TextView>(R.id.text_tv)

    override fun bind(cell: MessageCell, payloads: List<Any>?) {
        textTv.text = cell.text
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
