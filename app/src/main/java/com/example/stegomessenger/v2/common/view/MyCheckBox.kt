package com.example.stegomessenger.v2.common.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.RequestManager
import com.example.stegomessenger.R

class MyCheckBox @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val iconImageView: ImageView
    private val textView: TextView

    var onClick: (() -> Unit)? = null

    init {
        inflate(context, R.layout.view_my_checkbox, this)

        iconImageView = findViewById(R.id.icon)
        textView = findViewById(R.id.tv)

        setOnClickListener {
            onClick?.invoke()
        }
    }

    fun setIsChecked(requestManager: RequestManager, isChecked: Boolean) =
        requestManager
            .load(if (isChecked) R.drawable.ic_24_checkbox_on else R.drawable.ic_24_checkbox_off)
            .into(iconImageView)


    fun setText(text: String) {
        textView.text = text
    }
}
