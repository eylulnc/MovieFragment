package com.eylulcan.moviefragment.ui.discover

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eylulcan.moviefragment.MainActivity
import com.eylulcan.moviefragment.R
import com.eylulcan.moviefragment.databinding.FragmentDiscoverBinding
import com.eylulcan.moviefragment.model.ResultMovie
import com.eylulcan.moviefragment.util.Utils
import com.google.firebase.auth.FirebaseAuth
import me.samlss.broccoli.Broccoli

class DiscoverFragment : Fragment(), MovieListener, Toolbar.OnMenuItemClickListener,RecyclerViewListener {

    private lateinit var fragmentBinding: FragmentDiscoverBinding
    private val discoverViewModel: DiscoverViewModel by activityViewModels()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var lastLoadedPageItems: List<ResultMovie>? = emptyList()
    private var nowPlayingResultList: ArrayList<ResultMovie> = arrayListOf()
    private var enableToRequest: Boolean = false
    private lateinit var sharedPreferenceForSessionID: SharedPreferences
    private var sessionID: String? = null
    private val allListItems: ArrayList<ArrayList<ResultMovie>> = arrayListOf()
    private var placeholderNeeded = arrayListOf<View>()
    private var broccoli = Broccoli()
    private lateinit var recyclerViewAdapter: FlexibleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferenceForSessionID = requireContext().getSharedPreferences(
            getString(R.string.app_package_name), Context.MODE_PRIVATE
        )

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_discover, container, false)
        fragmentBinding = FragmentDiscoverBinding.bind(view)
        setPlaceholders()
        setToolbarMenu()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeViewModel()
        discoverViewModel.getPopularMovieList()
        discoverViewModel.lastLoadedPage++
        sessionID = sharedPreferenceForSessionID.getString(getString(R.string.sessionId), null)
        if (sessionID == null) {
            discoverViewModel.getGuestSession()
        }
    }

    private fun observeViewModel() {
        discoverViewModel.popularMovies.observe(viewLifecycleOwner, { movieData ->
            movieData?.let { movie ->
                allListItems.clear()
                allListItems.add(movie.results as java.util.ArrayList<ResultMovie>)
                discoverViewModel.getTopRatedMovieList()
            }
        })
        discoverViewModel.topRatedMovies.observe(viewLifecycleOwner, { movieData ->
            movieData?.let { movie ->
                allListItems.add(movie.results as java.util.ArrayList<ResultMovie>)
                discoverViewModel.getNowPlayingMovieList()
            }
        })
        discoverViewModel.nowPlaying.observe(viewLifecycleOwner, { movieData ->
            movieData?.let { movie ->
                broccoli.removeAllPlaceholders()
                lastLoadedPageItems =
                    if (movie.results?.let { lastLoadedPageItems?.containsAll(it) } == true) {
                        emptyList()
                    } else {
                        movie.results
                    }
                nowPlayingResultList.addAll(lastLoadedPageItems ?: arrayListOf())
                fragmentBinding.discoverMainRecyclerView.layoutManager = LinearLayoutManager(
                    this@DiscoverFragment.context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                recyclerViewAdapter = FlexibleAdapter(allListItems, this, this)
                fragmentBinding.discoverMainRecyclerView.adapter = recyclerViewAdapter
                recyclerViewAdapter.movieResults = nowPlayingResultList
                enableToRequest = true
                allListItems.add(movie.results as java.util.ArrayList<ResultMovie>)
            }
        })
        discoverViewModel.sessionId.observe(viewLifecycleOwner, { session ->
            sharedPreferenceForSessionID.edit()
                .putString(getString(R.string.sessionId), session.sessionID).commit()
        })

    }


    private fun setupUI() {
        if (Utils.isTablet(requireContext())) {
            val height = fragmentBinding.discoverMainRecyclerView.layoutParams.height
            fragmentBinding.discoverMainRecyclerView.layoutParams.height =
                (height * 1.1).toInt()
        }
    }

    override fun onMovieClicked(resultMovie: ResultMovie) {
        val movieDataBundle = bundleOf((getString(R.string.movieId)) to resultMovie.id)
        this.parentFragment?.parentFragment?.findNavController()?.navigate(
            R.id.action_dashboardFragment_to_movieDetailFragment,
            movieDataBundle, null, null
        )
    }

    override fun recyclerScrollListener(recyclerView: RecyclerView) {

        if (recyclerView.layoutManager is GridLayoutManager) {
            val layoutManager =
                recyclerView.layoutManager as GridLayoutManager
            val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
            if (lastVisiblePosition == nowPlayingResultList.size - 1 && enableToRequest) {
                discoverViewModel.getNowPlayingMovieList()
                discoverViewModel.lastLoadedPage++
                enableToRequest = false
            }
        }

    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.logout) {
            auth.signOut()
            Toast.makeText(context, R.string.logged_out_movie_list, Toast.LENGTH_LONG).show()
            this.parentFragment?.parentFragment?.findNavController()?.navigate(
                R.id.action_dashboardFragment_to_loginFragment, null,
                NavOptions.Builder().setPopUpTo(R.id.dashboardFragment, true).build()
            )
            return true
        } else if (item?.itemId == R.id.search_button) {
            this.parentFragment?.parentFragment?.findNavController()
                ?.navigate(R.id.action_dashboardFragment_to_searchFragment)
        }
        return false
    }

    private fun setToolbarMenu() {
        val toolbar = fragmentBinding.toolbar
        toolbar.inflateMenu(R.menu.menu)
        toolbar.setOnMenuItemClickListener(this)
    }

    private fun setPlaceholders() {
        placeholderNeeded.addAll(arrayListOf(
            fragmentBinding.discoverMainRecyclerView
        ))

        Utils.addPlaceholders(
            broccoli = broccoli,
            itemList = placeholderNeeded
        )
    }

}