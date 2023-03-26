package com.example.stegomessenger.v2.compose.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stegomessenger.v2.compose.StegoTheme
import com.example.stegomessenger.v2.compose.model.TextMessageCell


@Composable
fun TextMessage(messageCell: TextMessageCell) {
    StegoTheme {
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .align(
                        if (messageCell.isMine) {
                            Alignment.CenterEnd
                        } else {
                            Alignment.CenterStart
                        }
                    )
                    .padding(vertical = 4.dp, horizontal = 8.dp)
                    .then(
                        if (messageCell.isMine) {
                            Modifier.padding(start = 64.dp)
                        } else {
                            Modifier.padding(end = 64.dp)
                        }
                    )
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        color = Color(
                            if (messageCell.isMine) {
                                0x19007aff
                            } else {
                                0xffe1e3e6
                            }
                        )
                    )
                    .padding(all = 8.dp)
            ) {

                Text(
                    text = messageCell.contentText,
                    style = StegoTheme.typography.body,
                    modifier = Modifier
                        .padding(end = 8.dp),
                    color = Color.Black
                )

                Text(
                    text = messageCell.dateText,
                    style = StegoTheme.typography.caption,
                    modifier = Modifier.padding(top = 12.dp),
                    color = StegoTheme.colors.secondaryText
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TextMessage_Mine_Preview() {
    StegoTheme {
        TextMessage(
            messageCell =
            TextMessageCell(
                id = "1",
                dateText = "20.02.20",
                contentText = "Hello",
                isMine = true
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TextMessage_Not_Mine_Preview() {
    StegoTheme {
        TextMessage(
            messageCell =
            TextMessageCell(
                id = "1",
                dateText = "20.02.20",
                contentText = "Hello",
                isMine = false
            )
        )
    }
}