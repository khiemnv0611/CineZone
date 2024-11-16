package com.khiemnv.cinezone.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khiemnv.cinezone.model.MovieModel;
import com.khiemnv.cinezone.repository.MovieRepository;

import java.util.List;

public class MovieViewModel extends ViewModel {
    private final LiveData<List<MovieModel>> movies;
    private final LiveData<List<MovieModel>> top10Movies;
    private final LiveData<List<MovieModel>> upcomingMovies;
    private final LiveData<List<MovieModel>> inTheaterMovies;
    private final MutableLiveData<List<MovieModel>> suggestedMovies = new MutableLiveData<>();

    private final MovieRepository movieRepository;

    public MovieViewModel() {
        movieRepository = new MovieRepository();
        // Đẩy dữ liệu mẫu lên Firebase (nếu chưa có)
//        movieRepository.uploadSampleMovies();
        // Khởi tạo dữ liệu từ repository
        movies = movieRepository.getMoviesFromFirebase();
        top10Movies = movieRepository.getTop10Movies();
        upcomingMovies = movieRepository.getUpcomingMovies();
        inTheaterMovies = movieRepository.getMoviesInTheater();
    }

    public LiveData<List<MovieModel>> getMovies() {
        return movies;
    }

    public LiveData<List<MovieModel>> getTop10Movies() {
        return top10Movies;
    }

    public LiveData<List<MovieModel>> getUpcomingMovies() {
        return upcomingMovies;
    }

    public LiveData<List<MovieModel>> getMoviesInTheater() {
        return inTheaterMovies;
    }

    // Lấy danh sách phim gợi ý theo thể loại
    public LiveData<List<MovieModel>> getSuggestedMoviesByGenres(List<String> genres, String currentMovieId) {
        movieRepository.getMoviesByGenres(genres, currentMovieId).observeForever(suggestedMovies::setValue);
        return suggestedMovies;
    }
}
