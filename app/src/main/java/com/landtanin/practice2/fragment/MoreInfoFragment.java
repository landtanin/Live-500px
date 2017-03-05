package com.landtanin.practice2.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inthecheesefactory.thecheeselibrary.view.SlidingTabLayout;
import com.landtanin.practice2.R;


/**
 * Created by nuuneoi on 11/16/2014.
 */
public class MoreInfoFragment extends Fragment {

    ViewPager mViewPager;
    private SlidingTabLayout slidingTabLayout;

    public MoreInfoFragment() {
        super();
    }

    public static MoreInfoFragment newInstance() {
        MoreInfoFragment fragment = new MoreInfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_more_info, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    @SuppressWarnings("UnusedParameters")
    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here
        // Note: State of variable initialized here could not be saved
        //       in onSavedInstanceState
        mViewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        mViewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {

                switch (position) {
                    case 0:
                        return PhotoSummaryFragment.newInstance();
                    case 1:
                        return PhotoInfoFragment.newInstance();
                    case 2:
                        return PhotoTagsFragment.newInstance();
                    default:
                        return null;
                }

            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public CharSequence getPageTitle(int position) {

                switch (position) {

                    case 0:
                        return "Summarykkkkkkkkkkkk";
                    case 1:
                        return "Infokkkkkkkkkkkkkk";
                    case 2:
                        return "Tagskkkkkkkkkkkkkkkkk";
                    default:
                        return "";
                }

            }
        });

        slidingTabLayout = (SlidingTabLayout) rootView.findViewById(R.id.slidingTabLayout);
        slidingTabLayout.setViewPager(mViewPager);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance (Fragment level's variables) State here
    }

    @SuppressWarnings("UnusedParameters")
    private void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore Instance (Fragment level's variables) State here
    }

}
