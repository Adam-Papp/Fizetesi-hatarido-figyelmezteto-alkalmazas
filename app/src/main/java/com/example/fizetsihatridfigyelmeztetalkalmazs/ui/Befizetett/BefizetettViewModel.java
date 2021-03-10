package com.example.fizetsihatridfigyelmeztetalkalmazs.ui.Befizetett;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BefizetettViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public BefizetettViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}