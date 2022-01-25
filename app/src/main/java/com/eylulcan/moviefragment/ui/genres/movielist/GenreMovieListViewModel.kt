package com.eylulcan.moviefragment.ui.genres.movielist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eylulcan.moviefragment.api.MovieAPI
import com.eylulcan.moviefragment.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenreMovieListViewModel @Inject constructor(private var retrofit: MovieAPI): ViewModel() {

    private val movieList = MutableLiveData<Movie>()
    val movies: LiveData<Movie> get() = movieList
    var lastLoadedPage = 1

    fun getMovieListByGenre(genreId: Int, pageNo: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            if (pageNo == 1) {
                movieList.postValue(Movie())
            }
            val response = retrofit.getMovieByGenreId(genreId = genreId, pageNo = pageNo)
            response.let {
                if (response.isSuccessful) {
                    response.body()?.let {
                        movieList.postValue(it)
                    }
                }
            }
        }
    }
}