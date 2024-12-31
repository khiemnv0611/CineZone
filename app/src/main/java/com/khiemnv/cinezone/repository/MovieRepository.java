package com.khiemnv.cinezone.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.khiemnv.cinezone.model.MovieModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MovieRepository {
    private final DatabaseReference movieRef;
    private final MutableLiveData<List<MovieModel>> moviesLiveData;

    public MovieRepository() {
        // Tham chiếu đến "Movies" trong Firebase Realtime Database
        movieRef = FirebaseDatabase.getInstance().getReference("Movies");
        moviesLiveData = new MutableLiveData<>();
    }

    // Get all
    public LiveData<List<MovieModel>> getMoviesFromFirebase() {
        movieRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<MovieModel> movieList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MovieModel movie = snapshot.getValue(MovieModel.class);
                    movieList.add(movie);
                }
                moviesLiveData.setValue(movieList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu cần
            }
        });
        return moviesLiveData;
    }

    // Get top 10
    public LiveData<List<MovieModel>> getTop10Movies() {
        MutableLiveData<List<MovieModel>> top10MoviesLiveData = new MutableLiveData<>();
        Query top10Query = movieRef.orderByChild("viewCount").limitToLast(10);

        top10Query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<MovieModel> movieList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MovieModel movie = snapshot.getValue(MovieModel.class);
                    movieList.add(movie);
                }
                // Đảo ngược danh sách để có thứ tự từ lượt xem nhiều đến ít
                Collections.reverse(movieList);
                top10MoviesLiveData.setValue(movieList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu cần
            }
        });

        return top10MoviesLiveData;
    }

    // Up coming
    public LiveData<List<MovieModel>> getUpcomingMovies() {
        MutableLiveData<List<MovieModel>> upcomingMoviesLiveData = new MutableLiveData<>();
        Query upcomingQuery = movieRef.orderByChild("status").equalTo("Sắp ra mắt");

        upcomingQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<MovieModel> movieList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MovieModel movie = snapshot.getValue(MovieModel.class);
                    movieList.add(movie);
                }
                upcomingMoviesLiveData.setValue(movieList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu cần
            }
        });

        return upcomingMoviesLiveData;
    }

    // Theater
    public LiveData<List<MovieModel>> getMoviesInTheater() {
        MutableLiveData<List<MovieModel>> moviesInTheaterLiveData = new MutableLiveData<>();
        Query inTheaterQuery = movieRef.orderByChild("type").equalTo("Phim chiếu rạp");

        inTheaterQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<MovieModel> movieList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MovieModel movie = snapshot.getValue(MovieModel.class);
                    movieList.add(movie);
                }
                moviesInTheaterLiveData.setValue(movieList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu cần
            }
        });

        return moviesInTheaterLiveData;
    }

    // Similar genre
    public LiveData<List<MovieModel>> getMoviesByGenres(List<String> genres, String currentMovieId) {
        MutableLiveData<List<MovieModel>> moviesByGenres = new MutableLiveData<>();
        List<MovieModel> aggregatedList = new ArrayList<>();
        List<String> checkedGenres = new ArrayList<>();

        // Gọi hàm đệ quy
        getMoviesByGenreRecursive(genres, 0, aggregatedList, moviesByGenres, checkedGenres, currentMovieId);

        return moviesByGenres;
    }

    private void getMoviesByGenreRecursive(List<String> genres, int index, List<MovieModel> aggregatedList,
                                           MutableLiveData<List<MovieModel>> moviesByGenres,
                                           List<String> checkedGenres, String currentMovieId) {
        if (index >= genres.size() || aggregatedList.size() >= 10) {
            moviesByGenres.setValue(aggregatedList);
            return;
        }

        String currentGenre = genres.get(index);

        if (checkedGenres.contains(currentGenre)) {
            getMoviesByGenreRecursive(genres, index + 1, aggregatedList, moviesByGenres, checkedGenres, currentMovieId);
            return;
        }

        movieRef.orderByChild("genre/0").equalTo(currentGenre)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data : snapshot.getChildren()) {
                            MovieModel movie = data.getValue(MovieModel.class);

                            // Loại bỏ phim hiện tại và kiểm tra trùng lặp
                            if (!aggregatedList.contains(movie)) {
                                assert movie != null;
                                if (!movie.getMovieId().equals(currentMovieId)) {
                                    aggregatedList.add(movie);
                                }
                            }

                            if (aggregatedList.size() >= 10) break;
                        }

                        checkedGenres.add(currentGenre);
                        getMoviesByGenreRecursive(genres, index + 1, aggregatedList, moviesByGenres, checkedGenres, currentMovieId);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Xử lý lỗi nếu cần
                        getMoviesByGenreRecursive(genres, index + 1, aggregatedList, moviesByGenres, checkedGenres, currentMovieId);
                    }
                });
    }
}