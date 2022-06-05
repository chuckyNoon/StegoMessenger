package com.example.diplomclient.common

fun Int.setBit(position: Int, value: Boolean) =
    if (value) {
        this or (1 shl (position))
    } else {
        this and (1 shl (position)).inv()
    }

fun Int.setLowestBit(value: Boolean) =
    this.setBit(position = 0, value = value)

fun Int.getBit(position: Int) =
    ((this shr position) and 1) == 1

fun Byte.setBit(position: Int, value: Boolean) =
    this.toInt().setBit(position, value).toByte()

fun Byte.getBit(position: Int) =
    this.toInt().getBit(position)

fun Int.setBits(startPosition: Int, endPosition: Int, value: Boolean): Int {
    var result = this
    for (i in startPosition..endPosition) {
        result = result.setBit(i, value)
    }
    return result
}
