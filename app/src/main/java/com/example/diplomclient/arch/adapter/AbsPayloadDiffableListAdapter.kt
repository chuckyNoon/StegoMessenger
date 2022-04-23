package com.aita.adapter

import androidx.recyclerview.widget.RecyclerView

abstract class AbsPayloadDiffableListAdapter<T : PayloadDiffable<T>, H : RecyclerView.ViewHolder>(cells: List<T>) :
    AbsDiffableListAdapter<T, H>(cells, itemCallback = PayloadDiffableItemCallback<T>())
