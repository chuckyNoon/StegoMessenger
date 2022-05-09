package com.example.diplomclient.overview

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aita.adapter.composable.ComposableListAdapter
import com.example.diplomclient.R
import com.example.diplomclient.arch.infra.AbsFragment
import com.example.diplomclient.overview.model.ChatAdapterDelegate
import com.example.diplomclient.overview.model.DividerAdapterDelegate
import com.example.diplomclient.overview.model.MessageAdapterDelegate

class OverviewFragment : AbsFragment(R.layout.fragment_overview) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModelProvider = ViewModelProvider(this, appViewModelFactory)
        val viewModel = viewModelProvider.get(OverviewViewModel::class.java)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler).apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }

        view.findViewById<ImageButton>(R.id.plus_btn).apply {
            setOnClickListener {
                viewModel.dispatch(OverviewAction.ClickPlus)
            }
        }

        val delegates = listOf(
            MessageAdapterDelegate(layoutInflater),
            ChatAdapterDelegate(
                layoutInflater,
                onChatClick = {
                    viewModel.dispatch(OverviewAction.ClickChat(it))
                }
            ),
            DividerAdapterDelegate(layoutInflater)
        )

        val adapter = ComposableListAdapter(delegates).apply {
            recyclerView.adapter = this
        }

        viewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState: OverviewViewState? ->
            viewState ?: return@observe

            adapter.submitList(viewState.cells)
        }
    }

    override fun getBackStackTag(): String = "OverviewFragment"
}