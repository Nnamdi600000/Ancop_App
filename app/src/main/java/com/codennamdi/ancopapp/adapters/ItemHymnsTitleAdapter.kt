package com.codennamdi.ancopapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.codennamdi.ancopapp.databinding.ItemHymnsTitlesBinding
import com.codennamdi.ancopapp.models.Hymn

class ItemHymnsTitleAdapter(
    private val item: ArrayList<Hymn>,
    private val context: Context
) : RecyclerView.Adapter<ItemHymnsTitleAdapter.ViewHolder>() {
    private lateinit var onClickListener: OnClickListener

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
            onClickListener.onClick(position, item)
        }
    }

    override fun getItemCount(): Int {
        return item.size
    }

    //First step you create an interface
    interface OnClickListener {
        fun onClick(position: Int, items: Hymn)
    }

    //Secondly you would have to create an a function called setOnClickListener
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

}