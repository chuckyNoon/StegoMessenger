package com.example.stegomessenger.features.stego_dialog

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import com.example.stegomessenger.R
import com.example.stegomessenger.arch.redux.dispatcher.Dispatchable
import com.example.stegomessenger.arch.redux.store.Middleware
import com.example.stegomessenger.arch.redux.Action
import com.example.stegomessenger.arch.util.StringsProvider
import com.example.stegomessenger.common.getName
import com.example.stegomessenger.common.network.ApiService
import com.example.stegomessenger.domain.LsbAlgorithm
import com.example.stegomessenger.domain.StegoAlgorithm
import com.example.stegomessenger.domain.model.HiddenContent
import com.example.stegomessenger.app.core.CoreAction
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class StegoMiddleware @Inject constructor(
    private val context: Context,
    private val apiService: ApiService,
    private val stringsProvider: StringsProvider,
    private val algorithm: StegoAlgorithm
) : Middleware<StegoState>() {

    override fun onReduced(
        dispatchable: Dispatchable,
        action: Action,
        oldState: StegoState,
        newState: StegoState
    ) {
        when (action) {
            is StegoAction.ClickSend -> middlewareScope.launch {
                handleClickSend(dispatchable, newState, action)
            }
            else -> {}
        }
    }

    // TODO: rewrite this stuff, errors aren't handled properly
    private suspend fun handleClickSend(
        dispatchable: Dispatchable,
        newState: StegoState,
        action: StegoAction.ClickSend
    ) {
        val receiverId = newState.receiverId ?: return

        dispatchable.dispatch(StegoAction.SendingStarted)

        val containerUri = newState.containerUriStr?.let { Uri.parse(it) }
        val containerBitmap = containerUri?.let {
            MediaStore.Images.Media.getBitmap(action.contentResolver, it)
        }

        val onSendingSuccess = { _: Unit ->
            dispatchable.dispatch(StegoAction.SendingSuccess)
            dispatchable.dispatch(CoreAction.ReloadChats)
            dispatchable.dispatch(
                CoreAction.ShowToast(stringsProvider.getString(R.string.message_succefully_sent))
            )
            dispatchable.dispatch(StegoAction.Close)
        }

        val onSendingFail = { error: Throwable ->
            dispatchable.dispatch(StegoAction.SendingFail)
            dispatchable.dispatch(
                CoreAction.ShowToast(
                    error.message ?: stringsProvider.getString(R.string.message_succefully_sent)
                )
            )
        }

        when (newState.stateType) {
            StegoStateType.IMAGE -> {
                val contentUri = newState.contentUriStr?.let { Uri.parse(it) }
                val contentBitmap = contentUri?.let {
                    MediaStore.Images.Media.getBitmap(action.contentResolver, it)
                } ?: error("failed to parse content image")

                startSendImageRequest(
                    receiverId = receiverId,
                    containerUri = containerUri,
                    containerBitmap = containerBitmap,
                    contentBitmap = contentBitmap,
                    successCallback = onSendingSuccess,
                    failCallback = onSendingFail
                )
            }
            StegoStateType.TEXT -> {
                val text = newState.contentText ?: return
                sendText(
                    receiverId = receiverId,
                    text = text,
                    containerUri = containerUri,
                    containerBitmap = containerBitmap,
                    successCallback = onSendingSuccess,
                    failCallback = onSendingFail
                )
            }
        }
    }

    private suspend fun sendText(
        receiverId: String,
        text: String,
        containerUri: Uri?,
        containerBitmap: Bitmap?,
        successCallback: (Unit) -> Unit,
        failCallback: (Throwable) -> Unit
    ) {
        if (containerBitmap == null) {
            runCatching { apiService.sendText(receiverId = receiverId, text = text) }
                .onSuccess(successCallback)
                .onFailure(failCallback)
            return
        }

        val file = saveBitmapInFile(
            bitmap = algorithm.encodeBitmap(
                containerBitmap = containerBitmap,
                hiddenContent = HiddenContent.Text(text)
            ) ?: error("failed to generate stego-image"),
            fileName = containerUri?.getName(context) ?: "tmp.img"
        )

        runCatching { startSendImageRequest(receiverId, file) }
            .onSuccess(successCallback)
            .onFailure(failCallback)
    }

    private suspend fun startSendImageRequest(
        receiverId: String,
        containerUri: Uri?,
        containerBitmap: Bitmap?,
        contentBitmap: Bitmap,
        successCallback: ((Unit) -> Unit),
        failCallback: ((Throwable) -> Unit)
    ) {
        val bitmapToSend = if (containerBitmap != null) {
            LsbAlgorithm().encodeBitmap(
                containerBitmap = containerBitmap,
                hiddenContent = HiddenContent.Image(contentBitmap)
            ) ?: error("failed to generate stego-image")
        } else {
            contentBitmap
        }

        val file =
            saveBitmapInFile(bitmapToSend, fileName = containerUri?.getName(context) ?: "tmp.img")

        runCatching { startSendImageRequest(receiverId = receiverId, imageFile = file) }
            .onSuccess(successCallback)
            .onFailure(failCallback)
    }

    private suspend fun startSendImageRequest(receiverId: String, imageFile: File) =
        apiService.sendImage(
            file = MultipartBody.Part.createFormData(
                "image",
                imageFile.name,
                RequestBody.create("image/*".toMediaTypeOrNull(), imageFile)
            ),
            receiverId = MultipartBody.Part.createFormData("receiverId", receiverId)
        )

    private fun saveBitmapInFile(bitmap: Bitmap, fileName: String): File {
        val file = File(context.cacheDir, fileName)

        val os = BufferedOutputStream(FileOutputStream(file))
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
        os.close()

        return file
    }
}
