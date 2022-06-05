package com.example.diplomclient.content_dialog

import com.aita.adapter.composable.DelegateDiffable
import com.aita.arch.store.Reducer
import com.example.diplomclient.arch.flux.Action
import com.example.diplomclient.chat.items.ImageMessageCell
import com.example.diplomclient.overview.model.TextMessageCell

class ContentReducer : Reducer<ContentState> {
    override fun acceptsAction(action: Action): Boolean = action is ContentAction

    override fun reduce(oldState: ContentState, action: Action): ContentState = when (action) {
        is ContentAction.Init -> {
            val newState = oldState.copy(
                type = when {
                    !action.text.isNullOrEmpty() -> ContentStateType.TEXT
                    action.image != null -> ContentStateType.IMAGE
                    else -> error("invalid action")
                },
                image = action.image,
                text = action.text
            )
            rebuildViewState(newState)
        }
        else -> oldState
    }

    private fun rebuildViewState(state: ContentState): ContentState {
        val cells = mutableListOf<DelegateDiffable<*>>()
        when (state.type) {
            ContentStateType.IMAGE -> cells.add(
                ImageMessageCell(
                    id = "1",
                    imageSource = ImageMessageCell.ImageSource.LoadedBitmap(state.image!!),
                    dateText = "",
                    isMine = false,
                    isInProgress = false
                )
            )
            ContentStateType.TEXT -> cells.add(
                TextMessageCell(
                    id = "1",
                    contentText = state.text!!,
                    dateText = "",
                    isMine = false,
                )
            )
        }
        return state.copy(
            viewState = ContentViewState(
                cells = cells
            )
        )
    }
}
