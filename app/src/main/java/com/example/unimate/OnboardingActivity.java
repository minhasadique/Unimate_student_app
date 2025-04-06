package com.example.unimate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private OnboardingAdapter adapter;
    private MaterialButton btnSkip, btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if the user has already seen onboarding
        SharedPreferences prefs = getSharedPreferences("UnimatePrefs", MODE_PRIVATE);
        if (prefs.getBoolean("onboarding_complete", false)) {
            startActivity(new Intent(this, MainActivity22.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_onboarding);

        viewPager = findViewById(R.id.viewPager);
        btnSkip = findViewById(R.id.btnSkip);
        btnNext = findViewById(R.id.btnNext);

        List<OnboardingItem> items = new ArrayList<>();
        items.add(new OnboardingItem("Welcome to UNIMATE", "Your personalized student assistant.", R.drawable.robot));
        items.add(new OnboardingItem("Stay Updated", "Get course-specific news & announcements.", R.drawable.updated));
        items.add(new OnboardingItem("Connect with Peers", "Network with students and collaborate on projects.", R.drawable.connect));
        items.add(new OnboardingItem("Let's Get Started!", "Sign up now and explore UNIMATE!", R.drawable.welcome));

        adapter = new OnboardingAdapter(items);
        viewPager.setAdapter(adapter);

        btnSkip.setOnClickListener(v -> skipOnboarding());
        btnNext.setOnClickListener(v -> {
            if (viewPager.getCurrentItem() < items.size() - 1) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            } else {
                skipOnboarding();
            }
        });
    }

    private void skipOnboarding() {
        SharedPreferences.Editor editor = getSharedPreferences("UnimatePrefs", MODE_PRIVATE).edit();
        editor.putBoolean("onboarding_complete", true);
        editor.apply();

        startActivity(new Intent(this, MainActivity22.class));
        finish();
    }
}
