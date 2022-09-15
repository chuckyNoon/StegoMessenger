package com.example.stegomessenger.stego_dialog

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import com.example.stegomessenger.R
import com.example.stegomessenger.arch.redux.dispatcher.Dispatchable
import com.example.stegomessenger.arch.redux.store.Middleware
import com.example.stegomessenger.arch.redux.Action
import com.example.stegomessenger.common.network.model.ErrorResponse
import com.example.stegomessenger.arch.util.StringsProvider
import com.example.stegomessenger.common.getName
import com.example.stegomessenger.common.launchBackgroundWork
import com.example.stegomessenger.common.network.ApiService
import com.example.stegomessenger.common.safeApiCall
import com.example.stegomessenger.steganography.LsbAlgorithm
import com.example.stegomessenger.main.navigation.CoreAction
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.BufferedOutputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class StegoMiddleware(
    private val context: Context,
    private val apiService: ApiService,
    private val stringsProvider: StringsProvider,
) : Middleware<StegoState> {

    override fun onReduced(
        dispatchable: Dispatchable,
        action: Action,
        oldState: StegoState,
        newState: StegoState
    ) {
        when (action) {
            is StegoAction.ClickSend -> launchBackgroundWork {
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

        val onSendingFail = { error: ErrorResponse ->
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
        failCallback: (ErrorResponse) -> Unit
    ) {
        if (containerBitmap == null) {
            safeApiCall(
                apiCall = { apiService.sendText(receiverId = receiverId, text = text) },
                onSuccess = successCallback,
                onError = failCallback
            )
            return
        }

        val file = saveBitmapInFile(
            bitmap = LsbAlgorithm().lsbEncode(
                dataByteArray = text.toByteArray(Charsets.UTF_8),
                containerBitmap = containerBitmap,
                startLabel = LsbAlgorithm.LABEL_START_TEXT,
                endLabel = LsbAlgorithm.LABEL_END_TEXT
            ) ?: error("failed to generate stego-image"),
            fileName = containerUri?.getName(context) ?: "tmp.img"
        )

        safeApiCall(
            apiCall = { startSendImageRequest(receiverId, file) },
            onSuccess = successCallback,
            onError = failCallback
        )
    }

    private suspend fun startSendImageRequest(
        receiverId: String,
        containerUri: Uri?,
        containerBitmap: Bitmap?,
        contentBitmap: Bitmap,
        successCallback: ((Unit) -> Unit),
        failCallback: ((ErrorResponse) -> Unit)
    ) {

        val bitmapToSend = if (containerBitmap != null) {
            val stream = ByteArrayOutputStream()
            contentBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val dataByteArray: ByteArray = stream.toByteArray()
            contentBitmap.recycle()

            LsbAlgorithm().lsbEncode(
                dataByteArray = dataByteArray,
                containerBitmap = containerBitmap,
                startLabel = LsbAlgorithm.LABEL_START_IMG,
                endLabel = LsbAlgorithm.LABEL_END_IMG
            ) ?: error("failed to generate stego-image")
        } else {
            contentBitmap
        }

        val file =
            saveBitmapInFile(bitmapToSend, fileName = containerUri?.getName(context) ?: "tmp.img")

        safeApiCall(
            apiCall = { startSendImageRequest(receiverId = receiverId, imageFile = file) },
            onSuccess = successCallback,
            onError = failCallback
        )
    }

    private suspend fun startSendImageRequest(receiverId: String, imageFile: File) =
        apiService.sendImage(
            file = MultipartBody.Part.createFormData(
                "image",
                imageFile.name,
                RequestBody.create(MediaType.parse("image/*"), imageFile)
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
