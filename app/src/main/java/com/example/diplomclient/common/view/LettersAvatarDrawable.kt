package com.example.diplomclient.common.view

import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt

class LettersAvatarDrawable @JvmOverloads constructor(
    letters: String?,
    @ColorInt textColor: Int = Color.WHITE,
    @ColorInt backgroundColor: Int = PLACEHOLDER_COLOR
) : Drawable() {

    private var letters: String? = null
    private val textPaint: Paint
    private val backgroundPaint: Paint
    private var height = 0
    private var width = 0
    private var textX = 0f
    private var textY = 0f

    init {
        if (letters.isNullOrEmpty()) {
            this.letters = DEFAULT_PLACEHOLDER_STRING
        } else {
            this.letters = if (letters.length > 2) letters.substring(0, 2) else letters
        }
        textPaint = Paint()
        textPaint.isAntiAlias = true
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.color = textColor

        backgroundPaint = Paint()
        backgroundPaint.isAntiAlias = true
        backgroundPaint.style = Paint.Style.FILL
        backgroundPaint.color = backgroundColor
    }

    override fun draw(canvas: Canvas) {
        val bounds = bounds
        val width = bounds.width()
        val height = bounds.height()
        recalculateValuesOnBoundsChange(width, height)
        canvas.drawCircle(width / 2f, width / 2f, width / 2f, backgroundPaint)
        canvas.drawText(letters!!, textX, textY, textPaint)
    }

    override fun setAlpha(alpha: Int) {
        textPaint.alpha = alpha
        backgroundPaint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        textPaint.colorFilter = colorFilter
        backgroundPaint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    private fun recalculateValuesOnBoundsChange(width: Int, height: Int) {
        if (width != this.width || height != this.height) {
            this.width = width
            this.height = height
            val textSize = height * RELATIVE_TEXT_SIZE
            textPaint.textSize = textSize
            textX = width / 2f
            textY = (height - textPaint.ascent() - textPaint.descent()) / 2f
        }
    }

    companion object {
        private const val DEFAULT_PLACEHOLDER_STRING = "-"
        private const val RELATIVE_TEXT_SIZE = 0.4f
        private const val PLACEHOLDER_COLOR = -0x3400
    }
}
