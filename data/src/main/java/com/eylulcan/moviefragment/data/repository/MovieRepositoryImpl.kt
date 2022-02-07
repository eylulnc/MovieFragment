package com.eylulcan.moviefragment.data.repository

import com.eylulcan.moviefragment.data.datasource.MovieDataSource
import com.eylulcan.moviefragment.domain.entity.GuestSessionEntity
import com.eylulcan.moviefragment.domain.entity.MovieEntity
import com.eylulcan.moviefragment.domain.entity.PostRatingBodyEntity
import com.eylulcan.moviefragment.domain.entity.RatingPostResponseEntity
import com.eylulcan.moviefragment.domain.repository.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(private val dataSource: MovieDataSource) : MovieRepository {

    override suspend fun getPopularData(): MovieEntity? {
        return dataSource.getPopularData()
    }

    override suspend fun getTopRatedData(): MovieEntity? {
        return dataSource.getTopRatedData()
    }

    override suspend fun getNowPlayingData(): MovieEntity? {
        return dataSource.getNowPlayingData()
    }

    override suspend fun getUpcomingData(): MovieEntity? {
        return dataSource.getUpcomingData()
    }

    override suspend fun getGuestSessionId(): GuestSessionEntity? {
        return dataSource.getGuestSessionId()
    }

    override suspend fun getGenreMovieList(genreId: Int, pageNo: Int): MovieEntity? {
        return dataSource.getGenreMovieList(genreId, pageNo)
    }

    override suspend fun postRateMovie(
        movieId: Int,
        guestSessionId: String,
        postBody: PostRatingBodyEntity
    ): RatingPostResponseEntity? {
        return dataSource.postRateMovie(movieId,guestSessionId,postBody)
    }


}