package com.example.stegomessenger.features.result

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.viewModels
import com.bumptech.glide.RequestManager
import com.example.stegomessenger.R
import com.example.stegomessenger.arch.infra.AbsFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class ResultFragment : AbsFragment(R.layout.fragment_result) {

    private val viewModel: ResultViewModel by viewModels()
    @Inject
    lateinit var requestManager: RequestManager
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageView = view.findViewById<ImageView>(R.id.iv)

        viewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState: ResultViewState? ->
            viewState ?: return@observe

            requestManager.load(viewState.bitmap).into(imageView)
        }
    }

    override fun getBackStackTag(): String = "ResultFragment"
}
