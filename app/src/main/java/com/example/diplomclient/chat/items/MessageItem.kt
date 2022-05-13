package com.example.diplomclient.overview.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.aita.adapter.composable.AbsDelegateViewHolder
import com.aita.adapter.composable.AdapterDelegate
import com.aita.adapter.composable.DelegateDiffable
import com.example.diplomclient.R

data class MessageCell(
    val contentText: String,
    val dateText: String,
    val isMine: Boolean
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

    private val toDateTextView = itemView.findViewById<TextView>(R.id.to_date_tv)
    private val toContentTextView = itemView.findViewById<TextView>(R.id.to_content_tv)
    private val toContainer = itemView.findViewById<View>(R.id.to_container)

    private val fromDateTextView = itemView.findViewById<TextView>(R.id.from_date_tv)
    private val fromContentTextView = itemView.findViewById<TextView>(R.id.from_content_tv)
    private val fromContainer = itemView.findViewById<View>(R.id.from_container)

    override fun bind(cell: MessageCell, payloads: List<Any>?) {
        if (cell.isMine) {
            fromContainer.visibility = View.GONE
            toContainer.visibility = View.VISIBLE

            toContentTextView.text = cell.contentText
            toDateTextView.text = cell.dateText
        } else {
            fromContainer.visibility = View.VISIBLE
            toContainer.visibility = View.GONE

            fromContentTextView.text = cell.contentText
            fromDateTextView.text = cell.dateText
        }
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
