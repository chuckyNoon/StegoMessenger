package com.example.stegomessenger.common

import android.graphics.Rect
import android.view.View
import androidx.annotation.Px
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.*


fun View.setViewPaddingLeft(@Px paddingLeftPx: Int): Boolean{
    if (paddingLeft == paddingLeftPx) {
        return false
    }
    setPadding(paddingLeftPx, paddingTop, paddingRight, paddingBottom)
    return true
}

fun View.setViewPaddingTop( @Px paddingTopPx: Int): Boolean {
    if (paddingTop == paddingTopPx) {
        return false
    }
    setPadding(paddingLeft, paddingTopPx, paddingRight, paddingBottom)
    return true
}

fun View.setViewPaddingRight(@Px paddingRightPx: Int): Boolean {
    if (paddingRight == paddingRightPx) {
        return false
    }
    setPadding(paddingLeft, paddingTop, paddingRightPx, paddingBottom)
    return true
}

fun View.setViewPaddingBottom(@Px paddingBottomPx: Int): Boolean {
    if (paddingBottom == paddingBottomPx) {
        return false
    }
    setPadding(paddingLeft, paddingTop, paddingRight, paddingBottomPx)
    return true
}

fun View.handleInsetsWithPaddingForSides(vararg insetSides: InsetSide) {
    val insetSideSet: Set<InsetSide> = buildInsetSideSet(*insetSides)
    updatePaddingOnApplyWindowInsets(
        this,
        object : UpdatePaddingOnApplyWindowInsetsListener {
            override fun updatePaddingOnApplyWindowInsets(
                view: View,
                insets: WindowInsetsCompat,
                initialPadding: Rect
            ): WindowInsetsCompat {
                if (insetSideSet.contains(InsetSide.START)) {
                    view.setViewPaddingLeft(insets.leftSystemWindowInset + initialPadding.left)
                }
                if (insetSideSet.contains(InsetSide.TOP)) {
                    view.setViewPaddingTop(insets.topSystemWindowInset + initialPadding.top)
                }
                if (insetSideSet.contains(InsetSide.END)) {
                    view.setViewPaddingRight(insets.rightSystemWindowInset + initialPadding.right)
                }
                if (insetSideSet.contains(InsetSide.BOTTOM)) {
                    view.setViewPaddingBottom(insets.bottomSystemWindowInset + initialPadding.bottom)
                }
                return insets
            }
        }
    )
}

private fun updatePaddingOnApplyWindowInsets(
    view: View,
    listener: UpdatePaddingOnApplyWindowInsetsListener
) {
    val initialPadding = Rect(
        view.paddingLeft,
        view.paddingTop,
        view.paddingRight,
        view.paddingBottom
    )
    ViewCompat.setOnApplyWindowInsetsListener(
        view
    ) { v: View, insets: WindowInsetsCompat ->
        listener.updatePaddingOnApplyWindowInsets(
            v,
            insets,
            initialPadding
        )
    }
    requestApplyInsetsWhenAttached(view)
}

private fun requestApplyInsetsWhenAttached(view: View) {
    if (view.isAttachedToWindow) {
        view.requestApplyInsets()
        return
    }
    view.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(v: View) {
            v.removeOnAttachStateChangeListener(this)
            v.requestApplyInsets()
        }

        override fun onViewDetachedFromWindow(v: View) {}
    })
}

private fun buildInsetSideSet(vararg insetSides: InsetSide): Set<InsetSide> {
    if (insetSides.isEmpty()) {
        return EnumSet.noneOf(InsetSide::class.java)
    }
    if (insetSides.size == 1) {
        return EnumSet.of(insetSides[0])
    }
    val insetSidesTail: Array<InsetSide?> = arrayOfNulls(insetSides.size - 1)
    System.arraycopy(insetSides, 1, insetSidesTail, 0, insetSides.size - 1)
    val nonNullInsetSides = insetSidesTail.filterNotNull().toSet()
    return EnumSet.of(insetSides[0]) + nonNullInsetSides
}

interface UpdatePaddingOnApplyWindowInsetsListener {
    fun updatePaddingOnApplyWindowInsets(
        view: View,
        insets: WindowInsetsCompat,
        initialPadding: Rect
    ): WindowInsetsCompat
}

