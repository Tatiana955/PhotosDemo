package com.example.photosdemo.ui.fragments.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.photosdemo.data.models.comment.CommentDtoOut
import com.example.photosdemo.databinding.DetailsLayoutBinding

class DetailsAdapter(
    val fragment: DetailsFragment
    ) : ListAdapter<CommentDtoOut, DetailsAdapter.DetailsViewHolder>(CommentComparator())  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsViewHolder {
        val binding = DetailsLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = DetailsViewHolder(binding)

        holder.itemView.setOnLongClickListener {
            fragment.showDeleteDialog(holder.absoluteAdapterPosition)
            true
        }
        return holder
    }

    override fun onBindViewHolder(holder: DetailsViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    class DetailsViewHolder(private val binding: DetailsLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: CommentDtoOut) {
            binding.apply {
                comment.text = data.text
                date.text = data.date.toString()
            }
        }
    }

    class CommentComparator : DiffUtil.ItemCallback<CommentDtoOut>() {
        override fun areItemsTheSame(oldItem: CommentDtoOut, newItem: CommentDtoOut) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CommentDtoOut, newItem: CommentDtoOut) =
            oldItem == newItem
    }
}