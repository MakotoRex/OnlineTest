package com.test.onlinetest.models;

import android.app.Activity;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.test.onlinetest.BR;

public class MainActivityModel extends BaseObservable{

    private Activity mActivity;
    private String status;
    private boolean isReady;

    public MainActivityModel(Activity activity)
    {
        mActivity = activity;
    }

    @Bindable
    public String getStatus() { return status; }

    public void setStatus(String mText)
    {
        this.status = mText;
        notifyPropertyChanged(BR.status);
    }

    @Bindable
    public boolean getIsReady() {
        return isReady;
    }

    public void setIsReady(boolean isReady)
    {
        this.isReady = isReady;
        notifyPropertyChanged(BR.isReady);
    }
}