package com.example.diplomclient.overview.model

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.aita.adapter.composable.AbsDelegateViewHolder
import com.aita.adapter.composable.AdapterDelegate
import com.aita.adapter.composable.DelegateDiffable
import com.bumptech.glide.RequestManager
import com.example.diplomclient.R

data class MessageCell(
    val contentText: String,
    val dateText: String,
    val isMine: Boolean,
    val image: Bitmap?
) : DelegateDiffable<MessageCell> {

    override fun isSame(other: DelegateDiffable<*>): Boolean =
        other is MessageCell && this.contentText == other.contentText

    override fun getChangePayload(newCell: MessageCell): Any = newCell
}

class MessageHolder(
    parent: ViewGroup,
    inflater: LayoutInflater,
    private val requestManager: RequestManager
) : AbsDelegateViewHolder<MessageCell>(
    inflater.inflate(R.layout.item_message, parent, false)
) {

    private val toDateTextView = itemView.findViewById<TextView>(R.id.to_date_tv)
    private val toContentTextView = itemView.findViewById<TextView>(R.id.to_content_tv)
    private val toContainer = itemView.findViewById<View>(R.id.to_container)
    private val toImageView = itemView.findViewById<ImageView>(R.id.to_iv)

    private val fromDateTextView = itemView.findViewById<TextView>(R.id.from_date_tv)
    private val fromContentTextView = itemView.findViewById<TextView>(R.id.from_content_tv)
    private val fromContainer = itemView.findViewById<View>(R.id.from_container)

    override fun bind(cell: MessageCell, payloads: List<Any>?) {
        if (cell.isMine) {
            fromContainer.visibility = View.GONE
            toContainer.visibility = View.VISIBLE

            toContentTextView.text = cell.contentText
            toDateTextView.text = cell.dateText
            if (cell.image == null) {
                toImageView.visibility = View.GONE
            } else {
                toImageView.visibility = View.VISIBLE
                requestManager.load(cell.image).into(toImageView)
            }
        } else {
            fromContainer.visibility = View.VISIBLE
            toContainer.visibility = View.GONE
            toImageView.visibility = View.GONE

            fromContentTextView.text = cell.contentText
            fromDateTextView.text = cell.dateText
        }
    }
}

class MessageAdapterDelegate(
    private val inflater: LayoutInflater,
    private val requestManager: RequestManager
) : AdapterDelegate<MessageCell, MessageHolder> {

    override val cellClass: Class<MessageCell> = MessageCell::class.java

    override fun onCreateViewHolder(parent: ViewGroup): MessageHolder =
        MessageHolder(
            parent,
            inflater,
            requestManager
        )

    override fun isUsingCellAsPayload(): Boolean = true
}
