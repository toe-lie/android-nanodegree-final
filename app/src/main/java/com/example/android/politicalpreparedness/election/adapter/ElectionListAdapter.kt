package com.example.android.politicalpreparedness.election.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.databinding.ViewHolderElectionBinding
import com.example.android.politicalpreparedness.models.Election

class ElectionListAdapter(private val itemClickListener: ElectionItemClickListener) :
    ListAdapter<Election, ElectionViewHolder>(ElectionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {
        return ElectionViewHolder.from(parent, itemClickListener)
    }

    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

class ElectionViewHolder(
    private val binding: ViewHolderElectionBinding,
    private val itemClickListener: ElectionItemClickListener
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Election) {
        binding.election = item
        binding.itemClickListener = itemClickListener
        binding.executePendingBindings()
    }

    companion object {
        fun from(
            parent: ViewGroup,
            itemClickListener: ElectionItemClickListener
        ): ElectionViewHolder {
            val binding = ViewHolderElectionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return ElectionViewHolder(binding, itemClickListener)
        }
    }
}

class ElectionDiffCallback : DiffUtil.ItemCallback<Election>() {
    override fun areItemsTheSame(oldItem: Election, newItem: Election): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Election, newItem: Election): Boolean {
        return oldItem == newItem
    }

}

interface ElectionItemClickListener {
    fun onItemClick(election: Election)
}