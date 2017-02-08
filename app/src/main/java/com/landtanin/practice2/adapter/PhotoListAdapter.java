package com.landtanin.practice2.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.landtanin.practice2.dao.PhotoItemDao;
import com.landtanin.practice2.manager.PhotoListManager;
import com.landtanin.practice2.view.PhotoListItem;

/**
 * Created by landtanin on 2/3/2017 AD.
 */

public class PhotoListAdapter extends BaseAdapter {
    @Override
    public int getCount() {

        if (PhotoListManager.getInstance().getPhotoItemCollectionDao() == null) {
            return 0;
        }
        if (PhotoListManager.getInstance().getPhotoItemCollectionDao().getData() == null) {
            return 0;
        }

        return PhotoListManager.getInstance().getPhotoItemCollectionDao().getData().size();
    }

    @Override
    public Object getItem(int i) {
        return PhotoListManager.getInstance().getPhotoItemCollectionDao().getData().get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    // --------------single ListView type--------------

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        PhotoListItem item;

            if (view != null) {
                item = (PhotoListItem) view;

            }
            else {
                item = new PhotoListItem(viewGroup.getContext());
            }

        PhotoItemDao dao = (PhotoItemDao) getItem(i);
        item.setNameText(dao.getCaption());
        item.setDescriptionText(dao.getUserName() + "\n" + dao.getCamera());
        item.setImageUrl(dao.getImageUrl());
        return item;
    }

    // --------------multiple ListView type--------------

//    @Override
//    public int getViewTypeCount() {
//        return 2;
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return position % 2 == 0 ? 0 : 1;
//    }
//
//    @Override
//    public View getView(int i, View view, ViewGroup viewGroup) {
//
//        if (getItemViewType(i)== 0) {
//
//            PhotoListItem item;
//
//            if (view != null) {
//                item = (PhotoListItem) view;
//
//            }
//            else {
//                item = new PhotoListItem(viewGroup.getContext());
//            }
//
//            return item;
//
//        } else {
//
//            TextView item;
//
//            if (view != null) {
//
//                item = (TextView) view;
//
//            }
//            else {
//                item = new TextView(viewGroup.getContext());
//            }
//
//            item.setText("Position :" + i);
//
//            return item;
//
//        }
//
//    }
}
