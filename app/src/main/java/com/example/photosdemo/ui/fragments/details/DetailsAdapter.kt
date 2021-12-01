package com.example.photosdemo.ui.fragments.details

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.photosdemo.data.models.comment.CommentDtoOut
import com.example.photosdemo.databinding.DetailsLayoutBinding
import com.example.photosdemo.util.toDate
import org.json.JSONObject
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

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

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val dt = Instant.ofEpochSecond(data.date)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime()
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

    class CommentComparator : DiffUtil.ItemCallback<CommentDtoOut>() {
        override fun areItemsTheSame(oldItem: CommentDtoOut, newItem: CommentDtoOut) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CommentDtoOut, newItem: CommentDtoOut) =
            oldItem == newItem
    }
}