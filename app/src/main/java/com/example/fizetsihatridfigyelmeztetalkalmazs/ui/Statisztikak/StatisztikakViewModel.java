package com.example.fizetsihatridfigyelmeztetalkalmazs.ui.Statisztikak;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StatisztikakViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private MutableLiveData<String> mText;

    public StatisztikakViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Ez a Statisztikák fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}