package com.example.diplomclient.registration

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import com.example.diplomclient.R
import com.example.diplomclient.arch.infra.AbsFragment

class RegistrationFragment : AbsFragment(R.layout.fragment_registration) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModelProvider = ViewModelProvider(this, appViewModelFactory)
        val viewModel = viewModelProvider.get(RegistrationViewModel::class.java)

        val progressBar = view.findViewById<ProgressBar>(R.id.pb)
        val visibleNameEditText = view.findViewById<EditText>(R.id.visible_name_et)
        val loginEditText = view.findViewById<EditText>(R.id.login_et)
        val passwordEditText = view.findViewById<EditText>(R.id.password_et)
        val registrationButton = view.findViewById<Button>(R.id.register_btn).apply {
            setOnClickListener {
                viewModel.dispatch(
                    RegistrationAction.OnRegisterClick(
                        visibleName = visibleNameEditText.text.toString(),
                        login = loginEditText.text.toString(),
                        password = passwordEditText.text.toString()
                    )
                )
            }
        }

        viewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState: RegistrationViewState? ->
            viewState ?: return@observe

            if (viewState.isLoading) {
                registrationButton.isEnabled = false
                visibleNameEditText.isEnabled = false
                loginEditText.isEnabled = false
                passwordEditText.isEnabled = false

                progressBar.visibility = View.VISIBLE
            } else {
                registrationButton.isEnabled = true
                visibleNameEditText.isEnabled = true
                loginEditText.isEnabled = true
                passwordEditText.isEnabled = true
                registrationButton.isEnabled = true

                progressBar.visibility = View.INVISIBLE
            }
        }
    }

    override fun getBackStackTag(): String = "LoginFragment"
}
