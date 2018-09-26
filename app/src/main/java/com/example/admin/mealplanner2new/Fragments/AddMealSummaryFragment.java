package com.example.admin.mealplanner2new.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.mealplanner2new.R;
import com.example.admin.mealplanner2new.Views.AddTodayMealActivity;


public class AddMealSummaryFragment extends Fragment {

    View view_main;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view_main = inflater.inflate(R.layout.fragment_add_meal_summary, container, false);

        return view_main;
    }

    public static AddMealSummaryFragment newInstance(int page, String title) {
        AddMealSummaryFragment fragmentAddMealSummary = new AddMealSummaryFragment();
        Bundle args = new Bundle();
        args.putInt("6", page);
        args.putString("Meal Summary", title);
        fragmentAddMealSummary.setArguments(args);
        return fragmentAddMealSummary;
    }

}
