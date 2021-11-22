package com.example.photosdemo.ui.fragments.photos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.photosdemo.data.models.image.ImageDtoOut
import com.example.photosdemo.databinding.PhotoLayoutBinding

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
                date.text = data.date.toString()
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