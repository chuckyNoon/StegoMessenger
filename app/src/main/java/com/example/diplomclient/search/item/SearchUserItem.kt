package com.example.diplomclient.search.item

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.aita.adapter.composable.AbsDelegateViewHolder
import com.aita.adapter.composable.AdapterDelegate
import com.aita.adapter.composable.DelegateDiffable
import com.example.diplomclient.R

data class SearchUserCell(
    val nameText: String,
    val idText: String
) : DelegateDiffable<SearchUserCell> {

    override fun isSame(other: DelegateDiffable<*>): Boolean =
        other is SearchUserCell
}

class SearchUserHolder(
    parent: ViewGroup,
    inflater: LayoutInflater,
    private val onClick: ((SearchUserCell) -> Unit)
) : AbsDelegateViewHolder<SearchUserCell>(
    inflater.inflate(R.layout.item_message, parent, false)
) {

    private val nameTextView = itemView.findViewById<TextView>(R.id.name_tv)
    private val idTextView = itemView.findViewById<TextView>(R.id.id_tv)

    private var latestCell: SearchUserCell? = null

    init {
        itemView.setOnClickListener {
            latestCell?.let(onClick)
        }
    }

    override fun bind(cell: SearchUserCell, payloads: List<Any>?) {
        latestCell = cell

        nameTextView.text = cell.nameText
        idTextView.text = cell.idText
    }
}

class SearchUserDelegate(
    private val inflater: LayoutInflater,
    private val onClick: ((SearchUserCell) -> Unit)
) : AdapterDelegate<SearchUserCell, SearchUserHolder> {

    override val cellClass: Class<SearchUserCell> = SearchUserCell::class.java

    override fun onCreateViewHolder(parent: ViewGroup): SearchUserHolder =
        SearchUserHolder(
            parent,
            inflater,
            onClick
        )
}
