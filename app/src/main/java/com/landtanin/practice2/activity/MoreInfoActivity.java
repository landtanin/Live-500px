package com.landtanin.practice2.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.landtanin.practice2.R;
import com.landtanin.practice2.fragment.MoreInfoFragment;

public class MoreInfoActivity extends AppCompatActivity {

    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);

        initInstance();

        if (savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.moreInfoContentContainer, MoreInfoFragment.newInstance())
                    .commit();

        }

    }

    private void initInstance() {

        mToolbar = (Toolbar) findViewById(R.id.moreInfoToolbar);

        setSupportActionBar(mToolbar);

    }


}
