package com.landtanin.practice2.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.gson.Gson;
import com.inthecheesefactory.thecheeselibrary.manager.Contextor;
import com.landtanin.practice2.dao.PhotoItemCollectionDao;
import com.landtanin.practice2.dao.PhotoItemDao;

import java.util.ArrayList;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class PhotoListManager {

    // Essential of Singleton template
//    private static PhotoListManager instance;
//
//    public static PhotoListManager getInstance() {
//        if (instance == null)
//            instance = new PhotoListManager();
//        return instance;
//    }

    private Context mContext;
    private PhotoItemCollectionDao mPhotoItemCollectionDao;

    public PhotoListManager() {
        mContext = Contextor.getInstance().getContext();

        loadCache();

    }

    public PhotoItemCollectionDao getPhotoItemCollectionDao() {
        return mPhotoItemCollectionDao;
    }

    public void setPhotoItemCollectionDao(PhotoItemCollectionDao photoItemCollectionDao) {
        mPhotoItemCollectionDao = photoItemCollectionDao;

        saveCache();

    }

    public void insertDaoAtTopPosition(PhotoItemCollectionDao newDao) {

        if (mPhotoItemCollectionDao == null) {

            mPhotoItemCollectionDao = new PhotoItemCollectionDao();
        }
        if (mPhotoItemCollectionDao.getData() == null) {

            mPhotoItemCollectionDao.setData(new ArrayList<PhotoItemDao>());

        }

        mPhotoItemCollectionDao.getData().addAll(0, newDao.getData());

        saveCache();

    }

    public void appendDaoAtBottomPosition(PhotoItemCollectionDao newDao) {

        if (mPhotoItemCollectionDao == null) {

            mPhotoItemCollectionDao = new PhotoItemCollectionDao();
        }
        if (mPhotoItemCollectionDao.getData() == null) {

            mPhotoItemCollectionDao.setData(new ArrayList<PhotoItemDao>());

        }

        mPhotoItemCollectionDao.getData().addAll(newDao.getData().size(), newDao.getData());

        saveCache();

    }

    public int getMaximumId() {

        // Handle null cases
        if (mPhotoItemCollectionDao == null) {
            return 0;
        }
        if (mPhotoItemCollectionDao.getData() == null) {
            return 0;
        }
        if (mPhotoItemCollectionDao.getData().size() == 0) {
            return 0;
        }

        // calculation
        int maxId = mPhotoItemCollectionDao.getData().get(0).getId();
        for (int i = 1; i < mPhotoItemCollectionDao.getData().size(); i++) {
            maxId = Math.max(maxId, getPhotoItemCollectionDao().getData().get(i).getId());
        }
        return maxId;

    }

    public int getMinimumId() {

        // Handle null cases
        if (mPhotoItemCollectionDao == null) {
            return 0;
        }
        if (mPhotoItemCollectionDao.getData() == null) {
            return 0;
        }
        if (mPhotoItemCollectionDao.getData().size() == 0) {
            return 0;
        }

        // calculation
        int minId = mPhotoItemCollectionDao.getData().get(0).getId();
        for (int i = 1; i < mPhotoItemCollectionDao.getData().size(); i++) {
            minId = Math.min(minId, getPhotoItemCollectionDao().getData().get(i).getId());
        }
        return minId;

    }

    public int getCount(){

        if (mPhotoItemCollectionDao == null) {
            return 0;
        }
        if (mPhotoItemCollectionDao.getData() == null) {
            return 0;
        }

        return mPhotoItemCollectionDao.getData().size();

    }

    public Bundle onSaveInstanceState(){

        Bundle bundle = new Bundle();
        bundle.putParcelable("dao", mPhotoItemCollectionDao);
        return bundle;

    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {

        mPhotoItemCollectionDao = savedInstanceState.getParcelable("dao");

    }

    private void saveCache() {

        PhotoItemCollectionDao cacheDao = new PhotoItemCollectionDao();
        if (mPhotoItemCollectionDao != null && mPhotoItemCollectionDao.getData() != null) {
            cacheDao.setData(mPhotoItemCollectionDao.getData()
                    .subList(0, Math.min(20, getPhotoItemCollectionDao().getData().size())));
        }
        String json = new Gson().toJson(cacheDao);

        SharedPreferences prefs = mContext.getSharedPreferences("photos",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        // Add/Edit/Delete
        editor.putString("json", json);
        editor.apply();

    }

    private void loadCache(){

        SharedPreferences prefs = mContext.getSharedPreferences("photos", Context.MODE_PRIVATE);
        String json = prefs.getString("json", null);
        if (json == null) {
            return;
        }
        mPhotoItemCollectionDao = new Gson().fromJson(json, PhotoItemCollectionDao.class);

    }
}

