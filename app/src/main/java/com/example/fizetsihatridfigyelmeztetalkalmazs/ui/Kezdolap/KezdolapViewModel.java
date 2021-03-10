package com.example.fizetsihatridfigyelmeztetalkalmazs.ui.Kezdolap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class KezdolapViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public KezdolapViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Ez a kezdőlap fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}