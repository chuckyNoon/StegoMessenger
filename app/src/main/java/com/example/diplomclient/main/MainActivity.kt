package com.example.diplomclient.main

import android.app.Application
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.diplomclient.R
import com.example.diplomclient.arch.FragmentTransition
import com.example.diplomclient.arch.infra.AbsFragment
import com.example.diplomclient.chat.ChatFragment
import com.example.diplomclient.common.SystemUiUtils
import com.example.diplomclient.login.LoginFragment
import com.example.diplomclient.main.navigation.CoreNav
import com.example.diplomclient.main.navigation.CoreNavViewModel
import com.example.diplomclient.overview.OverviewFragment
import com.example.diplomclient.registration.RegistrationFragment
import com.example.diplomclient.result.ResultFragment
import com.example.diplomclient.search.SearchFragment
import com.example.diplomclient.stego_dialog.StegoDialog

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val window = window!!
        val sessionAppViewModelFactory = AppSessionViewModelFactory(application)
        val appViewModelProvider = ViewModelProvider(this, sessionAppViewModelFactory)
        val appViewModel = appViewModelProvider.get(AppViewModel::class.java)

        val appViewModelFactory = appViewModel.appViewModelFactory
        val thisFactoryViewModelProvider = ViewModelProvider(this, appViewModelFactory)
        val mainViewModel = thisFactoryViewModelProvider.get(CoreNavViewModel::class.java)

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

    private class AppSessionViewModelFactory(private val app: Application) :
        ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return AppViewModel(app) as T
        }
    }
}
