package com.example.stegomessenger.overview

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stegomessenger.arch.adapter.ComposableListAdapter
import com.example.stegomessenger.R
import com.example.stegomessenger.arch.infra.AbsFragment
import com.example.stegomessenger.chat.getPicassoInstance
import com.example.stegomessenger.common.*
import com.example.stegomessenger.main.navigation.CoreAction
import com.example.stegomessenger.overview.model.items.ChatAdapterDelegate
import com.example.stegomessenger.overview.model.items.DividerAdapterDelegate
import com.example.stegomessenger.chat.items.TextMessageAdapterDelegate
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

class OverviewFragment : AbsFragment(R.layout.fragment_overview) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = requireActivity()
        val viewModelProvider = ViewModelProvider(this, appViewModelFactory)
        val viewModel = viewModelProvider.get(OverviewViewModel::class.java)

        val fragmentManager = activity.supportFragmentManager
        view.findViewById<NavigationView>(R.id.nav_view).apply {
            setNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.log_out -> {
                        PrefsHelper.getEditor().remove(PrefsContract.TOKEN).commit()
                        for (i in 0 until fragmentManager.backStackEntryCount) {
                            fragmentManager.popBackStack()
                        }
                        viewModel.dispatch(CoreAction.ShowLogin)
                        true
                    }
                    else ->
                        false
                }
            }
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler).apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            handleInsetsWithPaddingForSides(
                InsetSide.START,
                InsetSide.END,
                InsetSide.BOTTOM
            )
        }

        view.findViewById<Toolbar>(R.id.toolbar).apply {
            handleInsetsWithPaddingForSides(
                InsetSide.TOP,
                InsetSide.START,
                InsetSide.END
            )
        }

        view.findViewById<ImageButton>(R.id.plus_btn).apply {
            setOnClickListener {
                viewModel.dispatch(CoreAction.ShowSearch)
            }
        }

        // TODO: make it controlled by liveData
        Snackbar.make(view, R.string.loading_messages, Snackbar.LENGTH_INDEFINITE).show()

        val delegates = listOf(
            TextMessageAdapterDelegate(
                layoutInflater
            ),
            ChatAdapterDelegate(
                layoutInflater,
                requestManager = getPicassoInstance(this),
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
