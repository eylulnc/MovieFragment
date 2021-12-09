package com.eylulcan.moviefragment.ui.moviedetail.more

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.eylulcan.moviefragment.R
import com.eylulcan.moviefragment.databinding.FragmentMoreBinding
import com.eylulcan.moviefragment.ui.moviedetail.DetailViewModel

class MoreFragment : Fragment() {

    private lateinit var binding: FragmentMoreBinding
    private lateinit var moreAdapter: MoreAdapter
    private val detailViewModel: DetailViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_more, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMoreBinding.bind(view)
        binding.moreRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        observeViewModel()
    }

    private fun observeViewModel(){
        detailViewModel.more.observe(viewLifecycleOwner, { movie ->
            moreAdapter = MoreAdapter(movie)
            binding.moreRecyclerView.adapter = moreAdapter
        })
    }

}