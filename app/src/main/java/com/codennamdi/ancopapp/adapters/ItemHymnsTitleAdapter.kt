package com.codennamdi.ancopapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.codennamdi.ancopapp.database.HymnEntity
import com.codennamdi.ancopapp.databinding.ItemHymnsTitlesBinding
import com.codennamdi.ancopapp.models.HymnDataModel

class ItemHymnsTitleAdapter(
    private val item: ArrayList<HymnDataModel>, private val context: Context
) : RecyclerView.Adapter<ItemHymnsTitleAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemHymnsTitlesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val hymnNumber = binding.textViewHymnNumberId
        val hymnTitle = binding.textViewHymnTitleId
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemHymnsTitlesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = item[position]

        holder.hymnNumber.text = item.num.toString()
        holder.hymnTitle.text = item.title

        holder.itemView.setOnClickListener {
            Toast.makeText(context, "Clicked!", Toast.LENGTH_LONG).show()
        }
    }

    override fun getItemCount(): Int {
        return item.size
    }
}