package com.example.diplomclient.main

import android.app.Application
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.diplomclient.R
import com.example.diplomclient.arch.infra.AbsFragment
import com.example.diplomclient.test.TestFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sessionAppViewModelFactory = AppSessionViewModelFactory(application)
        val appViewModelProvider = ViewModelProvider(this, sessionAppViewModelFactory)
        val appViewModel = appViewModelProvider.get(AppViewModel::class.java)

        val appViewModelFactory = appViewModel.appViewModelFactory
        val thisFactoryViewModelProvider = ViewModelProvider(this, appViewModelFactory)
        val mainViewModel = thisFactoryViewModelProvider.get(MainViewModel::class.java)

        mainViewModel.navigationLiveData.observe(this) { navigation: MainNavigation? ->
            when (navigation) {
                is MainNavigation.Test -> {
                    showFragment(TestFragment())
                }
            }
        }
    }

    private fun showFragment(fragment: AbsFragment) =
        showFragmentInternal(
            fragment = fragment,
            fragmentManager = supportFragmentManager,
            containerViewId = R.id.content,
        )

    private fun showFragmentInternal(
        fragment: AbsFragment,
        fragmentManager: FragmentManager,
        @IdRes containerViewId: Int = R.id.content,
    ) {
        if (!fragmentManager.isSafeToCommit) {
            return
        }

        val backStackTag = fragment.getBackStackTag()
        fragmentManager.commit(allowStateLoss = false) {
            setReorderingAllowed(false)
            replace(containerViewId, fragment, backStackTag)
            addToBackStack(backStackTag)
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
