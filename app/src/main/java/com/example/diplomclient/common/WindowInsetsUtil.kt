package com.example.diplomclient.common

import androidx.core.view.WindowInsetsCompat

val WindowInsetsCompat.leftSystemWindowInset: Int
    get() = getInsets(WindowInsetsCompat.Type.systemBars()).left

val WindowInsetsCompat.topSystemWindowInset: Int
    get() = getInsets(WindowInsetsCompat.Type.systemBars()).top

val WindowInsetsCompat.rightSystemWindowInset: Int
    get() = getInsets(WindowInsetsCompat.Type.systemBars()).right

val WindowInsetsCompat.bottomSystemWindowInset: Int
    get() = getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime()).bottom
