package com.khiemnv.cinezone.pages;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.khiemnv.cinezone.R;

public class SignUpFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout cho fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        // Lấy TextView từ layout
        TextView loginText = view.findViewById(R.id.login_text);

        // Lấy nội dung chuỗi từ resources
        String text = getString(R.string.signin_prompt); // Đảm bảo bạn có chuỗi này trong resources

        // Sử dụng SpannableString để thay đổi màu và tạo sự kiện nhấn
        SpannableString spannable = new SpannableString(text);

        // Sử dụng ContextCompat.getColor() để lấy màu
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.mainColor));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                // Chuyển sang fragment_signin khi nhấn
                Fragment fragment = new SignInFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

                // Áp dụng hiệu ứng chuyển đổi
                transaction.setCustomAnimations(
                        R.anim.slide_in_left, // Animation vào của fragment mới
                        R.anim.slide_out_right, // Animation ra của fragment hiện tại
                        R.anim.slide_in_right, // Animation quay lại vào của fragment cũ (khi nhấn Back)
                        R.anim.slide_out_left // Animation quay lại ra của fragment mới
                );

                transaction.replace(R.id.auth_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ContextCompat.getColor(requireContext(), R.color.mainColor)); // Đảm bảo màu vẫn hiển thị khi nhấn
                ds.setUnderlineText(false); // Gạch chân
                ds.setFakeBoldText(true); // In đậm
            }
        };

        // Xác định vị trí của từ "Sign in" hoặc "Đăng nhập" dựa trên chuỗi hiện tại
        int start, end;
        if (text.contains("Sign in")) {
            start = text.indexOf("Sign in");
            end = start + "Sign in".length();
        } else if (text.contains("Đăng nhập")) {
            start = text.indexOf("Đăng nhập");
            end = start + "Đăng nhập".length();
        } else {
            // Đề phòng trường hợp không tìm thấy chuỗi
            start = 0;
            end = 0;
        }

        // Áp dụng màu và tính nhấn cho phần "Sign in" hoặc "Đăng nhập"
        spannable.setSpan(colorSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Đặt SpannableString vào TextView và kích hoạt khả năng nhấn
        loginText.setText(spannable);
        loginText.setMovementMethod(LinkMovementMethod.getInstance());

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

