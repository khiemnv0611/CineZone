package com.khiemnv.cinezone.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.khiemnv.cinezone.R;
import com.khiemnv.cinezone.adapter.MovieAdapter;
import com.khiemnv.cinezone.adapter.ViewPagerAdapter;
import com.khiemnv.cinezone.model.MovieModel;
import com.khiemnv.cinezone.repository.MovieRepository;
import com.khiemnv.cinezone.viewmodel.MovieViewModel;

import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerViewTop10Movies;
    private RecyclerView recyclerViewTheaterMovies;
    private RecyclerView recyclerViewUpComingMovies;
    private MovieAdapter movieAdapter;
    private MovieViewModel movieViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout fragment_home và trả về View
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Thiết lập top header
        View topHeaderLayout = view.findViewById(R.id.topHeaderLayout);

        // Banner
        ViewPager2 viewPager = view.findViewById(R.id.viewPager);
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);

        // Danh sách các hình ảnh để hiển thị
        List<Integer> imageList = Arrays.asList(
                R.drawable.poster_1,
                R.drawable.poster_2,
                R.drawable.poster_3
                // Thêm các ảnh khác tùy ý
        );

        // Thiết lập Adapter cho ViewPager2
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity(), imageList);
        viewPager.setAdapter(adapter);

        // Liên kết ViewPager2 với TabLayout
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
        }).attach();

        // Card
        // Currently trending
        recyclerViewTop10Movies = view.findViewById(R.id.recyclerViewTop10Movies);
        recyclerViewTop10Movies.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        movieAdapter = new MovieAdapter(getContext(), false);
        recyclerViewTop10Movies.setAdapter(movieAdapter);

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        movieViewModel.getTop10Movies().observe(getViewLifecycleOwner(), new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                movieAdapter.setMovieList(movieModels);
            }
        });

        // Theater Movies
        recyclerViewTheaterMovies = view.findViewById(R.id.recyclerViewTheaterMovies);
        recyclerViewTheaterMovies.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        MovieAdapter movieAdapterTheater = new MovieAdapter(getContext(), false);  // Adapter cho theater movies
        recyclerViewTheaterMovies.setAdapter(movieAdapterTheater);

        movieViewModel.getMoviesInTheater().observe(getViewLifecycleOwner(), new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                movieAdapterTheater.setMovieList(movieModels);  // Cập nhật dữ liệu cho theater adapter
            }
        });

        // Upcoming Movies
        recyclerViewUpComingMovies = view.findViewById(R.id.recyclerViewUpComingMovies);
        recyclerViewUpComingMovies.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        MovieAdapter movieAdapterUpcoming = new MovieAdapter(getContext(), false);  // Adapter cho upcoming movies
        recyclerViewUpComingMovies.setAdapter(movieAdapterUpcoming);

        movieViewModel.getUpcomingMovies().observe(getViewLifecycleOwner(), new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                movieAdapterUpcoming.setMovieList(movieModels);  // Cập nhật dữ liệu cho upcoming adapter
            }
        });

        // Trả về view sau khi thực hiện các thao tác cần thiết
        return view;
    }
}

