package com.example.tictactoe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;


public class WelcomePage extends Fragment {
    Button button_enter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_welcome_page, container, false);

        // define button
        button_enter = view.findViewById(R.id.button_enter);

        // when clicked, the welcome page fragment disappears
        // Main Activity (Calculator opens)
        button_enter.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)

            {
                // getting the main activity
                MainActivity mainActivity = (MainActivity) getActivity();

                //define what view
                view.findViewById(R.id.layout_welcome).setVisibility(View.GONE);
                //removing the view
                container.removeView(view);
                // For linear layout, must reset
                // https://developer.android.com/reference/android/widget/LinearLayout.LayoutParams
                container.setLayoutParams(new LinearLayout.LayoutParams(0,0));

                // Confirmed fragment is removed on MainActivity
                mainActivity.fragmentClicked(true);
            }
        });

        return view;
    }
}