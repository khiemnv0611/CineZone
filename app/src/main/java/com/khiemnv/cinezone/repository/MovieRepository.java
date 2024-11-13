package com.khiemnv.cinezone.repository;

import android.graphics.Movie;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.khiemnv.cinezone.model.MovieModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MovieRepository {
    private DatabaseReference databaseReference;
    private MutableLiveData<List<MovieModel>> moviesLiveData;

    public MovieRepository() {
        // Tham chiếu đến "Movies" trong Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Movies");
        moviesLiveData = new MutableLiveData<>();
    }

    // Phương thức này để tạo và đẩy dữ liệu mẫu lên Firebase
//    public void uploadSampleMovies() {
//        List<MovieModel> movieList = new ArrayList<>();
//
//        // Tạo dữ liệu mẫu với ID ngẫu nhiên
//        movieList.add(new MovieModel("Inception", "Sci-Fi", "Released", "A mind-bending thriller", "https://imageurl.com/inception.jpg", "", "", "1", "2010-07-16", 4.8, 1000));
//        movieList.add(new MovieModel("Titanic", "Romance", "Released", "A tragic love story", "https://imageurl.com/titanic.jpg", "", "", "1", "1997-12-19", 4.7, 1500));
//        movieList.add(new MovieModel("Avengers", "Action", "Released", "Superheroes save the world", "https://imageurl.com/avengers.jpg", "", "", "1", "2012-05-04", 4.6, 2000));
//
//        // Đẩy từng đối tượng MovieModel lên Firebase
//        for (MovieModel movie : movieList) {
//            // Thêm phim vào Firebase với movieId làm key
//            databaseReference.child(movie.getMovieId()).setValue(movie);
//        }
//    }

    public LiveData<List<MovieModel>> getMoviesFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<MovieModel> movieList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MovieModel movie = snapshot.getValue(MovieModel.class);
                    movieList.add(movie);
                }
                moviesLiveData.setValue(movieList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu cần
            }
        });
        return moviesLiveData;
    }
}