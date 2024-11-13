package com.khiemnv.cinezone.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.khiemnv.cinezone.model.MovieModel;
import com.khiemnv.cinezone.repository.MovieRepository;

import java.util.List;

public class MovieViewModel extends ViewModel {
    private final LiveData<List<MovieModel>> movies;

    public MovieViewModel() {
        MovieRepository movieRepository = new MovieRepository();
        // Lấy dữ liệu từ repository
        movies = movieRepository.getMoviesFromFirebase();
        // Đẩy dữ liệu mẫu lên Firebase (nếu chưa có)
//        movieRepository.uploadSampleMovies();
    }

    public LiveData<List<MovieModel>> getMovies() {
        return movies;
    }
}
