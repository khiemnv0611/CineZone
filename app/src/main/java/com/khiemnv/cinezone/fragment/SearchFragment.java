package com.khiemnv.cinezone.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.khiemnv.cinezone.R;
import com.khiemnv.cinezone.adapter.MovieAdapter;
import com.khiemnv.cinezone.model.MovieModel;
import com.khiemnv.cinezone.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    private MovieAdapter movieAdapter;
    private MovieViewModel movieViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Setup RecyclerView
        RecyclerView recyclerViewMovies = view.findViewById(R.id.recyclerViewMovies);
        recyclerViewMovies.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2 items per row

        // Setup Adapter
        movieAdapter = new MovieAdapter(getContext(), false);
        recyclerViewMovies.setAdapter(movieAdapter);

        // Setup ViewModel
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        movieViewModel.getMovies().observe(getViewLifecycleOwner(), movieModels -> {
            movieAdapter.setMovieList(movieModels);
        });

        // Setup Search
        TextInputEditText searchBar = view.findViewById(R.id.search_bar);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterMovies(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return view;
    }

    // Filter movies by query
    private void filterMovies(String query) {
        movieViewModel.getMovies().observe(getViewLifecycleOwner(), movieModels -> {
            List<MovieModel> filteredList = new ArrayList<>();
            for (MovieModel movie : movieModels) {
                if (movie.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(movie);
                }
            }
            movieAdapter.setMovieList(filteredList);
        });
    }
}