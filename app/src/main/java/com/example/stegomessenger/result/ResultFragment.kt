package com.example.stegomessenger.result

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import com.example.stegomessenger.R
import com.example.stegomessenger.arch.infra.AbsFragment
import com.example.stegomessenger.chat.getPicassoInstance

// TODO: implement
class ResultFragment : AbsFragment(R.layout.fragment_result) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModelProvider = ViewModelProvider(this, appViewModelFactory)
        val viewModel = viewModelProvider.get(ResultViewModel::class.java)
        val requestManager = getPicassoInstance(this)

        val imageView = view.findViewById<ImageView>(R.id.iv)

        viewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState: ResultViewState? ->
            viewState ?: return@observe

            requestManager.load(viewState.bitmap).into(imageView)
        }
    }

    override fun getBackStackTag(): String = "ResultFragment"
}
