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
//        // Tạo các actor mẫu
//        Actor actor1 = new Actor("Leonardo DiCaprio", "https://upload.wikimedia.org/wikipedia/commons/3/36/Leonardo_DiCaprio_2014.jpg");
//        Actor actor2 = new Actor("Kate Winslet", "https://upload.wikimedia.org/wikipedia/commons/8/8e/Kate_Winslet.jpg");
//        Actor actor3 = new Actor("Robert Downey Jr.", "https://upload.wikimedia.org/wikipedia/commons/d/d6/Robert_Downey_Jr_in_2014.jpg");
//        Actor actor4 = new Actor("Ezra Miller", "https://upload.wikimedia.org/wikipedia/commons/5/5e/Ezra_Miller_2018.jpg");
//        Actor actor5 = new Actor("Sam Worthington", "https://upload.wikimedia.org/wikipedia/commons/e/ec/Sam_Worthington_2013.jpg");
//
//        // Tạo dữ liệu mẫu với ID ngẫu nhiên và thêm actors vào mỗi phim
//        movieList.add(new MovieModel(
//                "Inception",
//                Arrays.asList("Khoa học viễn tưởng", "Hành động"),  // genre
//                "Phim lẻ",                             // type
//                "14+",        // ageRating
//                "Hoàn thành",                         // status
//                "Bộ phim này kể về một giấc mơ",      // description
//                "https://images-na.ssl-images-amazon.com/images/I/71uKM+LdgFL.jpg", // imageUrl
//                "",                                   // videoUrl
//                "",                                   // trailerUrl
//                "1",                                  // season
//                "USA",                                // country
//                Arrays.asList("Warner Bros.", "Syncopy"), // productionCompanies
//                parseDate("2010-07-16"),              // releaseDate (Date type)
//                120,                                  // duration
//                60,                                   // viewCount
//                9.8,                                  // averageRating
//                1000,                                 // totalRatings
//                Arrays.asList(actor1, actor2)         // actors
//        ));
//
//        movieList.add(new MovieModel(
//                "Titanic",
//                Collections.singletonList("Tình cảm"),             // genre
//                "Phim chiếu rạp",                          // type
//                "18+",                                    // ageRating
//                "Hoàn thành",                          // status
//                "Chuyện tình đẹp giữa Jack và Rose",  // description
//                "https://i.ebayimg.com/images/g/gnEAAOSwP~tW4HMS/s-l1200.jpg", // imageUrl
//                "",                                    // videoUrl
//                "",                                    // trailerUrl
//                "1",                                   // season
//                "USA",                                 // country
//                Arrays.asList("20th Century Fox", "Paramount Pictures"), // productionCompanies
//                parseDate("1997-12-19"),               // releaseDate (Date type)
//                120,                                   // duration
//                10,                                    // viewCount
//                9.7,                                   // averageRating
//                1500,                                  // totalRatings
//                Arrays.asList(actor1, actor2)          // actors
//        ));
//
//        // Thêm bộ phim "Avatar"
//        movieList.add(new MovieModel(
//                "Avatar 3",
//                Arrays.asList("Khoa học viễn tưởng", "Phiêu lưu"),    // genre
//                "Phim chiếu rạp",                                     // type
//                "18+",                                                // ageRating
//                "Sắp ra mắt",                                         // status
//                "Một người lính bị liệt trở thành người Na'vi và chiến đấu bảo vệ Pandora.", // description
//                "https://upload.wikimedia.org/wikipedia/en/b/b0/Avatar-Teaser-Poster.jpg", // imageUrl
//                "",                                                    // videoUrl
//                "",                                                    // trailerUrl
//                "1",                                                   // season
//                "USA",                                                 // country
//                Arrays.asList("20th Century Fox", "Lightstorm Entertainment"), // productionCompanies
//                parseDate("2009-12-18"),                               // releaseDate (Date type)
//                162,                                                   // duration
//                500,                                                   // viewCount
//                7.9,                                                   // averageRating
//                2000,                                                  // totalRatings
//                Arrays.asList(actor3, actor5)                          // actors
//        ));
//
//        // Thêm bộ phim "Avengers: Endgame"
//        movieList.add(new MovieModel(
//                "Avengers: Endgame",
//                Arrays.asList("Hành động", "Khoa học viễn tưởng", "Siêu anh hùng"),  // genre
//                "Phim chiếu rạp",                                                // type
//                "14+",                                                           // ageRating
//                "Hoàn thành",                                                    // status
//                "Các Avengers phải hợp tác để sửa chữa thiệt hại mà Thanos gây ra.", // description
//                "https://upload.wikimedia.org/wikipedia/en/0/0d/Avengers_Endgame_poster.jpg", // imageUrl
//                "",                                                              // videoUrl
//                "",                                                              // trailerUrl
//                "1",                                                             // season
//                "USA",                                                           // country
//                Arrays.asList("Marvel Studios", "Walt Disney Studios Motion Pictures"), // productionCompanies
//                parseDate("2019-04-26"),                                         // releaseDate (Date type)
//                181,                                                             // duration
//                1000,                                                            // viewCount
//                9.7,                                                             // averageRating
//                5000,                                                            // totalRatings
//                Arrays.asList(actor3, actor4, actor5)                            // actors
//        ));
//
//        // Thêm bộ phim "Breaking Bad" (Series)
//        movieList.add(new MovieModel(
//                "Breaking Bad",
//                Arrays.asList("Hình sự", "Tội phạm", "Drama"),  // genre
//                "Phim bộ",                                       // type
//                "18+", // ageRating
//                "Hoàn thành",                                    // status
//                "Walter White, một giáo viên hóa học, biến thành trùm ma túy trong nỗ lực cứu gia đình khỏi cảnh nghèo.", // description
//                "https://upload.wikimedia.org/wikipedia/commons/7/7d/Breaking_Bad_title_card.png", // imageUrl
//                "",                                               // videoUrl
//                "",                                               // trailerUrl
//                "1",                                              // season
//                "USA",                                            // country
//                Arrays.asList("Sony Pictures Television", "AMC Studios"), // productionCompanies
//                parseDate("2008-01-20"),                          // releaseDate (Date type)
//                47,                                               // duration (each episode duration)
//                2000,                                             // viewCount
//                9.5,                                              // averageRating
//                10000,                                            // totalRatings
//                Arrays.asList(actor2, actor3, actor4)             // actors
//        ));
//
//        // Đẩy từng đối tượng MovieModel lên Firebase
//        for (MovieModel movie : movieList) {
//            // Thêm phim vào Firebase với movieId làm key
//            databaseReference.child(movie.getMovieId().toString()).setValue(movie);
//        }
//    }

    // Phương thức chuyển chuỗi ngày thành Date
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

    // Get by genre
//    public LiveData<List<MovieModel>> getMoviesByGenre(String genre) {
//        MutableLiveData<List<MovieModel>> genreMoviesLiveData = new MutableLiveData<>();
//        Query genreQuery = databaseReference.orderByChild("genre").equalTo(genre);
//
//        genreQuery.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                List<MovieModel> movieList = new ArrayList<>();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    MovieModel movie = snapshot.getValue(MovieModel.class);
//                    movieList.add(movie);
//                }
//                genreMoviesLiveData.setValue(movieList);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Xử lý lỗi nếu cần
//            }
//        });
//
//        return genreMoviesLiveData;
//    }

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