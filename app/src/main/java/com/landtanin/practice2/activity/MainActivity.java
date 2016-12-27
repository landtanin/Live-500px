package com.landtanin.practice2.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.landtanin.practice2.R;
import com.landtanin.practice2.fragment.MainFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainer, MainFragment.newInstance())
                    .commit();

        }

    }
}
