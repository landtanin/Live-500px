package com.landtanin.practice2.dataType;

import android.os.Bundle;

/**
 * Created by landtanin on 2/25/2017 AD.
 */

public class MutableInteger {

    private int value;

    public MutableInteger(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Bundle onSaveInstanceState() {

        Bundle bundle = new Bundle();
        bundle.putInt("value", value);
        return bundle;

    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        value = savedInstanceState.getInt("value");
    }
}
