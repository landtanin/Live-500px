package com.landtanin.practice2.manager;

import android.content.Context;

import com.inthecheesefactory.thecheeselibrary.manager.Contextor;
import com.landtanin.practice2.dao.PhotoItemCollectionDao;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class PhotoListManager {

    private static PhotoListManager instance;

    public static PhotoListManager getInstance() {
        if (instance == null)
            instance = new PhotoListManager();
        return instance;
    }

    private Context mContext;
    private PhotoItemCollectionDao mPhotoItemCollectionDao;

    private PhotoListManager() {
        mContext = Contextor.getInstance().getContext();
    }

    public PhotoItemCollectionDao getPhotoItemCollectionDao() {
        return mPhotoItemCollectionDao;
    }

    public void setPhotoItemCollectionDao(PhotoItemCollectionDao photoItemCollectionDao) {
        mPhotoItemCollectionDao = photoItemCollectionDao;
    }
}
