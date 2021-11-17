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
import com.example.photosdemo.databinding.FragmentSignUpBinding
import com.example.photosdemo.viewmodel.PhotosViewModel
import com.google.android.material.snackbar.Snackbar

class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private var navController: NavController? = null
    private lateinit var viewModel: PhotosViewModel

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
        if (binding.editTextPassword.text.toString() == binding.editTextPasswordRetry.text.toString()
            && binding.editTextPassword.text.toString() != "") {
            val password: String = binding.editTextPassword.text.toString()
            val signUserDtoIn = SignUserDtoIn(
                binding.editTextLogin.text.toString(),
                password
            )
            viewModel.postSignUp(signUserDtoIn)
            viewModel.getToken()
            navController?.navigate(R.id.photosFragment)
        } else {
            Snackbar.make(requireView(), "Check your password", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}