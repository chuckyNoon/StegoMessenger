package com.example.diplomclient.common

import android.graphics.Rect
import android.view.View
import androidx.annotation.Px
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.*

object ViewUtils {

    fun setViewPaddingLeft(view: View, @Px paddingLeftPx: Int): Boolean {
        if (view.paddingLeft == paddingLeftPx) {
            return false
        }
        view.setPadding(paddingLeftPx, view.paddingTop, view.paddingRight, view.paddingBottom)
        return true
    }

    fun setViewPaddingTop(view: View, @Px paddingTopPx: Int): Boolean {
        if (view.paddingTop == paddingTopPx) {
            return false
        }
        view.setPadding(view.paddingLeft, paddingTopPx, view.paddingRight, view.paddingBottom)
        return true
    }

    fun setViewPaddingRight(view: View, @Px paddingRightPx: Int): Boolean {
        if (view.paddingRight == paddingRightPx) {
            return false
        }
        view.setPadding(view.paddingLeft, view.paddingTop, paddingRightPx, view.paddingBottom)
        return true
    }

    fun setViewPaddingBottom(view: View, @Px paddingBottomPx: Int): Boolean {
        if (view.paddingBottom == paddingBottomPx) {
            return false
        }
        view.setPadding(view.paddingLeft, view.paddingTop, view.paddingRight, paddingBottomPx)
        return true
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

    fun handleInsetsWithPaddingForSides(
        view: View,
        vararg insetSides: InsetSide
    ) {
        val isRtl: Boolean = false //
        val insetSideSet: Set<InsetSide> = buildInsetSideSet(*insetSides)
        updatePaddingOnApplyWindowInsets(
            view,
            object : UpdatePaddingOnApplyWindowInsetsListener {
                override fun updatePaddingOnApplyWindowInsets(
                    view: View,
                    insets: WindowInsetsCompat,
                    initialPadding: Rect
                ): WindowInsetsCompat {
                    if (insetSideSet.contains(InsetSide.START)) {
                        if (isRtl) {
                            setViewPaddingRight(
                                view,
                                insets.rightSystemWindowInset + initialPadding.right
                            )
                        } else {
                            setViewPaddingLeft(
                                view,
                                insets.leftSystemWindowInset + initialPadding.left
                            )
                        }
                    }
                    if (insetSideSet.contains(InsetSide.TOP)) {
                        setViewPaddingTop(
                            view,
                            insets.topSystemWindowInset + initialPadding.top
                        )
                    }
                    if (insetSideSet.contains(InsetSide.END)) {
                        if (isRtl) {
                            setViewPaddingLeft(
                                view,
                                insets.leftSystemWindowInset + initialPadding.left
                            )
                        } else {
                            setViewPaddingRight(
                                view,
                                insets.rightSystemWindowInset + initialPadding.right
                            )
                        }
                    }
                    if (insetSideSet.contains(InsetSide.BOTTOM)) {
                        setViewPaddingBottom(
                            view,
                            insets.bottomSystemWindowInset + initialPadding.bottom
                        )
                    }
                    return insets
                }
            }
        )
    }

    interface UpdatePaddingOnApplyWindowInsetsListener {
        fun updatePaddingOnApplyWindowInsets(
            view: View,
            insets: WindowInsetsCompat,
            initialPadding: Rect
        ): WindowInsetsCompat
    }
}
