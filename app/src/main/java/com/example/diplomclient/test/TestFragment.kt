package com.example.diplomclient.test

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aita.adapter.composable.ComposableListAdapter
import com.example.diplomclient.R
import com.example.diplomclient.arch.infra.AbsFragment
import com.example.diplomclient.test.model.MessageAdapterDelegate

class TestFragment : AbsFragment(R.layout.fragment_test) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModelProvider = ViewModelProvider(this, appViewModelFactory)
        val viewModel = viewModelProvider.get(TestViewModel::class.java)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler).apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }

        view.findViewById<Button>(R.id.button).apply {
            setOnClickListener {
                viewModel.dispatch(TestAction.ClickLoad)
            }
        }

        val delegates = listOf(
            MessageAdapterDelegate(layoutInflater)
        )

        val adapter = ComposableListAdapter(delegates).apply {
            recyclerView.adapter = this
        }

        viewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState: TestViewState? ->
            viewState ?: return@observe

            adapter.submitList(viewState.cells)
        }
    }

    override fun getBackStackTag(): String = "TestFragment"
}
