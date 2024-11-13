package com.khiemnv.cinezone.repository;

import android.graphics.Movie;

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
//        movieList.add(new MovieModel("Inception", "Khoa học viễn tưởng ", "Hoàn thành", "Một bộ phim kinh dị rối trí", "https://images-na.ssl-images-amazon.com/images/I/71uKM+LdgFL.jpg", "", "", "1", "2010-07-16", 120, 60, 4.8, 1000));
//        movieList.add(new MovieModel("Titanic", "Tình cảm", "Hoàn thành", "Một câu chuyện tình bi thảm", "https://i.ebayimg.com/images/g/gnEAAOSwP~tW4HMS/s-l1200.jpg", "", "", "1", "1997-12-19", 120, 10, 4.7, 1500));
//        movieList.add(new MovieModel("Avengers", "Hành động", "Hoàn thành", "Siêu anh hùng cứu thế giới", "https://images-na.ssl-images-amazon.com/images/I/91syHT466TL.jpg", "", "", "1", "2012-05-04", 180, 4, 4.6, 2000));
//        movieList.add(new MovieModel("The Flash", "Hành động", "Sắp chiếu", "Một bộ phim về siêu anh hùng tốc độ", "https://upload.wikimedia.org/wikipedia/commons/4/47/Flash_poster.jpg", "", "", "4", "2024-06-12", 140, 30, 0, 0));
//        movieList.add(new MovieModel("Avatar 3", "Khoa học viễn tưởng", "Chiếu rạp", "Tiếp nối câu chuyện về hành tinh Pandora", "https://upload.wikimedia.org/wikipedia/commons/6/67/Avatar_The_Way_of_Water_logo.png", "", "", "5", "2024-12-20", 150, 15, 0, 0));
//
//        // Đẩy từng đối tượng MovieModel lên Firebase
//        for (MovieModel movie : movieList) {
//            // Thêm phim vào Firebase với movieId làm key
//            databaseReference.child(movie.getMovieId()).setValue(movie);
//        }
//    }

    // Get all
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

    // Get top 10
    public LiveData<List<MovieModel>> getTop10Movies() {
        MutableLiveData<List<MovieModel>> top10MoviesLiveData = new MutableLiveData<>();
        Query top10Query = databaseReference.orderByChild("viewCount").limitToLast(10);  // Lấy 10 phim có lượt xem cao nhất

        top10Query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu cần
            }
        });

        return top10MoviesLiveData;
    }

    // Get by genre
    public LiveData<List<MovieModel>> getMoviesByGenre(String genre) {
        MutableLiveData<List<MovieModel>> genreMoviesLiveData = new MutableLiveData<>();
        Query genreQuery = databaseReference.orderByChild("genre").equalTo(genre);

        genreQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<MovieModel> movieList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MovieModel movie = snapshot.getValue(MovieModel.class);
                    movieList.add(movie);
                }
                genreMoviesLiveData.setValue(movieList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu cần
            }
        });

        return genreMoviesLiveData;
    }

    // Get by status
    // Up coming
    public LiveData<List<MovieModel>> getUpcomingMovies() {
        MutableLiveData<List<MovieModel>> upcomingMoviesLiveData = new MutableLiveData<>();
        Query upcomingQuery = databaseReference.orderByChild("status").equalTo("Sắp chiếu");

        upcomingQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<MovieModel> movieList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MovieModel movie = snapshot.getValue(MovieModel.class);
                    movieList.add(movie);
                }
                upcomingMoviesLiveData.setValue(movieList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu cần
            }
        });

        return upcomingMoviesLiveData;
    }

    // Theater
    public LiveData<List<MovieModel>> getMoviesInTheater() {
        MutableLiveData<List<MovieModel>> moviesInTheaterLiveData = new MutableLiveData<>();
        Query inTheaterQuery = databaseReference.orderByChild("status").equalTo("Chiếu rạp");

        inTheaterQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<MovieModel> movieList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MovieModel movie = snapshot.getValue(MovieModel.class);
                    movieList.add(movie);
                }
                moviesInTheaterLiveData.setValue(movieList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu cần
            }
        });

        return moviesInTheaterLiveData;
    }

}