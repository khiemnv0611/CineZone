package com.khiemnv.cinezone.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.khiemnv.cinezone.model.Actor;
import com.khiemnv.cinezone.model.MovieModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MovieRepository {
    private DatabaseReference databaseReference;
    private MutableLiveData<List<MovieModel>> moviesLiveData;

    public MovieRepository() {
        // Tham chiếu đến "Movies" trong Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Movies");
        moviesLiveData = new MutableLiveData<>();
    }

//    public void uploadSampleMovies() {
//        List<MovieModel> movieList = new ArrayList<>();
//
//        // Tạo một số diễn viên mẫu
//        Actor actor1 = new Actor("Tom Hanks", "https://example.com/actor1.jpg");
//        Actor actor2 = new Actor("Natalie Portman", "https://example.com/actor2.jpg");
//        Actor actor3 = new Actor("Robert Downey Jr.", "https://example.com/actor3.jpg");
//
//        List<Actor> actors = Arrays.asList(actor1, actor2, actor3);
//
//        // Tạo một số đạo diễn mẫu
//        List<String> directors = Arrays.asList("Steven Spielberg", "Christopher Nolan");
//
//        // Tạo một số công ty sản xuất mẫu
//        List<String> productionCompanies = Arrays.asList("Warner Bros.", "Marvel Studios");
//
//        // Tạo một số thể loại mẫu
//        List<String> genre = Arrays.asList("Action", "Drama", "Adventure");
//
//        // Tạo movie mẫu đầu tiên
//        movieList.add(new MovieModel(
//                "Phi vụ triệu đô",  // Tên phim
//                Arrays.asList("Crime", "Comedy", "Thriller"),  // Thể loại
//                "Phim lẻ",  // Loại phim
//                13,  // Đánh giá độ tuổi
//                "Hoàn thành",  // Trạng thái
//                "Một nhóm tội phạm tài ba thực hiện một vụ cướp khổng lồ tại Las Vegas.",  // Mô tả
//                "https://example.com/heist.jpg",  // Hình ảnh
//                "https://example.com/heist_video.mp4",  // Video chính
//                "https://example.com/heist_trailer.mp4",  // Trailer
//                "N/A",  // Season
//                "Mỹ",  // Quốc gia
//                Arrays.asList("Warner Bros."),  // Công ty sản xuất
//                Arrays.asList("The Duffer Brothers"),  // Đạo diễn
//                parseDate("2001-12-7"),  // Ngày phát hành
//                116,  // Thời gian (phút)
//                4000000,  // Số lượt xem
//                8.0,  // Điểm đánh giá trung bình
//                700000,  // Tổng số đánh giá
//                actors  // Danh sách diễn viên
//        ));
//
//        // Đẩy từng đối tượng MovieModel lên Firebase
//        for (MovieModel movie : movieList) {
//            // Thêm phim vào Firebase với movieId làm key
//            databaseReference.child(movie.getMovieId().toString()).setValue(movie);
//        }
//    }

    //     Phương thức chuyển chuỗi ngày thành Date
//    private Date parseDate(String dateStr) {
//        try {
//            // Dùng SimpleDateFormat để chuyển chuỗi thành Date
//            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//            return format.parse(dateStr);
//        } catch (ParseException e) {
//            e.printStackTrace();
//            return null;  // Trả về null nếu có lỗi
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
        Query top10Query = databaseReference.orderByChild("viewCount").limitToLast(10);

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

    // Up coming
    public LiveData<List<MovieModel>> getUpcomingMovies() {
        MutableLiveData<List<MovieModel>> upcomingMoviesLiveData = new MutableLiveData<>();
        Query upcomingQuery = databaseReference.orderByChild("status").equalTo("Sắp ra mắt");

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
        Query inTheaterQuery = databaseReference.orderByChild("type").equalTo("Phim chiếu rạp");

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