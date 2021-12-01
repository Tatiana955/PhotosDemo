package com.example.photosdemo.ui.fragments.photos

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.photosdemo.data.models.image.ImageDtoOut
import com.example.photosdemo.databinding.PhotoLayoutBinding
import com.example.photosdemo.util.toDate
import org.json.JSONObject
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
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
                    val dt = data.date?.let {
                        Instant.ofEpochSecond(it)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime()
                    }
                    val parsedDate = LocalDate.parse(dt.toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
                    date.text = parsedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                } else {
                    val json = JSONObject()
                    json.put("date", data.date)
                    val d = json.getInt("date").toDate()
                    val year = d.toString().substring(d.toString().length - 4)
                    val result = "${d.date}.${d.month.plus(1)}.${year}"
                    date.text = result
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