package com.example.stegomessenger.app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.example.stegomessenger.R
import com.example.stegomessenger.arch.FragmentTransition
import com.example.stegomessenger.arch.infra.AbsFragment
import com.example.stegomessenger.common.SystemUiUtils
import com.example.stegomessenger.features.chat.ChatFragment
import com.example.stegomessenger.features.content_dialog.ContentDialog
import com.example.stegomessenger.features.login.LoginFragment
import com.example.stegomessenger.app.core.CoreNav
import com.example.stegomessenger.app.core.CoreNavViewModel
import com.example.stegomessenger.features.overview.OverviewFragment
import com.example.stegomessenger.features.registration.RegistrationFragment
import com.example.stegomessenger.features.result.ResultFragment
import com.example.stegomessenger.features.search.SearchFragment
import com.example.stegomessenger.features.stego_dialog.StegoDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val mainViewModel: CoreNavViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val window = window!!

        SystemUiUtils.makeStatusAndNavBarsTransparent(this, window)

        mainViewModel.navigationLiveData.observe(this) { navigation: CoreNav? ->
            when (navigation) {
                is CoreNav.Overview ->
                    showFragment(
                        fragment = OverviewFragment(),
                        fragmentTransition = FragmentTransition.NONE
                    )
                is CoreNav.Login ->
                    showFragment(
                        fragment = LoginFragment(),
                        fragmentTransition = FragmentTransition.EMERGE,
                    )
                is CoreNav.Registration ->
                    showFragment(
                        fragment = RegistrationFragment(),
                        fragmentTransition = FragmentTransition.SLIDE
                    )
                is CoreNav.Chat ->
                    showFragment(
                        fragment = ChatFragment(),
                        fragmentTransition = FragmentTransition.SLIDE
                    )
                is CoreNav.Search ->
                    showFragment(
                        fragment = SearchFragment(),
                        fragmentTransition = FragmentTransition.EMERGE
                    )
                is CoreNav.Result ->
                    showFragment(
                        fragment = ResultFragment(),
                        fragmentTransition = FragmentTransition.EMERGE
                    )
                is CoreNav.StegoDialog ->
                    StegoDialog().showAfterKeyboardHides(supportFragmentManager, "stego", this)
                is CoreNav.ContentDialog ->
                    ContentDialog().showAfterKeyboardHides(supportFragmentManager, "content", this)
                else -> {

                }
            }
        }

        mainViewModel.errorLiveData.observe(this) { error: String? ->
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showFragment(
        fragment: AbsFragment,
        fragmentTransition: FragmentTransition,
        mustAddToBackStack: Boolean = true
    ) =
        showFragmentInternal(
            fragment = fragment,
            fragmentManager = supportFragmentManager,
            fragmentTransition = fragmentTransition,
            containerViewId = R.id.content,
            mustAddToBackStack = mustAddToBackStack
        )

    private fun showFragmentInternal(
        fragment: AbsFragment,
        fragmentManager: FragmentManager,
        fragmentTransition: FragmentTransition,
        @IdRes containerViewId: Int = R.id.content,
        mustAddToBackStack: Boolean
    ) {
        if (!fragmentManager.isSafeToCommit) {
            return
        }

        val backStackTag = fragment.getBackStackTag()
        fragmentManager.commit(allowStateLoss = false) {
            setReorderingAllowed(false)
            fragmentTransition.apply(MainApplication.getInstance(), this)
            replace(containerViewId, fragment, backStackTag)
            if (mustAddToBackStack) {
                addToBackStack(backStackTag)
            }
        }
    }

    private val FragmentManager.isSafeToCommit: Boolean
        get() = !isStateSaved && !isDestroyed

}
