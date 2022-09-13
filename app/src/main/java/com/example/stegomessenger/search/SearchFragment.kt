package com.example.stegomessenger.search

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stegomessenger.arch.adapter.ComposableListAdapter
import com.example.stegomessenger.R
import com.example.stegomessenger.arch.infra.AbsFragment
import com.example.stegomessenger.chat.getPicassoInstance
import com.example.stegomessenger.common.InsetSide
import com.example.stegomessenger.common.handleInsetsWithPaddingForSides
import com.example.stegomessenger.overview.model.items.DividerAdapterDelegate
import com.example.stegomessenger.search.item.SearchUserDelegate

class SearchFragment : AbsFragment(R.layout.fragment_search) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModelProvider = ViewModelProvider(this, appViewModelFactory)
        val viewModel = viewModelProvider.get(SearchViewModel::class.java)
        val activity = requireActivity()

        view.findViewById<View>(R.id.toolbar_block).apply {
            handleInsetsWithPaddingForSides(
                InsetSide.TOP,
                InsetSide.START,
                InsetSide.END
            )
        }
        val searchEditText = view.findViewById<EditText>(R.id.search_et).apply {
            addTextChangedListener {
                viewModel.dispatch(SearchAction.TextTyped(it.toString()))
            }
        }
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler).apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            handleInsetsWithPaddingForSides(
                InsetSide.BOTTOM,
                InsetSide.START,
                InsetSide.END
            )
        }

        val delegates = listOf(
            SearchUserDelegate(
                inflater = layoutInflater,
                requestManager = getPicassoInstance(this),
                onClick = { cell ->
                    viewModel.dispatch(SearchAction.ClickStartChat(cell))
                }
            ),
            DividerAdapterDelegate(layoutInflater)
        )

        val adapter = ComposableListAdapter(delegates).apply {
            recyclerView.adapter = this
        }

        viewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState: SearchViewState? ->
            viewState ?: return@observe

            adapter.submitList(viewState.cells)
            if (searchEditText.text.toString().isEmpty()) {
                searchEditText.setText(viewState.searchText)
            }
        }

        viewModel.backLiveData.observe(viewLifecycleOwner) { unit: Unit? ->
            unit ?: return@observe

            activity.onBackPressed()
        }
    }

    override fun getBackStackTag(): String = "SearchFragment"
}
