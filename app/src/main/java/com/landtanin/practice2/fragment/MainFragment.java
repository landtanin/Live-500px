package com.landtanin.practice2.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.inthecheesefactory.thecheeselibrary.manager.Contextor;
import com.landtanin.practice2.R;
import com.landtanin.practice2.adapter.PhotoListAdapter;
import com.landtanin.practice2.dao.PhotoItemCollectionDao;
import com.landtanin.practice2.dao.PhotoItemDao;
import com.landtanin.practice2.dataType.MutableInteger;
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

    // Variable

    ListView mListView;
    PhotoListAdapter mPhotoListAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    PhotoListManager mPhotoListManager;
    Button btnNewPhotos;
    boolean isLoadingMore = false;
    MutableInteger lastPositionInteger;

    public interface FragmentLisnener {
        void onPhotoItemClicked(PhotoItemDao dao);
    }

    /**************
     * Methods
     ***********/

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init(savedInstanceState);

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {

        mPhotoListManager =  new PhotoListManager();
        lastPositionInteger = new MutableInteger(-1);

        //************Persistance storage
//        File dir = getContext().getFilesDir();
//        File dir = getContext().getDir("Hello", Context.MODE_PRIVATE);
//        File dir = getContext().getCacheDir();
//        Log.d("Storage", String.valueOf(dir));
//
//        File file = new File(dir, "testfile.txt");
//        try {
//
//            FileOutputStream fos = new FileOutputStream(file);
//            fos.write("hello".getBytes());
//            fos.close();
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        //************SharedPreference
//        SharedPreferences prefs = getContext().getSharedPreferences("dummy", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();
//        // Add/Edit/Delete
//        editor.putString("StringName", "StringValue");
//        editor.apply();
//
//        SharedPreferences prefs2 = getContext().getSharedPreferences("dummy2", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor2 = prefs2.edit();
//        // Add/Edit/Delete
//        editor2.putString("StringName2", "StringValue2");
//        editor2.apply();
//
//        String valueFromSharedPref = prefs.getString("StringName2", null);
//        Log.d("valueFromSharedPref", valueFromSharedPref);

    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here

        btnNewPhotos = (Button) rootView.findViewById(R.id.btnNewPhotos);
        btnNewPhotos.setOnClickListener(buttonClickListener);

        mListView = (ListView) rootView.findViewById(R.id.listView); // Fragment variable
        mPhotoListAdapter = new PhotoListAdapter(lastPositionInteger);
        mPhotoListAdapter.setDao(mPhotoListManager.getPhotoItemCollectionDao());
        mListView.setAdapter(mPhotoListAdapter);


        mListView.setOnItemClickListener(listViewItemClickListener);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.mainFragSwipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(pullToRefreshListener);

        mListView.setOnScrollListener(listViewScrollListener);

        if (savedInstanceState == null) {
            refreshData();
        }


    }

    private void refreshData() {
        if (mPhotoListManager.getCount() == 0) {
            reloadData();
        }
        else
            reloadDataNewer();
    }

    private void reloadDataNewer() {

        int maxId = mPhotoListManager.getMaximumId();
        Call<PhotoItemCollectionDao> call = HttpMangaer.getInstance()
                .getService()
                .loadPhotoListAfterId(maxId);

        call.enqueue(new PhotoListCallback(PhotoListCallback.MODE_RELOAD_NEWER));

    }

    private void loadMoreData() {

        if (isLoadingMore) {
            return;
        }
        isLoadingMore = true;

        int minId = mPhotoListManager.getMaximumId();
        Call<PhotoItemCollectionDao> call = HttpMangaer.getInstance()
                .getService()
                .loadPhotoListAfterId(minId);

        call.enqueue(new PhotoListCallback(PhotoListCallback.MODE_LOAD_MORE));

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
        outState.putBundle("photoListManager", mPhotoListManager.onSaveInstanceState());
        outState.putBundle("lastPositionInteger", lastPositionInteger.onSaveInstanceState());

    }


    private void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore instance state here

        mPhotoListManager.onRestoreInstanceState(savedInstanceState.getBundle("photoListManager"));
        lastPositionInteger.onRestoreInstanceState(savedInstanceState.getBundle("lastPositionInteger"));

    }

    /*
     * Restore Instance State Here
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void showButtonNewPHotos() {

        btnNewPhotos.setVisibility(View.VISIBLE);
        Animation anim = AnimationUtils.loadAnimation(Contextor.getInstance().getContext(), R.anim.zoom_fade_in);
        btnNewPhotos.startAnimation(anim);

    }

    private void hideButtonNewPhotos() {

        btnNewPhotos.setVisibility(View.GONE);
        Animation anim = AnimationUtils.loadAnimation(Contextor.getInstance().getContext(), R.anim.zoom_fade_out);
        btnNewPhotos.startAnimation(anim);

    }

    private void showToast(String text) {

        Toast.makeText(Contextor.getInstance().getContext(),
                text,
                Toast.LENGTH_SHORT)
                .show();

    }

    /*****************
     * Listener Zone
     ****************/

    final AdapterView.OnItemClickListener listViewItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

            // Wrong, we should not do this
//            Intent intent = new Intent(getContext(), MoreInfoActivity.class);
//            startActivity(intent);

            if (position < mPhotoListManager.getCount()) {

                PhotoItemDao dao = mPhotoListManager.getPhotoItemCollectionDao().getData().get(position);
                FragmentLisnener listener = (FragmentLisnener) getActivity();
                listener.onPhotoItemClicked(dao);

            }

        }
    };

    SwipeRefreshLayout.OnRefreshListener pullToRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {

            refreshData();

        }
    };

    View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (view == btnNewPhotos) {

                mListView.smoothScrollToPosition(0);
                hideButtonNewPhotos();

            }

        }
    };

    AbsListView.OnScrollListener listViewScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView absListView, int i) {

        }

        @Override
        public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            if (absListView == mListView) {
                mSwipeRefreshLayout.setEnabled(firstVisibleItem == 0);

                if (firstVisibleItem + visibleItemCount >= totalItemCount) {
                    if (mPhotoListManager.getCount() > 0) {

                        loadMoreData();
                    }
                }
            }

        }
    };



    /*****************
     * Inner Class Zone
     ****************/

    class PhotoListCallback implements Callback<PhotoItemCollectionDao> {

        public static final int MODE_RELOAD = 1;
        public static final int MODE_RELOAD_NEWER = 2;
        public static final int MODE_LOAD_MORE = 3;

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

                } else if (mode == MODE_LOAD_MORE) {

                    mPhotoListManager.appendDaoAtBottomPosition(dao);

                } else {

                    mPhotoListManager.setPhotoItemCollectionDao(dao);
                }

                clearLoadintMoreFlayIfCapable(mode);
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

                showToast("Load Completed");

            } else {

                clearLoadintMoreFlayIfCapable(mode);

                try {

                    showToast(response.errorBody().string());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(Call<PhotoItemCollectionDao> call, Throwable t) {

            clearLoadintMoreFlayIfCapable(mode);

            mSwipeRefreshLayout.setRefreshing(false);

            showToast(t.toString());

        }

        private void clearLoadintMoreFlayIfCapable(int mode) {

            if (mode == MODE_LOAD_MORE) {

                isLoadingMore = false;
            }

        }
    }


}
