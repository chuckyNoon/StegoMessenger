package com.example.diplomclient.login

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import com.example.diplomclient.R
import com.example.diplomclient.arch.infra.AbsFragment

class LoginFragment : AbsFragment(R.layout.fragment_login) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModelProvider = ViewModelProvider(this, appViewModelFactory)
        val viewModel = viewModelProvider.get(LoginViewModel::class.java)

        val progressBar = view.findViewById<ProgressBar>(R.id.pb)
        val nameEditText = view.findViewById<EditText>(R.id.name_et)
        val passwordEditText = view.findViewById<EditText>(R.id.password_et)
        val loginButton = view.findViewById<Button>(R.id.login_btn).apply {
            setOnClickListener {
                viewModel.dispatch(
                    LoginAction.OnLoginClick(
                        name = nameEditText.text.toString(),
                        password = passwordEditText.text.toString()
                    )
                )
            }
        }

        viewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState: LoginViewState? ->
            viewState ?: return@observe

            if (viewState.isProgressBarVisible) {
                loginButton.text = ""
                progressBar.visibility = View.VISIBLE
            } else {
                loginButton.text = "Login"
                progressBar.visibility = View.GONE
            }
        }
    }

    override fun getBackStackTag(): String = "LoginFragment"
}
