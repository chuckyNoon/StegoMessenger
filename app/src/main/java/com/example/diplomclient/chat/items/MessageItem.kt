package com.example.diplomclient.overview.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.aita.adapter.composable.AbsDelegateViewHolder
import com.aita.adapter.composable.AdapterDelegate
import com.aita.adapter.composable.DelegateDiffable
import com.bumptech.glide.RequestManager
import com.example.diplomclient.R

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

    private val toDateTextView = itemView.findViewById<TextView>(R.id.to_date_tv)
    private val toContentTextView = itemView.findViewById<TextView>(R.id.to_content_tv)
    private val toContainer = itemView.findViewById<View>(R.id.to_container)

    private val fromDateTextView = itemView.findViewById<TextView>(R.id.from_date_tv)
    private val fromContentTextView = itemView.findViewById<TextView>(R.id.from_content_tv)
    private val fromContainer = itemView.findViewById<View>(R.id.from_container)

    private var latestCell: TextMessageCell? = null

    override fun bind(cell: TextMessageCell, payloads: List<Any>?) {
        latestCell = cell

        if (cell.isMine) {
            fromContainer.visibility = View.GONE
            toContainer.visibility = View.VISIBLE

            toContentTextView.text = cell.contentText
            toDateTextView.text = cell.dateText
            /*
            if (cell.image == null) {
                toImageView.visibility = View.GONE
            } else {
                toImageView.visibility = View.VISIBLE
                // requestManager.load("fef").into(toImageView)
                requestManager.load(cell.image).into(toImageView)
            }*/
        } else {
            fromContainer.visibility = View.VISIBLE
            toContainer.visibility = View.GONE

            fromContentTextView.text = cell.contentText
            fromDateTextView.text = cell.dateText
        }
    }
}

class TextMessageAdapterDelegate(
    private val inflater: LayoutInflater,
    private val requestManager: RequestManager,
    private val onImageClick: ((TextMessageCell) -> Unit)
) : AdapterDelegate<TextMessageCell, TextMessageHolder> {

    override val cellClass: Class<TextMessageCell> = TextMessageCell::class.java

    override fun onCreateViewHolder(parent: ViewGroup): TextMessageHolder =
        TextMessageHolder(
            parent,
            inflater
        )

    override fun isUsingCellAsPayload(): Boolean = true
}
