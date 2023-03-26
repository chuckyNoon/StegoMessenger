package com.example.stegomessenger.v2.common

fun Int.setBit(position: Int, value: Boolean) =
    if (value) {
        this or (1 shl (position))
    } else {
        this and (1 shl (position)).inv()
    }

fun Int.getBit(position: Int) =
    ((this shr position) and 1) == 1

fun Byte.setBit(position: Int, value: Boolean) =
    this.toInt().setBit(position, value).toByte()

fun Byte.getBit(position: Int) =
    this.toInt().getBit(position)


