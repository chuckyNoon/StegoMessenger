package com.example.stegomessenger.arch

import android.content.Context
import androidx.fragment.app.FragmentTransaction
import com.example.stegomessenger.R

enum class FragmentTransition {
    NONE {
        override fun apply(context: Context, transaction: FragmentTransaction) {
        }
    },
    EMERGE {
        override fun apply(context: Context, transaction: FragmentTransaction) {
            transaction.setCustomAnimations(
                R.anim.slide_in_from_bottom,
                R.anim.slide_out_to_top,
                R.anim.slide_in_from_top,
                R.anim.slide_out_to_bottom
            )
        }
    },
    SLIDE {
        override fun apply(context: Context, transaction: FragmentTransaction) {
            transaction.setCustomAnimations(
                R.anim.slide_in_from_right,
                R.anim.slide_out_to_left,
                R.anim.slide_in_from_left,
                R.anim.slide_out_to_right
            )
        }
    };

    abstract fun apply(context: Context, transaction: FragmentTransaction)
}
