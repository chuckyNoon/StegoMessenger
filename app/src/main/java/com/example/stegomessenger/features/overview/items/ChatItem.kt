package com.example.stegomessenger.features.overview.items

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.RequestManager
import com.example.stegomessenger.R
import com.example.stegomessenger.arch.adapter.AbsDelegateViewHolder
import com.example.stegomessenger.arch.adapter.AdapterDelegate
import com.example.stegomessenger.arch.adapter.DelegateDiffable
import com.example.stegomessenger.common.view.LettersAvatarDrawable

data class ChatCell(
    val id: String,
    val chatTitleText: String,
    val messageText: String,
    val dateText: String,
    val initialsText: String
) : DelegateDiffable<ChatCell> {

    override fun isSame(other: DelegateDiffable<*>): Boolean =
        other is ChatCell && id == other.id
}

class ChatHolder(
    parent: ViewGroup,
    inflater: LayoutInflater,
    private val requestManager: RequestManager,
    private val onChatClick: (ChatCell) -> Unit
) : AbsDelegateViewHolder<ChatCell>(
    inflater.inflate(R.layout.item_chat, parent, false)
) {

    private val chatTextView = itemView.findViewById<TextView>(R.id.chat_tv)
    private val messageTextView = itemView.findViewById<TextView>(R.id.message_tv)
    private val dateTextView = itemView.findViewById<TextView>(R.id.date_tv)
    private val avatarImageView = itemView.findViewById<ImageView>(R.id.avatar_iv)

    private var latestCell: ChatCell? = null

    init {
        itemView.setOnClickListener {
            latestCell?.let(onChatClick)
        }
    }

    override fun bind(cell: ChatCell, payloads: List<Any>?) {
        latestCell = cell

        chatTextView.text = cell.chatTitleText
        dateTextView.text = cell.dateText
        messageTextView.text = cell.messageText

        requestManager
            .load(LettersAvatarDrawable(letters = cell.initialsText))
            .into(avatarImageView)
    }
}

class ChatAdapterDelegate(
    private val inflater: LayoutInflater,
    private val requestManager: RequestManager,
    private val onChatClick: ((ChatCell) -> Unit)
) : AdapterDelegate<ChatCell, ChatHolder> {

    override val cellClass: Class<ChatCell> = ChatCell::class.java

    override fun onCreateViewHolder(parent: ViewGroup): ChatHolder =
        ChatHolder(
            parent,
            inflater,
            requestManager,
            onChatClick
        )
}
