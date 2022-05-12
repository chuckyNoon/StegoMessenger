package com.example.diplomclient.login

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import com.example.diplomclient.R
import com.example.diplomclient.arch.infra.AbsFragment
import com.example.diplomclient.common.InsetSide
import com.example.diplomclient.common.ViewUtils
import com.example.diplomclient.main.navigation.CoreAction

class LoginFragment : AbsFragment(R.layout.fragment_login) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModelProvider = ViewModelProvider(this, appViewModelFactory)
        val viewModel = viewModelProvider.get(LoginViewModel::class.java)

        ViewUtils.handleInsetsWithPaddingForSides(
            view,
            InsetSide.TOP,
            InsetSide.START,
            InsetSide.END
        )

        val progressBar = view.findViewById<ProgressBar>(R.id.pb)
        val nameEditText = view.findViewById<EditText>(R.id.name_et)
        val passwordEditText = view.findViewById<EditText>(R.id.password_et)
        val loginButton = view.findViewById<Button>(R.id.login_btn).apply {
            setOnClickListener {
                viewModel.dispatch(
                    LoginAction.OnLoginClick(
                        login = nameEditText.text.toString(),
                        password = passwordEditText.text.toString()
                    )
                )
            }
        }

        val registrationButton = view.findViewById<Button>(R.id.register_btn).apply {
            setOnClickListener {
                viewModel.dispatch(CoreAction.ShowRegistration)
            }
        }

        viewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState: LoginViewState? ->
            viewState ?: return@observe

            if (viewState.isLoading) {
                loginButton.isEnabled = false
                nameEditText.isEnabled = false
                passwordEditText.isEnabled = false
                registrationButton.isEnabled = false

                progressBar.visibility = View.VISIBLE
            } else {
                loginButton.isEnabled = true
                nameEditText.isEnabled = true
                passwordEditText.isEnabled = true
                registrationButton.isEnabled = true

                progressBar.visibility = View.INVISIBLE
            }
        }
    }

    override fun getBackStackTag(): String = "LoginFragment"
}
