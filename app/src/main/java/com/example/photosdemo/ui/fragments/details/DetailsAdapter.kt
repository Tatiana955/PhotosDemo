package com.example.photosdemo.ui.fragments.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.photosdemo.R

class DetailsAdapter(
): RecyclerView.Adapter<DetailsAdapter.DetailsViewHolder>() {

    class DetailsViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        val comment = itemView.findViewById<TextView>(R.id.comment)
        val date = itemView.findViewById<TextView>(R.id.date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.details_layout, parent, false)
        return DetailsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DetailsViewHolder, position: Int) {
//        holder.comment.text =
//        holder.date.text =
    }

    override fun getItemCount(): Int {
        return 1
    }
}