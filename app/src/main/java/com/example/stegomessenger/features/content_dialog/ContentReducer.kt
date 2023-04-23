package com.example.stegomessenger.features.content_dialog

import com.example.stegomessenger.arch.redux.store.Reducer
import com.example.stegomessenger.arch.redux.Action
import com.example.stegomessenger.features.chat.items.ImageMessageCell
import com.example.stegomessenger.features.chat.items.TextMessageCell
import javax.inject.Inject

class ContentReducer @Inject constructor(): Reducer<ContentState> {
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
                cells = listOf(displayCell),
                isDownloadButtonVisible = state.type == ContentStateType.IMAGE
            )
        )
    }
}
