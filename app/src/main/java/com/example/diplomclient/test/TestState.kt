package com.example.diplomclient.test

data class TestState(
    val viewState: TestViewState
) {
    companion object {
        val EMPTY = TestState(viewState = TestViewState.EMPTY)
    }
}

data class TestViewState(
    val text: String?
) {
    companion object {
        val EMPTY = TestViewState(text = null)
    }
}
