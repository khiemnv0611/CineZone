package com.khiemnv.cinezone.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.khiemnv.cinezone.R;
import com.khiemnv.cinezone.adapter.ViewPagerAdapter;

import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout fragment_home và trả về View
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Thiết lập top header
        View topHeaderLayout = view.findViewById(R.id.topHeaderLayout);

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

        // Trả về view sau khi thực hiện các thao tác cần thiết
        return view;
    }
}

