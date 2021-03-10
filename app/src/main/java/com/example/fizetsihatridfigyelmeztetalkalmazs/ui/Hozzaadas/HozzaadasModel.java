package com.example.fizetsihatridfigyelmeztetalkalmazs.ui.Hozzaadas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HozzaadasModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HozzaadasModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}