package com.example.stegomessenger.features.overview

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.stegomessenger.arch.adapter.ComposableListAdapter
import com.example.stegomessenger.R
import com.example.stegomessenger.arch.infra.AbsFragment
import com.example.stegomessenger.arch.util.Prefs
import com.example.stegomessenger.common.*
import com.example.stegomessenger.features.chat.items.TextMessageAdapterDelegate
import com.example.stegomessenger.app.core.CoreAction
import com.example.stegomessenger.features.overview.items.ChatAdapterDelegate
import com.example.stegomessenger.features.overview.items.DividerAdapterDelegate
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OverviewFragment : AbsFragment(R.layout.fragment_overview) {
    private val viewModel: OverviewViewModel by viewModels()

    @Inject
    lateinit var requestManager: RequestManager

    @Inject
    lateinit var prefs: Prefs

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = requireActivity()
        val context = requireContext()

        val fragmentManager = activity.supportFragmentManager

        view.findViewById<NavigationView>(R.id.nav_view).apply {
            setNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.log_out -> {
                        prefs.saveString(PrefsContract.TOKEN, null)
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

        val delegates = listOf(
            TextMessageAdapterDelegate(
                layoutInflater
            ),
            ChatAdapterDelegate(
                layoutInflater,
                requestManager = requestManager,
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
