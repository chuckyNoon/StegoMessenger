package com.example.stegomessenger.v2.core.design.views

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stegomessenger.v2.compose.StegoTheme

@Composable
internal fun StegoButton(
    modifier: Modifier = Modifier,
    backgroundColor: Color = StegoTheme.colors.tintColor,
    textColor: Color = Color.White,
    shape: Shape = MaterialTheme.shapes.small,
    onClick: () -> Unit,
    text: String? = null,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit = {}
) {
    Button(
        modifier = modifier.height(48.dp),
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
            disabledBackgroundColor = backgroundColor.copy(
                alpha = 0.3f
            )
        ),
        shape = shape
    ) {
        text?.let {
            Text(
                text = it,
                style = StegoTheme.typography.body,
                color = textColor
            )
        } ?: content.invoke(this)
    }
}


enum class OutlineButtonStyle {
    MAIN,
    SECONDARY
}

@Composable
fun OutlinedStegoButton(
    modifier: Modifier = Modifier,
    style: OutlineButtonStyle,
    onClick: () -> Unit,
    text: String? = null,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit = {}
) {
    val backgroundColor =
        when (style) {
            OutlineButtonStyle.MAIN -> StegoTheme.colors.accentColor
            OutlineButtonStyle.SECONDARY -> Color(0xFFD6D7D7)
        }


    val textColor =
        when (style) {
            OutlineButtonStyle.MAIN -> Color.White
            OutlineButtonStyle.SECONDARY -> Color.Black
        }

    val shape = when(style){
        OutlineButtonStyle.MAIN -> RoundedCornerShape(12.dp)
        OutlineButtonStyle.SECONDARY -> MaterialTheme.shapes.small
    }

    StegoButton(
        modifier = modifier,
        backgroundColor = backgroundColor,
        textColor = textColor,
        onClick = onClick,
        text = text,
        enabled = enabled,
        content = content,
        shape = shape,
    )

}

@Composable
private fun OutlinedStegoButton_themed(
    style: OutlineButtonStyle
) {
    StegoTheme {
        Surface(
            color = StegoTheme.colors.primaryBackground
        ) {
            OutlinedStegoButton(style = style, text = "Some text", onClick = {})
        }
    }
}

@Preview
@Composable
private fun OutlineStegoButton_mainLight_preview() {
    OutlinedStegoButton_themed(style = OutlineButtonStyle.MAIN)
}

@Preview
@Composable
private fun OutlineStegoButton_mainDark_preview() {
    OutlinedStegoButton_themed(style = OutlineButtonStyle.SECONDARY)
}


