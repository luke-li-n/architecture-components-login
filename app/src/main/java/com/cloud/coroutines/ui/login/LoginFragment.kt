package com.cloud.coroutines.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.cloud.coroutines.ViewModelFactory
import com.cloud.coroutines.R
import com.cloud.coroutines.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels {
        ViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setEvent()
    }

    private fun setEvent() {
        registerEditTextChanged(binding.username, binding.password)
        binding.password.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                login()
            }
            false
        }

        binding.login.setOnClickListener {
            login()
        }
    }

    private fun registerEditTextChanged(vararg edits: EditText) {
        for (e in edits) {
            e.doAfterTextChanged {
                validator()
            }
        }
    }

    private fun login() {
        binding.loading.visibility = View.VISIBLE
        loginViewModel.login(
            binding.username.text.toString(),
            binding.password.text.toString()
        ).observe(viewLifecycleOwner) { loginResult ->
            binding.loading.visibility = View.GONE
            loginResult.error?.let {
                showLoginFailed(it)
            }
            loginResult.success?.let {
                updateUiWithUser(it)
            }
        }
    }

    private fun validator() {
        loginViewModel.loginDataChanged(
            binding.username.text.toString(),
            binding.password.text.toString()
        ).observe(viewLifecycleOwner) { loginFormState ->
            binding.login.isEnabled = loginFormState.isDataValid
            loginFormState.usernameError?.let {
                binding.username.error = getString(it)
            }
            loginFormState.passwordError?.let {
                binding.password.error = getString(it)
            }
        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome) + model.displayName
        Toast.makeText(requireContext(), welcome, Toast.LENGTH_LONG).show()
    }

    private fun showLoginFailed(errorString: String) {
        Toast.makeText(requireContext(), errorString, Toast.LENGTH_LONG).show()
    }
}