package com.khiemnv.cinezone.components;

import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.khiemnv.cinezone.R;

public class BottomNavManager {
    private final Context context; // Thêm context vào đây
    private final BottomNavigationView bottomNavigationView;

    public BottomNavManager(Context context, BottomNavigationView bottomNavigationView) {
        this.context = context; // Khởi tạo context
        this.bottomNavigationView = bottomNavigationView;
    }

    public void setupBottomNavigation() {
        Menu menu = bottomNavigationView.getMenu();
        menu.add(0, 1, 0, context.getString(R.string.home)).setIcon(R.drawable.ic_home);
        menu.add(0, 2, 1, context.getString(R.string.search)).setIcon(R.drawable.ic_search);
        menu.add(0, 3, 2, context.getString(R.string.profile)).setIcon(R.drawable.ic_user);

        // Sử dụng listener để xử lý sự kiện khi chọn item
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case 1:
                    // Xử lý khi chọn Home
                    return true;
                case 2:
                    // Xử lý khi chọn Search
                    return true;
                case 3:
                    // Xử lý khi chọn Profile
                    return true;
                default:
                    return false;
            }
        });
    }
}
