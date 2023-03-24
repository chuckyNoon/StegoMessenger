package com.example.stegomessenger.compose.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stegomessenger.R
import com.example.stegomessenger.common.ColoredText
import com.example.stegomessenger.compose.StegoTheme
import com.example.stegomessenger.search.item.SearchUserCell

@Composable
fun UserItem(cell: SearchUserCell) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_24_image),
            contentDescription = "",
            modifier = Modifier
                .padding(start = 8.dp)
                .size(size = 48.dp),
        )
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
        ) {
            Text(
                text = cell.nameText.value,
                color = Color.Black,
                style = StegoTheme.typography.body
            )

            Text(
                text = cell.idText.value,
                color = Color.Gray,
                style = StegoTheme.typography.body
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UserItem_preview() {
    StegoTheme {
        UserItem(
            cell = SearchUserCell(
                id = "1",
                nameText = ColoredText(value = "Name Surname", colorRes = R.color.black),
                idText = ColoredText(value = "@test12", colorRes = R.color.system_blue)
            )
        )
    }
}