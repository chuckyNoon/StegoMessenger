package com.example.diplomclient.registration

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import com.example.diplomclient.R
import com.example.diplomclient.arch.infra.AbsFragment
import com.example.diplomclient.common.InsetSide
import com.example.diplomclient.common.handleInsetsWithPaddingForSides

class RegistrationFragment : AbsFragment(R.layout.fragment_registration) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModelProvider = ViewModelProvider(this, appViewModelFactory)
        val viewModel = viewModelProvider.get(RegistrationViewModel::class.java)

        val progressBar = view.findViewById<ProgressBar>(R.id.pb)
        val nameEditText = view.findViewById<EditText>(R.id.name_et)
        val passwordEditText = view.findViewById<EditText>(R.id.password_et)
        val idEditText = view.findViewById<EditText>(R.id.id_et)
        val registrationButton = view.findViewById<View>(R.id.sign_up).apply {
            setOnClickListener {
                viewModel.dispatch(
                    RegistrationAction.OnRegisterClick(
                        id = idEditText.text.toString(),
                        password = passwordEditText.text.toString(),
                        name = nameEditText.text.toString()
                    )
                )
            }
        }
        view.findViewById<View>(R.id.btn_block).apply {
            handleInsetsWithPaddingForSides(
                InsetSide.BOTTOM,
                InsetSide.START,
                InsetSide.END
            )
        }
        view.findViewById<View>(R.id.toolbar).apply {
            handleInsetsWithPaddingForSides(
                InsetSide.TOP,
                InsetSide.START,
                InsetSide.END
            )
        }

        viewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState: RegistrationViewState? ->
            viewState ?: return@observe

            if (viewState.isLoading) {
                registrationButton.isEnabled = false
                nameEditText.isEnabled = false
                passwordEditText.isEnabled = false
                idEditText.isEnabled = false
                nameEditText.isEnabled = false

                progressBar.visibility = View.VISIBLE
            } else {
                registrationButton.isEnabled = true
                nameEditText.isEnabled = true
                passwordEditText.isEnabled = true
                registrationButton.isEnabled = true
                idEditText.isEnabled = true
                nameEditText.isEnabled = true

                progressBar.visibility = View.INVISIBLE
            }
        }
    }

    override fun getBackStackTag(): String = "RegistrationFragment"
}
