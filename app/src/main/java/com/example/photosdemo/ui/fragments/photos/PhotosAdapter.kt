package com.example.photosdemo.ui.fragments.photos

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.photosdemo.data.models.image.ImageDtoOut
import com.example.photosdemo.databinding.PhotoLayoutBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class PhotosAdapter(
    val fragment: PhotosFragment
    ) : ListAdapter<ImageDtoOut, PhotosAdapter.PhotosViewHolder>(PhotoComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        val binding = PhotoLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = PhotosViewHolder(binding)

        holder.itemView.setOnClickListener {
            fragment.navToDetails(holder.absoluteAdapterPosition)
        }

        holder.itemView.setOnLongClickListener {
            fragment.showDeleteDialog(holder.absoluteAdapterPosition)
            true
        }
        return holder
    }

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    class PhotosViewHolder(private val binding: PhotoLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ImageDtoOut) {
            binding.apply {
                photo.load(data.url)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val parsedDate = LocalDate.parse(data.date.toString(), DateTimeFormatter.ofPattern("yyyyMMdd"))
                    date.text = parsedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                } else {
                    val strDate = data.date.toString()
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
    }

    class PhotoComparator : DiffUtil.ItemCallback<ImageDtoOut>() {
        override fun areItemsTheSame(oldItem: ImageDtoOut, newItem: ImageDtoOut) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ImageDtoOut, newItem: ImageDtoOut) =
            oldItem == newItem
    }
}