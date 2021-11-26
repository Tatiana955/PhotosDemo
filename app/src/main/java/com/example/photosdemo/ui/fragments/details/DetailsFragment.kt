package com.example.photosdemo.ui.fragments.details

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.photosdemo.databinding.FragmentDetailsBinding
import com.example.photosdemo.viewmodel.PhotosViewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: DetailsAdapter
    private lateinit var viewModel: PhotosViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel = ViewModelProvider(requireActivity())[PhotosViewModel::class.java]
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val image = viewModel.selectedImage
        adapter = DetailsAdapter()

        binding.apply {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            photo.load(image?.url)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val parsedDate =
                    LocalDate.parse(image?.date.toString(), DateTimeFormatter.ofPattern("yyyyMMdd"))
                date.text = parsedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            } else {
                val strDate = image?.date.toString()
                val formatter = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
                try {
                    val d: Date? = formatter.parse(strDate)
                    val year = d.toString().substring(d.toString().length - 4)
                    val result = "${d?.date}.${d?.month?.plus(1)}.${year}"
                    date.text = result
                } catch (e: ParseException) {
                    Log.d("!!!e", e.message.toString())
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}