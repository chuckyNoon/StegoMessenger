package com.example.diplomclient.arch.flux

interface Action {

    val logMsg: String
        get() {
            val fqcn = this.javaClass.name
            val rawName = fqcn.substringAfterLast(".")
            return rawName.replace("$", ".")
        }
}
