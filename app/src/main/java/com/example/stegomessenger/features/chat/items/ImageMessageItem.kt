package com.example.stegomessenger.features.chat.items

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.RequestManager
import com.example.stegomessenger.R
import com.example.stegomessenger.arch.adapter.AbsDelegateViewHolder
import com.example.stegomessenger.arch.adapter.AdapterDelegate
import com.example.stegomessenger.arch.adapter.DelegateDiffable

data class ImageMessageCell(
    val id: String,
    val imageSource: ImageSource,
    val dateText: String,
    val isMine: Boolean,
    val isInProgress: Boolean
) : DelegateDiffable<ImageMessageCell> {

    sealed class ImageSource {
        data class Url(val url: String) : ImageSource()
        data class LoadedBitmap(val bitmap: Bitmap) : ImageSource()
    }

    override fun isSame(other: DelegateDiffable<*>): Boolean =
        other is ImageMessageCell && id == other.id

    override fun getChangePayload(newCell: ImageMessageCell): Any = newCell
}

class ImageMessageHolder(
    parent: ViewGroup,
    inflater: LayoutInflater,
    private val requestManager: RequestManager,
    private val onImageClick: (ImageMessageCell) -> Unit
) : AbsDelegateViewHolder<ImageMessageCell>(
    inflater.inflate(R.layout.item_image_message, parent, false)
) {
    private val sendedImageView = itemView.findViewById<ImageView>(R.id.sended_iv)
    private val sendedContainer = itemView.findViewById<View>(R.id.sent_container)

    private val receivedImageView = itemView.findViewById<ImageView>(R.id.received_iv)
    private val receivedContainer = itemView.findViewById<View>(R.id.received_container)

    private val progressbar = itemView.findViewById<ProgressBar>(R.id.pb)

    private var latestCell: ImageMessageCell? = null

    init {
        itemView.setOnClickListener {
            latestCell?.let(onImageClick)
        }
    }

    override fun bind(cell: ImageMessageCell, payloads: List<Any>?) {
        latestCell = cell

        if (cell.isMine) {
            sendedContainer.visibility = View.VISIBLE
            receivedContainer.visibility = View.GONE
        } else {
            sendedContainer.visibility = View.GONE
            receivedContainer.visibility = View.VISIBLE
        }

        val targetImageView = if (cell.isMine) sendedImageView else receivedImageView
        when (val source = cell.imageSource) {
            is ImageMessageCell.ImageSource.Url ->
                requestManager
                    .load(source.url)
                    .into(targetImageView)

            is ImageMessageCell.ImageSource.LoadedBitmap ->
                requestManager
                    .load(source.bitmap)
                    .into(targetImageView)

        }

        if (cell.isInProgress) {
            progressbar.visibility = View.VISIBLE
        } else {
            progressbar.visibility = View.GONE
        }
    }
}

class ImageMessageDelegate(
    private val inflater: LayoutInflater,
    private val requestManager: RequestManager,
    private val onImageClick: ((ImageMessageCell) -> Unit)
) : AdapterDelegate<ImageMessageCell, ImageMessageHolder> {

    override val cellClass: Class<ImageMessageCell> = ImageMessageCell::class.java

    override fun onCreateViewHolder(parent: ViewGroup): ImageMessageHolder =
        ImageMessageHolder(
            parent,
            inflater,
            requestManager,
            onImageClick
        )

    override fun isUsingCellAsPayload(): Boolean = true
}
