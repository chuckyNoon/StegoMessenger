package com.example.stegomessenger.compose.bottomsheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stegomessenger.compose.nav.Dialogs

@Composable
fun BottomSheetContent(dialogs: Dialogs){
    when(dialogs){
        Dialogs.SEND_MESSAGE -> {
            BottomSheet(_text = "1")
        }
        Dialogs.SEND_MESSAGE_2 -> {
            BottomSheet(_text = "2")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun BottomSheet(_text: String = "1") {
    Column(
        modifier = Modifier.padding(32.dp)
    ) {
        Text(
            text = "Bottom sheet", style = MaterialTheme.typography.h6
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = _text + "Click outside the bottom sheet to hide it",
            style = MaterialTheme.typography.body1
        )
    }
}