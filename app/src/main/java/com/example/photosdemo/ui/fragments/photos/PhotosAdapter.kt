package com.example.photosdemo.ui.fragments.photos

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.photosdemo.R
import com.example.photosdemo.data.models.image.ImageDtoOut

class PhotosAdapterAsync(
    val fragment: PhotosFragment
    ) : RecyclerView.Adapter<PhotosAdapterAsync.PhotosAsyncViewHolder>() {

    private val mDiffer: AsyncListDiffer<ImageDtoOut>

    private val diffCallback: DiffUtil.ItemCallback<ImageDtoOut> = object : DiffUtil.ItemCallback<ImageDtoOut>() {

        override fun areItemsTheSame(oldItem: ImageDtoOut, newItem: ImageDtoOut): Boolean {
            return TextUtils.equals(oldItem.id.toString(), newItem.id.toString())
        }

        override fun areContentsTheSame(oldItem: ImageDtoOut, newItem: ImageDtoOut): Boolean {
            return oldItem.url == newItem.url
        }
    }

    init {
        mDiffer = AsyncListDiffer(this, diffCallback)
    }

    override fun getItemCount(): Int {
        return mDiffer.currentList.size
    }

    fun submitList(data: MutableList<ImageDtoOut?>) {
        mDiffer.submitList(data)
    }

    private fun getItem(position: Int): ImageDtoOut {
        return mDiffer.currentList[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosAsyncViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.photo_layout, parent, false)
        val holder = PhotosAsyncViewHolder(itemView)

        holder.itemView.setOnClickListener {
            fragment.navToDetails(holder.absoluteAdapterPosition)
        }

        holder.itemView.setOnLongClickListener {
            fragment.showDeleteDialog(holder.absoluteAdapterPosition)
            true
        }
        return holder
    }

    override fun onBindViewHolder(holder: PhotosAsyncViewHolder, position: Int) {
        holder.setData(getItem(position))
    }

    class PhotosAsyncViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val photo: ImageView = itemView.findViewById(R.id.photo)
        private val date: TextView = itemView.findViewById(R.id.date)

        fun setData(data: ImageDtoOut) {
            photo.load(data.url)
            date.text = data.date.toString()
        }
    }
}