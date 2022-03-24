package com.example.photosdemo.ui.fragments.details

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.photosdemo.data.models.comment.CommentDtoIn
import com.example.photosdemo.data.models.comment.CommentDtoOut
import com.example.photosdemo.data.remote.SessionManager
import com.example.photosdemo.databinding.FragmentDetailsBinding
import com.example.photosdemo.util.toDate
import com.example.photosdemo.viewmodel.PhotosViewModel
import org.json.JSONObject
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: DetailsAdapter
    private lateinit var viewModel: PhotosViewModel
    private lateinit var sessionManager: SessionManager
    private var token: String? = null
    private val comments = mutableListOf<CommentDtoOut?>()

    private var alertDialog: AlertDialog? = null

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
        sessionManager = SessionManager(requireContext())
        token = sessionManager.fetchAuthToken()
        val image = viewModel.selectedImage
        adapter = DetailsAdapter(this)

        binding.apply {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            photo.load(image?.url)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val dt = image?.date?.let {
                    Instant.ofEpochSecond(it)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime()
                }
                val parsedDate = LocalDateTime.parse(dt.toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
                date.text = parsedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
            } else {
                val json = JSONObject()
                json.put("date", image?.date)
                val d = json.getInt("date").toDate()
                val year = d.toString().substring(d.toString().length - 4)
                val result = "${d.date}.${d.month.plus(1)}.${year} ${d.hours}:${d.minutes}"
                date.text = result
            }

            viewModel.getComments(sessionManager.fetchAuthToken()!!).observe(viewLifecycleOwner) { result ->
                comments.clear()
                result.data?.let { comments.addAll(it) }
                adapter.submitList(result.data)
            }

            sendButton.setOnClickListener {
            val commentDtoIn = CommentDtoIn(editComment.text.toString())
                token?.let { token -> viewModel.postComment(commentDtoIn, token) }
            }
        }
    }

    fun showDeleteDialog(position: Int) {
        val builder = AlertDialog.Builder(activity)
        builder.run {
            setTitle("Delete")
            setMessage("Do you want to delete a comment?")
            setPositiveButton("Yes") { _, _ ->
                deleteComment(position)
            }
            setNegativeButton("Cancel") { _, _ ->
            }
        }
        alertDialog = builder.create()
        alertDialog?.show()
    }

    private fun deleteComment(position: Int) {
        token?.let { viewModel.deleteComment(comments[position]!!.id, it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}