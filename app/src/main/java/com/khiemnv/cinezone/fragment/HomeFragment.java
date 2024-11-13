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
    private RecyclerView recyclerViewMovies;
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
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {}).attach();

        // Card
        // Khởi tạo RecyclerView
        recyclerViewMovies = view.findViewById(R.id.recyclerViewMovies);
        recyclerViewMovies.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Khởi tạo Adapter
        movieAdapter = new MovieAdapter(getContext());
        recyclerViewMovies.setAdapter(movieAdapter);

        // Khởi tạo ViewModel và quan sát dữ liệu
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        movieViewModel.getMovies().observe(getViewLifecycleOwner(), new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                movieAdapter.setMovieList(movieModels);  // Cập nhật Adapter với dữ liệu mới
            }
        });

        // Trả về view sau khi thực hiện các thao tác cần thiết
        return view;
    }
}

