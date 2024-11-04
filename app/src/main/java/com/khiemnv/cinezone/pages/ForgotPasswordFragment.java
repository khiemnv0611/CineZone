package com.khiemnv.cinezone.pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.khiemnv.cinezone.R;

public class ForgotPasswordFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout cho fragment
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        // Lấy TextView và ImageView
        TextView returnToLoginText = view.findViewById(R.id.tvReturnToLogin);
        ImageView returnToLoginArrow = view.findViewById(R.id.ivArrow);

        // Xử lý sự kiện nhấn cho TextView "Trở về Đăng nhập"
        View.OnClickListener returnToLoginListener = v -> {
            // Quay lại SignInFragment
            Fragment fragment = new SignInFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

            // Áp dụng hiệu ứng chuyển đổi
            transaction.setCustomAnimations(
                    R.anim.slide_in_right, // Animation vào của fragment mới
                    R.anim.slide_out_left, // Animation ra của fragment hiện tại
                    R.anim.slide_in_left, // Animation quay lại vào của fragment cũ (khi nhấn Back)
                    R.anim.slide_out_right // Animation quay lại ra của fragment mới
            );

            transaction.replace(R.id.auth_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        };

        // Gán sự kiện cho TextView và ImageView
        returnToLoginText.setOnClickListener(returnToLoginListener);
        returnToLoginArrow.setOnClickListener(returnToLoginListener);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Xử lý sự kiện nhấn nút Back
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Quay lại fragment trước đó
                FragmentManager fragmentManager = getParentFragmentManager();
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    fragmentManager.popBackStack();
                } else {
                    // Nếu không có fragment nào để quay lại, gọi super để thoát activity
                    requireActivity().finish();
                }
            }
        });
    }
}

