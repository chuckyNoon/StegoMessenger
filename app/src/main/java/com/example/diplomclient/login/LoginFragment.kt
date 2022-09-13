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
import com.example.diplomclient.common.handleInsetsWithPaddingForSides
import com.example.diplomclient.main.navigation.CoreAction

class LoginFragment : AbsFragment(R.layout.fragment_login) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModelProvider = ViewModelProvider(this, appViewModelFactory)
        val viewModel = viewModelProvider.get(LoginViewModel::class.java)

        val progressBar = view.findViewById<ProgressBar>(R.id.pb)
        val nameEditText = view.findViewById<EditText>(R.id.name_et)
        val passwordEditText = view.findViewById<EditText>(R.id.password_et)
        val loginButton = view.findViewById<View>(R.id.login_btn).apply {
            setOnClickListener {
                viewModel.dispatch(
                    LoginAction.ClickLogin(
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

        view.findViewById<View>(R.id.content).apply {
            handleInsetsWithPaddingForSides(
                InsetSide.TOP,
                InsetSide.START,
                InsetSide.END
            )
        }

        view.findViewById<View>(R.id.btn_block).apply {
            handleInsetsWithPaddingForSides(
                InsetSide.BOTTOM,
                InsetSide.START,
                InsetSide.END
            )
        }

        viewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState: LoginViewState? ->
            viewState ?: return@observe

            loginButton.isEnabled = viewState.isLoginButtonEnabled
            nameEditText.isEnabled = viewState.isNameEditTextEnabled
            passwordEditText.isEnabled = viewState.isPasswordEditTextEnabled
            registrationButton.isEnabled = viewState.isRegistrationButtonEnabled

            progressBar.visibility = if (viewState.isProgressBarVisible) View.VISIBLE else View.INVISIBLE
        }
    }

    override fun getBackStackTag(): String = "LoginFragment"
}
