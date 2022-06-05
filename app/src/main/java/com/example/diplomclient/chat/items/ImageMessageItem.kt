package com.example.diplomclient.chat.items

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import com.aita.adapter.composable.AbsDelegateViewHolder
import com.aita.adapter.composable.AdapterDelegate
import com.aita.adapter.composable.DelegateDiffable
import com.bumptech.glide.RequestManager
import com.example.diplomclient.R

data class ImageMessageCell(
    val id: String,
    val imageSource: ImageSource,
    val dateText: String = "",
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
    private val toImageView = itemView.findViewById<ImageView>(R.id.to_iv)
    private val toContainer = itemView.findViewById<View>(R.id.to_container)

    private val fromImageView = itemView.findViewById<ImageView>(R.id.from_iv)
    private val fromContainer = itemView.findViewById<View>(R.id.from_container)

    private val context = itemView.context
    private var latestCell: ImageMessageCell? = null

    private val progressbar = itemView.findViewById<ProgressBar>(R.id.pb)

    init {
        itemView.setOnClickListener {
            latestCell?.let(onImageClick)
        }
    }

    override fun bind(cell: ImageMessageCell, payloads: List<Any>?) {
        latestCell = cell

        if (cell.isMine) {
            toContainer.visibility = View.VISIBLE
            fromContainer.visibility = View.GONE
        } else {
            toContainer.visibility = View.GONE
            fromContainer.visibility = View.VISIBLE
        }

        val targetImageView = if (cell.isMine) toImageView else fromImageView
        when (val source = cell.imageSource) {
            is ImageMessageCell.ImageSource.Url -> {
                requestManager
                    .load(source.url)
                    .into(targetImageView)
            }
            is ImageMessageCell.ImageSource.LoadedBitmap -> {
                requestManager
                    .load(source.bitmap)
                    .into(targetImageView)
            }
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
