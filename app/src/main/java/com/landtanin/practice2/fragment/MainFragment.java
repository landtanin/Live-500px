package com.landtanin.practice2.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.inthecheesefactory.thecheeselibrary.manager.Contextor;
import com.landtanin.practice2.R;
import com.landtanin.practice2.adapter.PhotoListAdapter;
import com.landtanin.practice2.dao.PhotoItemCollectionDao;
import com.landtanin.practice2.manager.HttpMangaer;
import com.landtanin.practice2.manager.PhotoListManager;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by nuuneoi on 11/16/2014.
 */
public class MainFragment extends Fragment {

    ListView mListView;
    PhotoListAdapter mPhotoListAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    PhotoListManager mPhotoListManager;
    Button btnNewPhotos;

    public MainFragment() {
        super();
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        initInstances(rootView);
        return rootView;
    }

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here
        mPhotoListManager = new PhotoListManager();



        btnNewPhotos = (Button) rootView.findViewById(R.id.btnNewPhotos);
        btnNewPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListView.smoothScrollToPosition(0);
                hideButtonNewPhotos();
            }
        });



        mListView = (ListView) rootView.findViewById(R.id.listView);
        mPhotoListAdapter = new PhotoListAdapter();
        mListView.setAdapter(mPhotoListAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.mainFragSwipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refreshData();

            }
        });

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }


            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                mSwipeRefreshLayout.setEnabled(firstVisibleItem==0);

            }
        });


        refreshData();

    }

    private void refreshData() {
        if (mPhotoListManager.getCount() == 0) {
            reloadData();
        }
        else
            reloadDataNewer();
    }

    class PhotoListCallback implements Callback<PhotoItemCollectionDao> {

        public static final int MODE_RELOAD = 1;
        public static final int MODE_RELOAD_NEWER = 2;

        int mode;

        public PhotoListCallback(int mode) {

            this.mode = mode;

        }


        @Override

        public void onResponse(Call<PhotoItemCollectionDao> call, Response<PhotoItemCollectionDao> response) {

            mSwipeRefreshLayout.setRefreshing(false);

            if (response.isSuccessful()) {
                PhotoItemCollectionDao dao = response.body();

                // maintain scroll position process
                int firstVisiblePosition = mListView.getFirstVisiblePosition();
                View c = mListView.getChildAt(0);
                int top = c == null ? 0 : c.getTop();

                if (mode == MODE_RELOAD_NEWER) {
                    mPhotoListManager.insertDaoAtTopPosition(dao);
                } else {
                    mPhotoListManager.setPhotoItemCollectionDao(dao);
                }

                mPhotoListAdapter.setDao(mPhotoListManager.getPhotoItemCollectionDao());    // feed the updated dao to PhotoListAdapter
                mPhotoListAdapter.notifyDataSetChanged();

                // maintain scroll position process
                if (mode == MODE_RELOAD_NEWER) {

                    int additionalSize = (dao != null && dao.getData() != null) ? dao.getData().size() : 0;
                    mPhotoListAdapter.increaseLastPosition(additionalSize);
                    mListView.setSelectionFromTop(firstVisiblePosition + additionalSize, top);

                    if (additionalSize>0) {


                        showButtonNewPHotos();

                    }

                }

                Toast.makeText(Contextor.getInstance().getContext(),
                        "download completed",
                        Toast.LENGTH_SHORT)
                        .show();

            } else {
                try {
                    Toast.makeText(Contextor.getInstance().getContext(),
                            response.errorBody().string(),
                            Toast.LENGTH_SHORT)
                            .show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(Call<PhotoItemCollectionDao> call, Throwable t) {

            mSwipeRefreshLayout.setRefreshing(false);

            Toast.makeText(Contextor.getInstance().getContext(),
                    t.toString(),
                    Toast.LENGTH_SHORT)
                    .show();

        }
    }


    private void reloadDataNewer() {

        int maxId = mPhotoListManager.getMaximumId();
        Call<PhotoItemCollectionDao> call = HttpMangaer.getInstance()
                .getService()
                .loadPhotoListAfterId(maxId);

        call.enqueue(new PhotoListCallback(PhotoListCallback.MODE_RELOAD_NEWER));

    }

    private void reloadData() {
        Call<PhotoItemCollectionDao> call = HttpMangaer.getInstance().getService().loadPhotoList();
        call.enqueue(new PhotoListCallback(PhotoListCallback.MODE_RELOAD));
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /*
     * Save Instance State Here
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here
    }

    /*
     * Restore Instance State Here
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore Instance State here
        }
    }

    public void showButtonNewPHotos() {


        btnNewPhotos.setVisibility(View.VISIBLE);
        Animation anim = AnimationUtils.loadAnimation(Contextor.getInstance().getContext(), R.anim.zoom_fade_in);
        btnNewPhotos.startAnimation(anim);

    }

    public void hideButtonNewPhotos() {

        btnNewPhotos.setVisibility(View.GONE);
        Animation anim = AnimationUtils.loadAnimation(Contextor.getInstance().getContext(), R.anim.zoom_fade_out);
        btnNewPhotos.startAnimation(anim);

    }
}
