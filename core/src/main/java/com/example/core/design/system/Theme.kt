package com.example.core.design.system

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stegomessenger.v2.compose.orangeDarkPalette
import com.example.stegomessenger.v2.compose.orangeLightPalette
import com.example.stegomessenger.v2.compose.purpleDarkPalette
import com.example.stegomessenger.v2.compose.purpleLightPalette


@Composable
fun StegoTheme(
    style: StegoStyle = StegoStyle.Purple,
    textSize: StegoSize = StegoSize.Medium,
    paddingSize: StegoSize = StegoSize.Medium,
    corners: StegoCorners = StegoCorners.Rounded,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = when (darkTheme) {
        true -> {
            when (style) {
                StegoStyle.Purple -> purpleDarkPalette
                StegoStyle.Orange -> orangeDarkPalette
            }
        }
        false -> {
            when (style) {
                StegoStyle.Purple -> purpleLightPalette
                StegoStyle.Orange -> orangeLightPalette
            }
        }
    }

    val typography = StegoTypography(
        heading = TextStyle(
            fontSize = when (textSize) {
                StegoSize.Small -> 24.sp
                StegoSize.Medium -> 28.sp
                StegoSize.Big -> 32.sp
            },
            fontWeight = FontWeight.Bold
        ),
        body = TextStyle(
            fontSize = when (textSize) {
                StegoSize.Small -> 14.sp
                StegoSize.Medium -> 16.sp
                StegoSize.Big -> 18.sp
            },
            fontWeight = FontWeight.Normal
        ),
        toolbar = TextStyle(
            fontSize = when (textSize) {
                StegoSize.Small -> 14.sp
                StegoSize.Medium -> 16.sp
                StegoSize.Big -> 18.sp
            },
            fontWeight = FontWeight.Medium
        ),
        caption = TextStyle(
            fontSize = when (textSize) {
                StegoSize.Small -> 10.sp
                StegoSize.Medium -> 12.sp
                StegoSize.Big -> 14.sp
            }
        )
    )

    val shapes = StegoShape(
        padding = when (paddingSize) {
            StegoSize.Small -> 12.dp
            StegoSize.Medium -> 16.dp
            StegoSize.Big -> 20.dp
        },
        cornersStyle = when (corners) {
            StegoCorners.Flat -> RoundedCornerShape(0.dp)
            StegoCorners.Rounded -> RoundedCornerShape(8.dp)
        }
    )

    val images = StegoImage(
        mainIcon =  null, //if (darkTheme) R.drawable.ic_baseline_mood_24 else R.drawable.ic_baseline_mood_bad_24,
        mainIconDescription = if (darkTheme) "Good Mood" else "Bad Mood"
    )

    CompositionLocalProvider(
        LocalStegoColors provides colors,
        LocalStegoTypography provides typography,
        LocalStegoShape provides shapes,
        LocalStegoImage provides images,
        content = content
    )
}


data class StegoColors(
    val primaryText: Color,
    val primaryBackground: Color,
    val secondaryText: Color,
    val secondaryBackground: Color,
    val tintColor: Color,
    val controlColor: Color,
    val errorColor: Color,
    val accentColor: Color,
)

data class StegoTypography(
    val heading: TextStyle,
    val body: TextStyle,
    val toolbar: TextStyle,
    val caption: TextStyle
)

data class StegoShape(
    val padding: Dp,
    val cornersStyle: Shape
)

data class StegoImage(
    val mainIcon: Int?,
    val mainIconDescription: String
)

object StegoTheme {
    val colors: StegoColors
        @Composable
        get() = LocalStegoColors.current

    val typography: StegoTypography
        @Composable
        get() = LocalStegoTypography.current

    val shapes: StegoShape
        @Composable
        get() = LocalStegoShape.current

    val images: StegoImage
        @Composable
        get() = LocalStegoImage.current
}

enum class StegoStyle {
    Purple, Orange
}

enum class StegoSize {
    Small, Medium, Big
}

enum class StegoCorners {
    Flat, Rounded
}

internal val LocalStegoColors = staticCompositionLocalOf<StegoColors> {
    error("No colors provided")
}

internal val LocalStegoTypography = staticCompositionLocalOf<StegoTypography> {
    error("No font provided")
}

internal val LocalStegoShape = staticCompositionLocalOf<StegoShape> {
    error("No shapes provided")
}

internal val LocalStegoImage = staticCompositionLocalOf<StegoImage> {
    error("No images provided")
}