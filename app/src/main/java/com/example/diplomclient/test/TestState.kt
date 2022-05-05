package com.example.diplomclient.test

import com.aita.adapter.composable.DelegateDiffable

data class TestState(
    val values: List<String>,
    val viewState: TestViewState
) {
    companion object {
        val EMPTY = TestState(
            values = emptyList(),
            viewState = TestViewState.EMPTY
        )
    }
}

data class TestViewState(
    val cells: List<DelegateDiffable<*>>
) {
    companion object {
        val EMPTY = TestViewState(cells = emptyList())
    }
}
