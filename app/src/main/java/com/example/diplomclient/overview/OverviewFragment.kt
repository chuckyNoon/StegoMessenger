package com.example.diplomclient.overview

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.diplomclient.arch.adapter.composable.ComposableListAdapter
import com.example.diplomclient.R
import com.example.diplomclient.arch.infra.AbsFragment
import com.example.diplomclient.chat.getPicassoInstance
import com.example.diplomclient.common.*
import com.example.diplomclient.main.navigation.CoreAction
import com.example.diplomclient.overview.model.items.ChatAdapterDelegate
import com.example.diplomclient.overview.model.DividerAdapterDelegate
import com.example.diplomclient.chat.items.TextMessageAdapterDelegate
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

class OverviewFragment : AbsFragment(R.layout.fragment_overview) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = requireActivity()
        val viewModelProvider = ViewModelProvider(this, appViewModelFactory)
        val viewModel = viewModelProvider.get(OverviewViewModel::class.java)

        val fragmentManager = activity.supportFragmentManager
        val navView = view.findViewById<NavigationView>(R.id.nav_view).apply {
            setNavigationItemSelectedListener { item ->
                AppLogger.log("nav click")
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
            ViewUtils.handleInsetsWithPaddingForSides(
                this,
                InsetSide.START,
                InsetSide.END,
                InsetSide.BOTTOM
            )
        }

        view.findViewById<Toolbar>(R.id.toolbar).apply {
            ViewUtils.handleInsetsWithPaddingForSides(
                this,
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

        val snackBar = Snackbar.make(view, "Loading messages...", Snackbar.LENGTH_INDEFINITE)

        val delegates = listOf(
            TextMessageAdapterDelegate(
                layoutInflater,
                requestManager = getPicassoInstance(this),
                onImageClick = {
                }
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
            if (viewState.isLoading) {
                snackBar.show()
            } else {
                snackBar.dismiss()
            }
        }
    }

    override fun getBackStackTag(): String = "OverviewFragment"
}
