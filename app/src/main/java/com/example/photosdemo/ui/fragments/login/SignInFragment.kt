package com.example.photosdemo.ui.fragments.login

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
import com.example.photosdemo.data.remote.SessionManager
import com.example.photosdemo.databinding.FragmentSignInBinding
import com.example.photosdemo.viewmodel.PhotosViewModel
import com.google.android.material.snackbar.Snackbar

class SignInFragment : Fragment() {
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private var navController: NavController? = null
    private lateinit var viewModel: PhotosViewModel
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        viewModel = ViewModelProvider(requireActivity())[PhotosViewModel::class.java]
        sessionManager = SessionManager(requireContext())

        binding.button.setOnClickListener {
            signIn()

            viewModel.userToken.observe(requireActivity(), {
                if (it != null) {
                    sessionManager.saveAuthToken(it)
                    navController?.navigate(R.id.photosFragment)
                } else {
                    Snackbar.make(
                        requireView(),
                        "Check the correctness of the entered data",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

    private fun signIn() {
        val signUserDtoIn = SignUserDtoIn(
            binding.editTextLogin.text.toString(),
            binding.editTextPassword.text.toString()
        )
        viewModel.postSignIn(signUserDtoIn)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}