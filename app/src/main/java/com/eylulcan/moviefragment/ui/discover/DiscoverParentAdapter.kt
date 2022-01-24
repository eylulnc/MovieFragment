package com.eylulcan.moviefragment.ui.discover

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.eylulcan.moviefragment.R
import com.eylulcan.moviefragment.databinding.DiscoverParentFragmentBinding
import com.eylulcan.moviefragment.model.ResultMovie
import javax.inject.Inject
import javax.inject.Named

class DiscoverParentAdapter @Inject constructor() :
    RecyclerView.Adapter<DiscoverParentAdapter.ViewHolder>() {

    private val viewPool = RecyclerView.RecycledViewPool()
    private var parentOnItemClickListener: ((id: Int) -> Unit)? = null

    class ViewHolder(val binding: DiscoverParentFragmentBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            DiscoverParentFragmentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return movieResults.size
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val parent = movieResults[position]

        if (position == 2) {
            holder.binding.categoryText.text =
                holder.itemView.context.getString(R.string.now_playing)
            val childLayoutManager =
                GridLayoutManager(holder.binding.discoverRecyclerView.context, 3)
            childLayoutManager.initialPrefetchItemCount = 3
            holder.binding.discoverRecyclerView.apply {
                layoutManager = childLayoutManager
                val childAdapter = DiscoverChildAdapter()
                adapter = childAdapter
                childAdapter.setOnItemClickListener {
                    parentOnItemClickListener?.let { it1 -> it1(it) }
                }
                childAdapter.movieResults = parent
                setRecycledViewPool(viewPool)
            }
        } else {
            if (position == 0) {
                holder.binding.categoryText.text =
                    holder.itemView.context.getString(R.string.most_popular)
            } else if (position == 1) {
                holder.binding.categoryText.text =
                    holder.itemView.context.getString(R.string.top_rated)
            }
            val childLayoutManager =
                LinearLayoutManager(
                    holder.binding.discoverRecyclerView.context,
                    RecyclerView.HORIZONTAL,
                    false
                )
            childLayoutManager.initialPrefetchItemCount = 3
            holder.binding.discoverRecyclerView.apply {
                layoutManager = childLayoutManager
                val childAdapter = DiscoverChildAdapter()
                childAdapter.setOnItemClickListener {
                    parentOnItemClickListener?.let { it1 -> it1(it) }
                }
                adapter = childAdapter
                childAdapter.movieResults = parent
                setRecycledViewPool(viewPool)
            }
        }
    }

    private val diffUtil = object : DiffUtil.ItemCallback<ArrayList<ResultMovie>>() {
        override fun areItemsTheSame(
            oldItem: ArrayList<ResultMovie>,
            newItem: ArrayList<ResultMovie>
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: ArrayList<ResultMovie>,
            newItem: ArrayList<ResultMovie>
        ): Boolean {
            return oldItem.equals(newItem)
        }

    }

    private val recyclerListDiffer = AsyncListDiffer(this, diffUtil)

    var movieResults: List<ArrayList<ResultMovie>>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    fun setOnItemClickListener(listener: (id: Int) -> Unit) {
        parentOnItemClickListener = listener
    }

}