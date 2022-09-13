package com.example.diplomclient.content_dialog

import com.example.diplomclient.arch.adapter.DelegateDiffable
import com.example.diplomclient.arch.redux.store.Reducer
import com.example.diplomclient.arch.redux.Action
import com.example.diplomclient.chat.items.ImageMessageCell
import com.example.diplomclient.chat.items.TextMessageCell

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
        val displayCell = when (state.type) {
            ContentStateType.IMAGE -> {
                requireNotNull(state.image) {
                    "state.image should not be null when state.type is ContentStateType.IMAGE"
                }
                ImageMessageCell(
                    id = "hidden_image",
                    imageSource = ImageMessageCell.ImageSource.LoadedBitmap(state.image),
                    dateText = "",
                    isMine = false,
                    isInProgress = false
                )
            }
            ContentStateType.TEXT -> {
                requireNotNull(state.text) {
                    "state.text should not be null when state.type is ContentStateType.TEXT"
                }
                TextMessageCell(
                    id = "hidden_text",
                    contentText = state.text,
                    dateText = "",
                    isMine = false,
                )
            }
        }
        return state.copy(
            viewState = ContentViewState(
                cells = listOf(displayCell)
            )
        )
    }
}
