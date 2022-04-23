package com.example.diplomclient.test

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.diplomclient.R
import com.example.diplomclient.arch.infra.AbsFragment

class TestFragment : AbsFragment(R.layout.fragment_test) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModelProvider = ViewModelProvider(this, appViewModelFactory)
        val viewModel = viewModelProvider.get(TestViewModel::class.java)

        val textView = view.findViewById<TextView>(R.id.tv)

        viewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState: TestViewState? ->
            viewState ?: return@observe

            textView.text = viewState.text
        }
    }

    override fun getBackStackTag(): String = "TestFragment"
}
