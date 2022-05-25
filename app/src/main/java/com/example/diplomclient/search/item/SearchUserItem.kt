package com.example.diplomclient.search.item

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.aita.adapter.composable.AbsDelegateViewHolder
import com.aita.adapter.composable.AdapterDelegate
import com.aita.adapter.composable.DelegateDiffable
import com.bumptech.glide.RequestManager
import com.example.diplomclient.R
import com.example.diplomclient.common.ColoredText
import com.example.diplomclient.common.setColoredText
import com.example.diplomclient.common.view.LettersAvatarDrawable

data class SearchUserCell(
    val nameText: ColoredText,
    val idText: ColoredText
) : DelegateDiffable<SearchUserCell> {

    override fun isSame(other: DelegateDiffable<*>): Boolean =
        other is SearchUserCell
}

class SearchUserHolder(
    parent: ViewGroup,
    inflater: LayoutInflater,
    private val requestManager: RequestManager,
    private val onClick: ((SearchUserCell) -> Unit)
) : AbsDelegateViewHolder<SearchUserCell>(
    inflater.inflate(R.layout.item_search_user, parent, false)
) {

    private val nameTextView = itemView.findViewById<TextView>(R.id.name_tv)
    private val idTextView = itemView.findViewById<TextView>(R.id.id_tv)
    private val avatarImageView = itemView.findViewById<ImageView>(R.id.avatar_iv)

    private var latestCell: SearchUserCell? = null

    init {
        itemView.setOnClickListener {
            latestCell?.let(onClick)
        }
    }

    override fun bind(cell: SearchUserCell, payloads: List<Any>?) {
        latestCell = cell

        nameTextView.setColoredText(cell.nameText)
        idTextView.setColoredText(cell.idText)

        val initials = cell.nameText.value.split(" ")
        val firstInitial = initials[0].uppercase()
        val fullText = if (initials.size > 1) {
            firstInitial + initials[1].uppercase()
        } else {
            firstInitial
        }
        requestManager.load(
            LettersAvatarDrawable(itemView.context, fullText)
        ).into(avatarImageView)
    }
}

class SearchUserDelegate(
    private val inflater: LayoutInflater,
    private val requestManager: RequestManager,
    private val onClick: ((SearchUserCell) -> Unit)
) : AdapterDelegate<SearchUserCell, SearchUserHolder> {

    override val cellClass: Class<SearchUserCell> = SearchUserCell::class.java

    override fun onCreateViewHolder(parent: ViewGroup): SearchUserHolder =
        SearchUserHolder(
            parent,
            inflater,
            requestManager,
            onClick
        )
}
