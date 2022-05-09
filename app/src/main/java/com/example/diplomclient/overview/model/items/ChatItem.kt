package com.example.diplomclient.overview.model

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.aita.adapter.composable.AbsDelegateViewHolder
import com.aita.adapter.composable.AdapterDelegate
import com.aita.adapter.composable.DelegateDiffable
import com.example.diplomclient.R

data class ChatCell(
    val id: String,
    val nameText: String,
    val dateText: String
) : DelegateDiffable<ChatCell> {

    override fun isSame(other: DelegateDiffable<*>): Boolean =
        other is ChatCell && id == other.id
}

class ChatHolder(
    parent: ViewGroup,
    inflater: LayoutInflater,
    private val onChatClick: (ChatCell) -> Unit
) : AbsDelegateViewHolder<ChatCell>(
    inflater.inflate(R.layout.item_chat, parent, false)
) {

    private val nameTextView = itemView.findViewById<TextView>(R.id.name_tv)
    private val dateTextView = itemView.findViewById<TextView>(R.id.date_tv)

    private var latestCell: ChatCell? = null

    init {
        itemView.setOnClickListener {
            latestCell?.let(onChatClick)
        }
    }

    override fun bind(cell: ChatCell, payloads: List<Any>?) {
        latestCell = cell

        nameTextView.text = cell.nameText
        dateTextView.text = cell.dateText
    }
}

class ChatAdapterDelegate(
    private val inflater: LayoutInflater,
    private val onChatClick: ((ChatCell) -> Unit)
) : AdapterDelegate<ChatCell, ChatHolder> {

    override val cellClass: Class<ChatCell> = ChatCell::class.java

    override fun onCreateViewHolder(parent: ViewGroup): ChatHolder =
        ChatHolder(
            parent,
            inflater,
            onChatClick
        )
}
