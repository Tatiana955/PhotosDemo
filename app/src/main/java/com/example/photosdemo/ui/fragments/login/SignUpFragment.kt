package com.example.photosdemo.ui.fragments.login

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.photosdemo.R
import com.example.photosdemo.data.models.security.SignUserDtoIn
import com.example.photosdemo.databinding.FragmentSignUpBinding
import com.example.photosdemo.viewmodel.PhotosViewModel
import com.google.android.material.snackbar.Snackbar
import java.util.regex.Pattern

class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private var navController: NavController? = null
    private lateinit var viewModel: PhotosViewModel
    private var alertDialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        viewModel = ViewModelProvider(requireActivity())[PhotosViewModel::class.java]

        binding.button.setOnClickListener {
            registration()
        }
    }

    private fun registration() {
        val pattern = "[a-z0-9_\\-.@]+"
        if (binding.editTextLogin.text.toString().length in 4..32
            && Pattern.matches(pattern, binding.editTextLogin.text.toString())
        ) {
            val login = binding.editTextLogin.text.toString()
            if (binding.editTextPassword.text.toString() == binding.editTextPasswordRetry.text.toString()
                && binding.editTextPassword.text.toString().length in 8..500
            ) {
                val password: String = binding.editTextPassword.text.toString()
                val signUserDtoIn = SignUserDtoIn(
                    login,
                    password
                )
                viewModel.postSignUp(signUserDtoIn)
            } else {
                snackbarPassword()
            }
        } else {
            snackbarLogin()
        }
    }

    private fun snackbarLogin() {
        val snackbar = Snackbar.make(requireView(), getString(R.string.check_login), Snackbar.LENGTH_LONG)
        snackbar.setAction(getString(R.string.details)
        ) {
            showDetailsDialog(getString(R.string.login_details))
        }
        snackbar.show()
    }

    private fun snackbarPassword() {
        val snackbar = Snackbar.make(requireView(), getString(R.string.check_password), Snackbar.LENGTH_LONG)
        snackbar.setAction(getString(R.string.details)) {
            showDetailsDialog(getString(R.string.password_details))
        }
        snackbar.show()
    }

    private fun showDetailsDialog(message: String) {
        val builder = AlertDialog.Builder(activity)
        builder.run {
            setMessage(message)
        }
        alertDialog = builder.create()
        alertDialog?.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}