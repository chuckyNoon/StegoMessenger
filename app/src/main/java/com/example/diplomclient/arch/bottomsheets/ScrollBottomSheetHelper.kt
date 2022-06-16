package com.example.diplomclient.arch.bottomsheets

import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView

object ScrollViewUtil {

    val CAN_SCROLL_UP = -1
    val CAN_SCROLL_DOWN = 1
}

class ScrollBottomSheetHelper {

    private val updateElevationsRunnable: Runnable

    @JvmOverloads
    constructor(
        scrollView: NestedScrollView,
        buttonBlock: View? = null,
        titleBlock: View? = null,
    ) {
        updateElevationsRunnable = Runnable {
            titleBlock?.isSelected = scrollView.canScrollVertically(ScrollViewUtil.CAN_SCROLL_UP)
            buttonBlock?.isSelected = scrollView.canScrollVertically(ScrollViewUtil.CAN_SCROLL_DOWN)
        }
        scrollView.setOnScrollChangeListener { _, _, _, _, _ -> updateElevationsRunnable.run() }
        scrollView.post(updateElevationsRunnable)
    }

    @JvmOverloads
    constructor(
        recyclerView: RecyclerView,
        buttonBlock: View? = null,
        titleBlock: View? = null,
    ) {
        updateElevationsRunnable = Runnable {
            titleBlock?.isSelected = recyclerView.canScrollVertically(ScrollViewUtil.CAN_SCROLL_UP)
            buttonBlock?.isSelected =
                recyclerView.canScrollVertically(ScrollViewUtil.CAN_SCROLL_DOWN)
        }
        recyclerView.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    updateElevationsRunnable.run()
                }
            }
        )
        recyclerView.post(updateElevationsRunnable)
    }

}
