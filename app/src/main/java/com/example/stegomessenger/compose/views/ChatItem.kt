package com.example.stegomessenger.compose.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stegomessenger.R
import com.example.stegomessenger.compose.StegoTheme
import com.example.stegomessenger.overview.model.items.ChatCell

@Composable
fun ChatItem(cell: ChatCell, onClick: (ChatCell) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                onClick(cell)
            }
    ) {

        Image(
            painter = painterResource(id = R.drawable.ic_24_image),
            contentDescription = "",
            modifier = Modifier
                .padding(start = 8.dp, end = 16.dp)
                .size(48.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = cell.title,
                color = Color.Black,
                style = StegoTheme.typography.body
            )
            Text(
                text = cell.message,
                color = StegoTheme.colors.secondaryText,
                style = StegoTheme.typography.caption
            )
        }

        Text(
            text = cell.date,
            color = Color.Black,
            style = StegoTheme.typography.caption,
            modifier = Modifier
                .padding(start = 8.dp, end = 16.dp)
                .align(Alignment.CenterVertically)
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun ChatItem_preview() {
    StegoTheme {
        ChatItem(
            cell = ChatCell(
                "1",
                "Title",
                "Message",
                "24.05.2020", "DA"
            ),
            onClick = {

            }
        )
    }
}