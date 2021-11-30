package com.eylulcan.moviefragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eylulcan.moviefragment.databinding.GenresFragmentRecyclerRowBinding

class GenresAdapter(private val genreList: GenreList) :
    RecyclerView.Adapter<GenresAdapter.ViewHolder>() {

    class ViewHolder(val binding: GenresFragmentRecyclerRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            GenresFragmentRecyclerRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val genres = genreList.genres
        genres?.get(position)?.let { genre ->
            holder.binding.genresRowText.text = genre.name
        }
    }

    override fun getItemCount(): Int {
        genreList.genres?.let { genreList ->
            return genreList.size
        } ?: run {
            return 0
        }
    }
}