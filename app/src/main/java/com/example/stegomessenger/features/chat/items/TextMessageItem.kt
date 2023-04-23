package com.example.stegomessenger.features.chat.items

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.stegomessenger.R
import com.example.stegomessenger.arch.adapter.AbsDelegateViewHolder
import com.example.stegomessenger.arch.adapter.AdapterDelegate
import com.example.stegomessenger.arch.adapter.DelegateDiffable

data class TextMessageCell(
    val id: String,
    val contentText: String,
    val dateText: String,
    val isMine: Boolean,
) : DelegateDiffable<TextMessageCell> {

    override fun isSame(other: DelegateDiffable<*>): Boolean =
        other is TextMessageCell && this.id == other.id

    override fun getChangePayload(newCell: TextMessageCell): Any = newCell
}

class TextMessageHolder(
    parent: ViewGroup,
    inflater: LayoutInflater
) : AbsDelegateViewHolder<TextMessageCell>(
    inflater.inflate(R.layout.item_text_message, parent, false)
) {

    private val sentDateTextView = itemView.findViewById<TextView>(R.id.sent_date_tv)
    private val sentContentTextView = itemView.findViewById<TextView>(R.id.sent_content_tv)
    private val sentContainer = itemView.findViewById<View>(R.id.sent_container)

    private val receivedDateTextView = itemView.findViewById<TextView>(R.id.received_date_tv)
    private val receivedContentTextView = itemView.findViewById<TextView>(R.id.received_content_tv)
    private val receivedContainer = itemView.findViewById<View>(R.id.received_container)

    private var latestCell: TextMessageCell? = null

    override fun bind(cell: TextMessageCell, payloads: List<Any>?) {
        latestCell = cell

        if (cell.isMine) {
            receivedContainer.visibility = View.GONE
            sentContainer.visibility = View.VISIBLE

            sentContentTextView.text = cell.contentText
            sentDateTextView.text = cell.dateText
        } else {
            receivedContainer.visibility = View.VISIBLE
            sentContainer.visibility = View.GONE

            receivedContentTextView.text = cell.contentText
            receivedDateTextView.text = cell.dateText
        }
    }
}

class TextMessageAdapterDelegate(private val inflater: LayoutInflater) :
    AdapterDelegate<TextMessageCell, TextMessageHolder> {

    override val cellClass: Class<TextMessageCell> = TextMessageCell::class.java

    override fun onCreateViewHolder(parent: ViewGroup): TextMessageHolder =
        TextMessageHolder(
            parent,
            inflater
        )

    override fun isUsingCellAsPayload(): Boolean = true
}
