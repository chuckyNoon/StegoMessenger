package com.example.stegomessenger.arch.redux

interface Action {

    val logMsg: String
        get() = this.javaClass.name
            .substringAfterLast(".")
            .replace("$", ".")
}
